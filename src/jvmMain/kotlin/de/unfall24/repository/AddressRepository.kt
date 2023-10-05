package de.unfall24.repository

import de.unfall24.dao.AddressDao
import de.unfall24.dao.SqlAddressDao
import de.unfall24.model.Address
import de.unfall24.service.Sort

object AddressRepository {

    private val addressDao: AddressDao = SqlAddressDao()

    suspend fun list(forUserId: Int): List<Address> {
        return addressDao.fetchAll(forUserId, Sort.FIRST_NAME)
    }

    suspend fun remove(addressId: Int, forUserId: Int): Boolean {
        return addressDao.delete(addressId, forUserId)
    }

    suspend fun update(address: Address, forUserId: Int): Address {
        return addressDao.update(address, forUserId)
    }

    suspend fun add(address: Address, forUserId: Int): Address {
        return addressDao.create(address, forUserId)
    }
}