package com.prettyteeth

import com.prettyteeth.repository.DataRepository
import com.prettyteeth.routes.configureApiRoutes
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import java.io.File

fun Application.module() {
    val repository = DataRepository()
    
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }
    
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)
        anyHost()
    }
    
    configureApiRoutes(repository)
    
    routing {
        // Serve uploaded files
        get("/uploads/{filename}") {
            val filename = call.parameters["filename"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            val file = File("uploads/$filename")
            if (file.exists()) {
                call.respondFile(file)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        
        // Serve static files
        get("/") {
            call.respondText(
                """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Pretty Teeth - Dental Care</title>
                </head>
                <body>
                    <h1>Pretty Teeth API</h1>
                    <p>Welcome to Pretty Teeth - Your dental care companion</p>
                </body>
                </html>
                """.trimIndent(),
                ContentType.Text.Html
            )
        }
    }
}
