package com.arman.huisapp.client.store

import com.arman.huisapp.common.Config
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import org.slf4j.LoggerFactory
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.nio.file.Files
import java.nio.file.Paths

object Store {

    val users: UserModule
    val groups: GroupModule

    init {
        val mapper = ObjectMapper(YAMLFactory()).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        val config = with(Files.newInputStream(Paths.get("C:\\Users\\Coen\\IdeaProjects\\huisapp-library\\api\\src\\main\\resources\\application.yml"))) {
            mapper.readValue(this, Config::class.java)
        }

        val retrofit = Retrofit.Builder()
                .baseUrl("http://${config.server.host}:${config.server.port}")
                .addConverterFactory(JacksonConverterFactory.create())
                .build()

        users = retrofit.create(UserModule::class.java)
        groups = retrofit.create(GroupModule::class.java)
    }

}

private val Logger = LoggerFactory.getLogger(Store::class.java)

fun main() {
    val response = Store.users.findAll().execute()
    if (response.isSuccessful) {
        response.body()?.forEach {
            Logger.info(it.toString())
        }
    }
}