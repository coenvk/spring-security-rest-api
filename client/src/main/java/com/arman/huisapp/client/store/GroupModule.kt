package com.arman.huisapp.client.store

import com.arman.huisapp.common.model.Group
import com.arman.huisapp.common.model.request.group.CreateRequest
import retrofit2.Call
import retrofit2.http.*

interface GroupModule {

    @FormUrlEncoded
    @POST("/api/groups")
    fun create(@Body request: CreateRequest): Call<Group>

    @GET("/api/groups")
    fun findAll(): Call<List<Group>>

    @GET("/api/groups/{groupId}")
    fun findById(@Path("groupId") groupId: Long): Call<Group>

}