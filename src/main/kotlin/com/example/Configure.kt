package com.example

import com.example.config.Config
import com.example.config.JDBC
import com.example.config.JWTConfig
import io.ktor.server.application.*

fun Application.config() = environment.config.run {
    Config(
        jwt = JWTConfig(
            domain = property("jwt.domain").getString(),
            secret = property("jwt.secret").getString()
        ),
        development = property("ktor.development").getString().toBoolean(),
        dropDB = property("database.drop").getString().toBoolean(),
        jdbc = JDBC(
                driver = property("database.jdbc.driver").getString(),
                url = property("database.jdbc.url").getString(),
                schema = property("database.jdbc.schema").getString(),
                username = property("database.jdbc.username").getString(),
                password = property("database.jdbc.password").getString()
            )
    )
}
