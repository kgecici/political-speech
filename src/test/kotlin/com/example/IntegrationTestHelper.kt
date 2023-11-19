package com.example

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

@Suppress("unused")
fun Application.testModule(testing: Boolean = false) {
    routing {
        get("/data1.csv") {
            val csvContent = """
                    Speaker;Topic;Date;Words
                    John Doe;Technology,2022-01-01;500
                    Jane Smith;Education,2022-02-01;700
                    Bob Brown;Health;2022-03-01;600
                """.trimIndent()
            call.respond(csvContent)
        }
        get("/data1-duplicate.csv") {
            val csvContent = """
                    Speaker;Topic;Date;Words
                    John Doe;Technology,2022-01-01;500
                    Jane Smith;Education,2022-02-01;700
                    Bob Brown;Health;2022-03-01;600
                """.trimIndent()
            call.respond(csvContent)
        }
        get("/malicious.csv") {
            call.respond("a".repeat(1000))
        }
    }
}

