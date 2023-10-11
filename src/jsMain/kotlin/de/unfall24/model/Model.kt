package de.unfall24.model

import de.unfall24.AppScope
import de.unfall24.Security
import de.unfall24.service.AddressService
import de.unfall24.service.UserService
import de.unfall24.service.RegisterUserService
import de.unfall24.service.Sort
import io.kvision.state.ObservableList
import io.kvision.state.ObservableValue
import io.kvision.state.observableListOf
import io.kvision.utils.syncWithList
import kotlinx.coroutines.launch

object Model {

    private val addressService = AddressService()
    private val userService = UserService()
    private val registerUserService = RegisterUserService()

    val addresses: ObservableList<Address> = observableListOf()
    val user = ObservableValue(User())

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

    suspend fun readUser() {
        Security.withAuth {
            user.value = userService.getUser()
        }
    }

    suspend fun registerUser(user: User): Boolean {
        return try {
            registerUserService.registerUser(user)
        } catch (e: Exception) {
            console.log(e)
            false
        }
    }
}
