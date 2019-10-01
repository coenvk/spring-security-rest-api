package com.arman.huisapp.client.store

import com.arman.huisapp.common.model.User
import com.arman.huisapp.common.model.request.user.LoginRequest
import com.arman.huisapp.common.model.request.user.RegisterRequest
import retrofit2.Call
import retrofit2.http.*

interface UserModule {

    @FormUrlEncoded
    @POST("/api/users/login")
    fun login(@Body request: LoginRequest): Call<User>

    @FormUrlEncoded
    @POST("/api/users")
    fun register(@Body request: RegisterRequest): Call<User>

    @GET("/api/users")
    fun findAll(): Call<List<User>>

    @GET("/api/users/{userId}")
    fun findById(@Path("userId") userId: Long): Call<User>

}