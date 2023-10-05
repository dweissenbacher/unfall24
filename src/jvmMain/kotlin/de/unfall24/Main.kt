package de.unfall24

import de.unfall24.database.Db
import de.unfall24.database.Db.dbQuery
import de.unfall24.database.UserTable
import de.unfall24.model.User
import de.unfall24.service.IAddressService
import de.unfall24.service.IUserService
import de.unfall24.service.IRegisterProfileService
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
import org.apache.commons.codec.digest.DigestUtils
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
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
            validate { credentials ->
                dbQuery {
                    UserTable.select {
                        (UserTable.username eq credentials.name) and (UserTable.password eq DigestUtils.sha256Hex(
                            credentials.password
                        ))
                    }.firstOrNull()?.let {
                        UserIdPrincipal(credentials.name)
                    }
                }
            }
            skipWhen { call -> call.sessions.get<User>() != null }
        }
    }

    routing {
        applyRoutes(getServiceManager<IRegisterProfileService>())
        authenticate {
            post("login") {
                val principal = call.principal<UserIdPrincipal>()
                val result = if (principal != null) {
                    dbQuery {
                        UserTable.select { UserTable.username eq principal.name }.firstOrNull()?.let {
                            val user =
                                User(it[UserTable.id], it[UserTable.name], it[UserTable.username].toString(), null, null)
                            call.sessions.set(user)
                            HttpStatusCode.OK
                        } ?: HttpStatusCode.Unauthorized
                    }
                } else {
                    HttpStatusCode.Unauthorized
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
