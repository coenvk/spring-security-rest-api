package com.arman.springsecurityrest.common

import com.fasterxml.jackson.annotation.JsonProperty

class Config {

    @JsonProperty("server")
    lateinit var server: Server
        private set

    inner class Server {

        @JsonProperty("port")
        var port: Int = 0
            private set

        @JsonProperty("address")
        lateinit var host: String
            private set

    }

}