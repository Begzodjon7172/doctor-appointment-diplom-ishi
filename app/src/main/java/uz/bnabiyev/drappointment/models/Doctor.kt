package uz.bnabiyev.drappointment.models

import java.io.Serializable

class Doctor:Serializable {
    var firstName: String? = null
    var lastName: String? = null
    var email: String? = null
    var phoneNumber: String? = null
    var password: String? = null
    var master: String? = null
    var uid: String? = null


    constructor(
        firstName: String?,
        lastName: String?,
        email: String?,
        phoneNumber: String?,
        password: String?,
        master: String?,
        uid: String?
    ) {
        this.firstName = firstName
        this.lastName = lastName
        this.email = email
        this.phoneNumber = phoneNumber
        this.password = password
        this.master = master
        this.uid = uid
    }


    constructor()

    override fun toString(): String {
        return "Doctor(firstName=$firstName, lastName=$lastName, email=$email, phoneNumber=$phoneNumber, password=$password, master=$master)"
    }
}

