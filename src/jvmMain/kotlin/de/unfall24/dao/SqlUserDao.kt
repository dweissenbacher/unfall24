package de.unfall24.dao

import de.unfall24.database.Db
import de.unfall24.database.UserTable
import de.unfall24.model.User
import de.unfall24.service.Sort
import org.apache.commons.codec.digest.DigestUtils
import org.jetbrains.exposed.sql.insert

class SqlUserDao: UserDao {

    override suspend fun create(user: User){
        Db.dbQuery {
            UserTable.insert {
                it[this.name] = user.name!!
                it[this.username] = user.username!!
                it[this.password] = DigestUtils.sha256Hex(user.password!!)
            }
        }
    }

    override suspend fun read(id: Int): User {
        TODO("Not yet implemented")
    }

    override suspend fun update(user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun search(search: String?, types: String, sort: Sort): List<User> {
        TODO("Not yet implemented")
    }

}