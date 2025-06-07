// app/src/main/java/fct/nova/beeppisca/service/user/UserService.kt
package fct.nova.beeppisca.service.user

import fct.nova.beeppisca.domain.SimpleUser
import fct.nova.beeppisca.service.URIS
import fct.nova.beeppisca.storage.LocalDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class UserService(
    private val localDataStore: LocalDataStore
) {
    private val client = OkHttpClient()
    private var sessionCookie: String? = null

    /** Load the cookie we last saved (if any) */
    suspend fun loadSessionCookie() {
        sessionCookie = localDataStore.sessionCookieFlow.first()
    }

    /** Register a new user */
    suspend fun register(
        username: String,
        password: String,
        email: String,
        type: String
    ): Boolean = withContext(Dispatchers.IO) {
        val json = JSONObject()
            .put("username", username)
            .put("password", password)
            .put("email", email)
            .put("type", type)

        val body = json
            .toString()
            .toRequestBody("application/json".toMediaTypeOrNull())

        val request = Request.Builder()
            .url(URIS.User.CREATE)
            .post(body)
            .build()

        client.newCall(request).execute().use { response ->
            return@use response.isSuccessful
        }
    }

    /** Login and capture JSESSIONID */
    suspend fun login(username: String, password: String): Boolean = withContext(Dispatchers.IO) {
        val form = FormBody.Builder()
            .add("username", username)
            .add("password", password)
            .build()

        val request = Request.Builder()
            .url(URIS.User.LOGIN)
            .post(form)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return@use false

            // find the JSESSIONID in Set-Cookie header
            val rawCookies = response.headers("Set-Cookie")
            val jsess = rawCookies
                .firstOrNull { it.startsWith("JSESSIONID") }
                ?.substringBefore(";")

            jsess?.let {
                sessionCookie = it
                localDataStore.saveSessionCookie(it)
            }

            return@use sessionCookie != null
        }
    }

    /** Logout (POST empty body) */
    suspend fun logout(): Boolean = withContext(Dispatchers.IO) {
        val body = ByteArray(0).toRequestBody(null)
        val builder = Request.Builder()
            .url(URIS.User.LOGOUT)
            .post(body)

        sessionCookie?.let { builder.addHeader("Cookie", it) }

        client.newCall(builder.build()).execute().use { response ->
            if (response.isSuccessful) {
                sessionCookie = null
                localDataStore.clearSessionCookie()
            }
            return@use response.isSuccessful
        }
    }

    /** Get the “current user” from /me */
    suspend fun getMe(): SimpleUser? = withContext(Dispatchers.IO) {
        if (sessionCookie == null) loadSessionCookie()

        val builder = Request.Builder().url(URIS.User.ME)
        sessionCookie?.let { builder.addHeader("Cookie", it) }

        client.newCall(builder.build()).execute().use { response ->
            if (!response.isSuccessful) return@use null
            val body = response.body?.string() ?: return@use null
            return@use parseSimpleUserFromJson(body)
        }
    }

    private fun parseSimpleUserFromJson(json: String): SimpleUser? {
        return try {
            val o = JSONObject(json)
            SimpleUser(
                id       = o.optString("id"),
                name     = o.optString("name"),
                email    = o.optString("email"),
                password = o.optString("password", null),
                token    = o.optString("token",    null),
                isAdmin  = o.optBoolean("isAdmin",  false)
            )
        } catch (e: Exception) {
            null
        }
    }
}
