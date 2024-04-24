package uz.bnabiyev.drappointment.models

class User {
    var firstName: String? = null
    var lastName: String? = null
    var email: String? = null
    var phoneNumber: String? = null
    var password: String? = null
    var uid: String? = null

    constructor(
        firstName: String?,
        lastName: String?,
        email: String?,
        phoneNumber: String?,
        password: String?,
        uid: String?
    ) {
        this.firstName = firstName
        this.lastName = lastName
        this.email = email
        this.phoneNumber = phoneNumber
        this.password = password
        this.uid = uid
    }

    constructor()
}
