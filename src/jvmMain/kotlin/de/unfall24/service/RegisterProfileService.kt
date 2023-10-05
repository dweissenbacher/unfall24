package de.unfall24.service

import de.unfall24.Db
import de.unfall24.UserDao
import de.unfall24.model.Profile
import org.apache.commons.codec.digest.DigestUtils
import org.jetbrains.exposed.sql.insert

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class RegisterProfileService : IRegisterProfileService {

    override suspend fun registerProfile(profile: Profile, password: String): Boolean {
        try {
            Db.dbQuery {
                UserDao.insert {
                    it[this.name] = profile.name!!
                    it[this.username] = profile.username!!
                    it[this.password] = DigestUtils.sha256Hex(password)
                }
            }
        } catch (e: Exception) {
            throw Exception("Register operation failed!")
        }
        return true
    }

}