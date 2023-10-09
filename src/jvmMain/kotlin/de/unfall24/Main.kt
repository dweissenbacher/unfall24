package de.unfall24

import de.unfall24.database.Db
import de.unfall24.model.User
import de.unfall24.repository.UserRepository
import de.unfall24.service.IAddressService
import de.unfall24.service.IRegisterUserService
import de.unfall24.service.IUserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.kvision.remote.applyRoutes
import io.kvision.remote.getServiceManager
import io.kvision.remote.kvisionInit
import kotlin.collections.set

fun Application.main() {
    install(Compression)
    install(DefaultHeaders)
    install(CallLogging)
    install(Sessions) {
        cookie<User>("KTSESSION", storage = SessionStorageMemory()) {
            cookie.path = "/"
            cookie.extensions["SameSite"] = "strict"
        }
    }
    Db.init(environment.config)

    install(Authentication) {
        form {
            userParamName = "username"
            passwordParamName = "password"
            validate { credentials -> UserRepository.validate(credentials) }
            skipWhen { call -> call.sessions.get<User>() != null }
        }
    }

    routing {
        applyRoutes(getServiceManager<IRegisterUserService>())
        authenticate {
            post("login") {
                var result = HttpStatusCode.Unauthorized
                call.principal<UserIdPrincipal>()?.let { principal ->
                    UserRepository.getAuthenticatedUser(principal)?.let { user ->
                        call.sessions.set(user)
                        result = HttpStatusCode.OK
                    }
                }

                call.respond(result)
            }

            get("logout") {
                call.sessions.clear<User>()
                call.respondRedirect("/")
            }

            applyRoutes(getServiceManager<IAddressService>())
            applyRoutes(getServiceManager<IUserService>())
        }
    }
    kvisionInit()
}
