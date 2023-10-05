package de.unfall24.service

import de.unfall24.model.Address
import io.kvision.annotations.KVService

@KVService
interface IAddressService {
    suspend fun getAddressList(search: String?, types: String, sort: Sort): List<Address>
    suspend fun addAddress(address: Address): Address
    suspend fun updateAddress(address: Address): Address
    suspend fun deleteAddress(id: Int): Boolean
}