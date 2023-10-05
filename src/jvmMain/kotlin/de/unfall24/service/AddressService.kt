package de.unfall24.service

import com.google.inject.Inject
import de.unfall24.extension.withProfile
import de.unfall24.model.Address
import de.unfall24.repository.AddressRepository
import io.ktor.server.application.*

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual class AddressService : IAddressService {

    @Inject
    lateinit var call: ApplicationCall

    override suspend fun getAddressList(search: String?, types: String, sort: Sort) = call.withProfile { user ->
        AddressRepository.list(user.id!!)
    }

    override suspend fun addAddress(address: Address): Address = call.withProfile { user ->
        AddressRepository.add(address, user.id!!)
    }

    override suspend fun updateAddress(address: Address) = call.withProfile { user ->
        AddressRepository.update(address, user.id!!)
    }

    override suspend fun deleteAddress(id: Int): Boolean = call.withProfile { user ->
        AddressRepository.remove(id, user.id!!)
    }
}