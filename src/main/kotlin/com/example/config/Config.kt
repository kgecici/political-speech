package com.example.config

import com.example.repository.DatabaseFactory

data class Config(
    val jwt: JWTConfig,
    val development: Boolean,
    val jdbc: JDBC?,
    val dropDB: Boolean
) {
    init {
        DatabaseFactory.init(jdbc!!.driver, jdbc.url, jdbc.schema, jdbc.username, jdbc.password, dropDB)
    }
}