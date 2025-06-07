package fct.nova.beeppisca.service.user

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject

class UserService(private val baseUrl: String) {
    private val client = OkHttpClient()
    private var sessionCookie: String? = null

    // Register (JSON body)
    suspend fun register(username: String, password: String, email: String, type: String): Boolean = withContext(Dispatchers.IO) {
        val url = "$baseUrl/api/v1/user/create"
        val json = JSONObject()
            .put("username", username)
            .put("password", password)
            .put("email", email)
            .put("type", type)
        val body = RequestBody.create("application/json".toMediaTypeOrNull(), json.toString())
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()
        client.newCall(request).execute().use { response ->
            response.isSuccessful // true if created, else false (you can parse errors if needed)
        }
    }

    // Login (form fields, NOT JSON!)
    suspend fun login(username: String, password: String): Boolean = withContext(Dispatchers.IO) {
        val url = "$baseUrl/api/v1/user/login"
        val formBody = FormBody.Builder()
            .add("username", username)
            .add("password", password)
            .build()
        val request = Request.Builder()
            .url(url)
            .post(formBody)
            .build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return@use false
            // Extract JSESSIONID cookie
            val cookieHeader = response.headers("Set-Cookie")
            val jsession = cookieHeader.find { it.startsWith("JSESSIONID") }
            jsession?.let {
                // Only store the session id, ignore additional attributes like Path, HttpOnly
                sessionCookie = it.substringBefore(";")
            }
            sessionCookie != null // true if we have a session cookie
        }
    }

    // Logout (send session cookie)
    suspend fun logout(): Boolean = withContext(Dispatchers.IO) {
        val url = "$baseUrl/api/v1/user/logout"
        val builder = Request.Builder()
            .url(url)
            .post(RequestBody.create(null, ByteArray(0)))
        sessionCookie?.let {
            builder.addHeader("Cookie", it)
        }
        val request = builder.build()
        client.newCall(request).execute().use { response ->
            if (response.isSuccessful) sessionCookie = null
            response.isSuccessful
        }
    }

    // Example for authenticated requests: get "me"
    suspend fun getMe(): String? = withContext(Dispatchers.IO) {
        val url = "$baseUrl/api/v1/user/me"
        val builder = Request.Builder().url(url)
        sessionCookie?.let { builder.addHeader("Cookie", it) }
        val request = builder.build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return@use null
            response.body?.string()
        }
    }
}


data class LoginResponse(
    val token: String,
    val userId: Long,
    val username: String
)
