package com.arman.springsecurityrest.client.store

import com.arman.springsecurityrest.common.Config
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import org.slf4j.LoggerFactory
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.net.CookieManager
import java.net.CookiePolicy
import java.nio.file.Files
import java.nio.file.Paths

object Store {

    val users: UserModule

    init {
        val mapper = ObjectMapper(YAMLFactory()).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        val config = with(Files.newInputStream(Paths.get(".\\api\\src\\main\\resources\\application.yml"))) {
            mapper.readValue(this, Config::class.java)
        }

        val cookieManager = CookieManager()
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)

        val client = OkHttpClient.Builder().cookieJar(JavaNetCookieJar(cookieManager)).build()

        val retrofit = Retrofit.Builder()
                .baseUrl("http://${config.server.host}:${config.server.port}")
                .addConverterFactory(JacksonConverterFactory.create())
                .client(client)
                .build()

        users = retrofit.create(UserModule::class.java)
    }

}

private val Logger = LoggerFactory.getLogger(Store::class.java)

fun main() {
    val registerResponse = Store.users.register("test", "test@test.com", "test").execute()
    if (!registerResponse.isSuccessful) {
        Logger.error(registerResponse.errorBody()?.string())
    }
    Logger.info("Header Set-Cookie: ${registerResponse.headers()["Set-Cookie"]}")
    val loginResponse = Store.users.login("test@test.com", "test").execute()
    if (!loginResponse.isSuccessful) {
        Logger.error(loginResponse.errorBody()?.string())
    }
    Logger.info("Header Set-Cookie: ${loginResponse.headers()["Set-Cookie"]}")

    val response = Store.users.findLoggedInUser().execute()
    if (response.isSuccessful) {
        Logger.info("Successful retrieval of users.")
        Logger.info("Logged in as: ${response.body()?.name}")
    } else {
        Logger.error(response.errorBody()?.string())
    }
}