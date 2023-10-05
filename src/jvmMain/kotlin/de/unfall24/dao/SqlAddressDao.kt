package de.unfall24.dao

import com.github.andrewoma.kwery.core.builder.query
import de.unfall24.database.Db.queryList
import de.unfall24.database.AddressTable
import de.unfall24.database.Db
import de.unfall24.model.Address
import de.unfall24.service.Sort
import org.jetbrains.exposed.sql.*
import org.joda.time.DateTime
import java.sql.ResultSet
import java.time.ZoneId
import java.util.*
import java.util.Date

class SqlAddressDao: AddressDao {

    override suspend fun create(address: Address, forUserId: Int): Address {
        val key = Db.dbQuery {
            (AddressTable.insert {
                it[firstName] = address.firstName
                it[lastName] = address.lastName
                it[email] = address.email
                it[phone] = address.phone
                it[postalAddress] = address.postalAddress
                it[favourite] = address.favourite ?: false
                it[createdAt] = DateTime()
                it[userId] = forUserId

            } get AddressTable.id)
        }

        return getAddress(key)!!
    }

    override suspend fun update(address: Address, forUserId: Int): Address {
        return address.id?.let {
            getAddress(it)?.let { oldAddress ->
                Db.dbQuery {
                    AddressTable.update({ AddressTable.id eq it }) {
                        it[firstName] = address.firstName
                        it[lastName] = address.lastName
                        it[email] = address.email
                        it[phone] = address.phone
                        it[postalAddress] = address.postalAddress
                        it[favourite] = address.favourite ?: false
                        it[createdAt] = oldAddress.createdAt
                            ?.let { DateTime(Date.from(it.atZone(ZoneId.systemDefault()).toInstant())) }
                        it[userId] = forUserId
                    }
                }
            }
            getAddress(it)
        } ?: throw IllegalArgumentException("The ID of the address not set")
    }

    override suspend fun delete(addressId: Int, forUserId: Int): Boolean {
        return Db.dbQuery {
            AddressTable.deleteWhere { (AddressTable.userId eq forUserId) and (AddressTable.id eq addressId) } > 0
        }
    }

    override suspend fun fetchAll(forUserId: Int, sort: Sort): List<Address> {
        return Db.dbQuery {
            val query = query {
                select("SELECT * FROM address")
                whereGroup {
                    where("user_id = :user_id")
                    parameter("user_id", forUserId)
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

    private fun toAddress(rs: ResultSet): Address {
        return Address(
            id = rs.getInt(AddressTable.id.name),
            firstName = rs.getString(AddressTable.firstName.name),
            lastName = rs.getString(AddressTable.lastName.name),
            email = rs.getString(AddressTable.email.name),
            phone = rs.getString(AddressTable.phone.name),
            postalAddress = rs.getString(AddressTable.postalAddress.name),
            favourite = rs.getBoolean(AddressTable.favourite.name),
            createdAt = rs.getTimestamp(AddressTable.createdAt.name)?.toInstant()
                ?.atZone(ZoneId.systemDefault())?.toLocalDateTime(),
            userId = rs.getInt(AddressTable.userId.name)
        )
    }

    private fun toAddress(row: ResultRow): Address {
        return Address(
            id = row[AddressTable.id],
            firstName = row[AddressTable.firstName],
            lastName = row[AddressTable.lastName],
            email = row[AddressTable.email],
            phone = row[AddressTable.phone],
            postalAddress = row[AddressTable.postalAddress],
            favourite = row[AddressTable.favourite],
            createdAt = row[AddressTable.createdAt]?.millis?.let { Date(it) }?.toInstant()
                ?.atZone(ZoneId.systemDefault())?.toLocalDateTime(),
            userId = row[AddressTable.userId]
        )
    }

    private suspend fun getAddress(id: Int): Address? = Db.dbQuery {
        AddressTable.select {
            AddressTable.id eq id
        }.mapNotNull { toAddress(it) }.singleOrNull()
    }
}