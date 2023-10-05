package de.unfall24

import de.unfall24.model.Address
import de.unfall24.model.Profile
import de.unfall24.service.AddressService
import de.unfall24.service.ProfileService
import de.unfall24.service.RegisterProfileService
import de.unfall24.service.Sort
import io.kvision.state.ObservableList
import io.kvision.state.ObservableValue
import io.kvision.state.observableListOf
import io.kvision.utils.syncWithList
import kotlinx.coroutines.launch

object Model {

    private val addressService = AddressService()
    private val profileService = ProfileService()
    private val registerProfileService = RegisterProfileService()

    val addresses: ObservableList<Address> = observableListOf()
    val profile = ObservableValue(Profile())

    var search: String? = null
        set(value) {
            field = value
            AppScope.launch {
                getAddressList()
            }
        }
    var types: String = "all"
        set(value) {
            field = value
            AppScope.launch {
                getAddressList()
            }
        }
    var sort = Sort.FIRST_NAME
        set(value) {
            field = value
            AppScope.launch {
                getAddressList()
            }
        }

    suspend fun getAddressList() {
        Security.withAuth {
            val newAddresses = addressService.getAddressList(search, types, sort)
            addresses.syncWithList(newAddresses)
        }
    }

    suspend fun addAddress(address: Address) {
        Security.withAuth {
            addressService.addAddress(address)
            getAddressList()
        }
    }

    suspend fun updateAddress(address: Address) {
        Security.withAuth {
            addressService.updateAddress(address)
            getAddressList()
        }
    }

    suspend fun deleteAddress(id: Int): Boolean {
        return Security.withAuth {
            val result = addressService.deleteAddress(id)
            getAddressList()
            result
        }
    }

    suspend fun readProfile() {
        Security.withAuth {
            profile.value = profileService.getProfile()
        }
    }

    suspend fun registerProfile(profile: Profile, password: String): Boolean {
        return try {
            registerProfileService.registerProfile(profile, password)
        } catch (e: Exception) {
            console.log(e)
            false
        }
    }
}