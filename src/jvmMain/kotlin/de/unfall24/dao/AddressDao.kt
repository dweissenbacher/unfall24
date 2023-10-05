package de.unfall24.dao

import de.unfall24.model.Address
import de.unfall24.model.User
import de.unfall24.service.Sort

interface AddressDao {

    suspend fun create(address: Address, forUserId: Int): Address

    suspend fun update(address: Address, forUserId: Int): Address

    suspend fun delete(addressId: Int, forUserId: Int): Boolean

    suspend fun fetchAll(forUserId: Int, sort: Sort) :List<Address>

}