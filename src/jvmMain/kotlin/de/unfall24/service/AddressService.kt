package de.unfall24.service

import com.github.andrewoma.kwery.core.builder.query
import com.google.inject.Inject
import de.unfall24.AddressDao
import de.unfall24.Db
import de.unfall24.Db.queryList
import de.unfall24.extensions.withProfile
import de.unfall24.model.Address
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.*
import org.joda.time.DateTime
import java.sql.ResultSet
import java.time.ZoneId
import java.util.Date

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class AddressService : IAddressService {

    @Inject
    lateinit var call: ApplicationCall

    override suspend fun getAddressList(search: String?, types: String, sort: Sort) =
        call.withProfile { profile ->
            Db.dbQuery {
                val query = query {
                    select("SELECT * FROM address")
                    whereGroup {
                        where("user_id = :user_id")
                        parameter("user_id", profile.id)
                        search?.let {
                            where(
                                """(lower(first_name) like :search
                            OR lower(last_name) like :search
                            OR lower(email) like :search
                            OR lower(phone) like :search
                            OR lower(postal_address) like :search)""".trimMargin()
                            )
                            parameter("search", "%${it.lowercase()}%")
                        }
                        if (types == "fav") {
                            where("favourite")
                        }
                    }
                    when (sort) {
                        Sort.FIRST_NAME -> orderBy("lower(first_name)")
                        Sort.LAST_NAME -> orderBy("lower(last_name)")
                        Sort.EMAIL -> orderBy("lower(email)")
                        Sort.FAVOURITE -> orderBy("favourite")
                    }
                }
                queryList(query.sql, query.parameters) {
                    toAddress(it)
                }
            }
        }

    override suspend fun addAddress(address: Address) = call.withProfile { profile ->
        val key = Db.dbQuery {
            (AddressDao.insert {
                it[firstName] = address.firstName
                it[lastName] = address.lastName
                it[email] = address.email
                it[phone] = address.phone
                it[postalAddress] = address.postalAddress
                it[favourite] = address.favourite ?: false
                it[createdAt] = DateTime()
                it[userId] = profile.id!!

            } get AddressDao.id)
        }
        getAddress(key)!!
    }

    override suspend fun updateAddress(address: Address) = call.withProfile { profile ->
        address.id?.let {
            getAddress(it)?.let { oldAddress ->
                Db.dbQuery {
                    AddressDao.update({ AddressDao.id eq it }) {
                        it[firstName] = address.firstName
                        it[lastName] = address.lastName
                        it[email] = address.email
                        it[phone] = address.phone
                        it[postalAddress] = address.postalAddress
                        it[favourite] = address.favourite ?: false
                        it[createdAt] = oldAddress.createdAt
                            ?.let { DateTime(Date.from(it.atZone(ZoneId.systemDefault()).toInstant())) }
                        it[userId] = profile.id!!
                    }
                }
            }
            getAddress(it)
        } ?: throw IllegalArgumentException("The ID of the address not set")
    }

    override suspend fun deleteAddress(id: Int): Boolean = call.withProfile { profile ->
        Db.dbQuery {
            AddressDao.deleteWhere { (AddressDao.userId eq profile.id!!) and (AddressDao.id eq id) } > 0
        }
    }

    private suspend fun getAddress(id: Int): Address? = Db.dbQuery {
        AddressDao.select {
            AddressDao.id eq id
        }.mapNotNull { toAddress(it) }.singleOrNull()
    }

    private fun toAddress(row: ResultRow): Address =
        Address(
            id = row[AddressDao.id],
            firstName = row[AddressDao.firstName],
            lastName = row[AddressDao.lastName],
            email = row[AddressDao.email],
            phone = row[AddressDao.phone],
            postalAddress = row[AddressDao.postalAddress],
            favourite = row[AddressDao.favourite],
            createdAt = row[AddressDao.createdAt]?.millis?.let { Date(it) }?.toInstant()
                ?.atZone(ZoneId.systemDefault())?.toLocalDateTime(),
            userId = row[AddressDao.userId]
        )

    private fun toAddress(rs: ResultSet): Address =
        Address(
            id = rs.getInt(AddressDao.id.name),
            firstName = rs.getString(AddressDao.firstName.name),
            lastName = rs.getString(AddressDao.lastName.name),
            email = rs.getString(AddressDao.email.name),
            phone = rs.getString(AddressDao.phone.name),
            postalAddress = rs.getString(AddressDao.postalAddress.name),
            favourite = rs.getBoolean(AddressDao.favourite.name),
            createdAt = rs.getTimestamp(AddressDao.createdAt.name)?.toInstant()
                ?.atZone(ZoneId.systemDefault())?.toLocalDateTime(),
            userId = rs.getInt(AddressDao.userId.name)
        )
}