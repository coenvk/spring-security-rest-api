package com.arman.springsecurityrest.client.store

import com.arman.springsecurityrest.common.model.User
import retrofit2.Call
import retrofit2.http.*

interface UserModule {

    @FormUrlEncoded
    @Headers(
            "Content-Type: application/x-www-form-urlencoded"
    )
    @POST("/login")
    fun login(@Field("email") email: String, @Field("password") password: String): Call<Void>

    @FormUrlEncoded
    @Headers(
            "Content-Type: application/x-www-form-urlencoded"
    )
    @POST("/api/users")
    fun register(@Field("name") name: String, @Field("email") email: String, @Field("password") password: String): Call<User>

    @GET("/api/users?email={email}")
    fun findByEmail(@Path("email") email: String): Call<User>

    @GET("/api/users/current")
    fun findLoggedInUser(): Call<User>

    @GET("/api/users/{userId}")
    fun findById(@Path("userId") userId: Long): Call<User>

}