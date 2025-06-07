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
    // Backing field for in-memory session cookie
    private var sessionCookie: String? = null

    // Load session cookie from storage on demand or app launch
    suspend fun loadSessionCookie() {
        sessionCookie = localDataStore.sessionCookieFlow.first()
    }

    // Register (JSON body)
    suspend fun register(username: String, password: String, email: String, type: String): Boolean = withContext(Dispatchers.IO) {
        val json = JSONObject()
            .put("username", username)
            .put("password", password)
            .put("email", email)
            .put("type", type)
        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder()
            .url(URIS.User.CREATE)
            .post(body)
            .build()
        client.newCall(request).execute().use { response ->
            response.isSuccessful
        }
    }

    // Login (form fields)
    suspend fun login(username: String, password: String): Boolean = withContext(Dispatchers.IO) {
        val formBody = FormBody.Builder()
            .add("username", username)
            .add("password", password)
            .build()
        val request = Request.Builder()
            .url(URIS.User.LOGIN)
            .post(formBody)
            .build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return@use false
            // Extract JSESSIONID cookie
            val cookieHeader = response.headers("Set-Cookie")
            val jSession = cookieHeader.find { it.startsWith("JSESSIONID") }
            jSession?.let {
                sessionCookie = it.substringBefore(";")
                // Save to local storage as well
                localDataStore.saveSessionCookie(sessionCookie!!)
            }
            sessionCookie != null
        }
    }

    // Logout
    suspend fun logout(): Boolean = withContext(Dispatchers.IO) {
        val builder = Request.Builder()
            .url(URIS.User.LOGOUT)
            .post(RequestBody.create(null, ByteArray(0)))
        sessionCookie?.let {
            builder.addHeader("Cookie", it)
        }
        val request = builder.build()
        client.newCall(request).execute().use { response ->
            if (response.isSuccessful) {
                sessionCookie = null
                localDataStore.clearSessionCookie()
            }
            response.isSuccessful
        }
    }

    // Get "me"
    suspend fun getMe(): SimpleUser? = withContext(Dispatchers.IO) {
        // Always make sure we have the session cookie (from memory or storage)
        if (sessionCookie == null) {
            loadSessionCookie()
        }

        val builder = Request.Builder().url(URIS.User.ME)
        sessionCookie?.let { builder.addHeader("Cookie", it) }
        val request = builder.build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return@use null
            response.body?.string()?.let { body ->
                parseSimpleUserFromJson(body)
            }
        }
    }

    private fun parseSimpleUserFromJson(json: String): SimpleUser? {
        return try {
            val obj = JSONObject(json)
            SimpleUser(
                name = obj.optString("name"),
                email = obj.optString("email"),
                password = obj.optString("password", null),
                token = obj.optString("token", null),
                isAdmin = obj.optBoolean("isAdmin", false)
            )
        } catch (e: Exception) {
            null
        }
    }
}
