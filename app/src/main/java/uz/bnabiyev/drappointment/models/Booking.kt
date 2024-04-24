package uz.bnabiyev.drappointment.models

class Booking {
    var date: String? = null
    var time: String? = null
    var userUid: String? = null
    var doctorUid: String? = null
    var userName:String?=null
    var doctorName:String?=null

    constructor(
        date: String?,
        time: String?,
        userUid: String?,
        doctorUid: String?,
        userName: String?,
        doctorName: String?
    ) {
        this.date = date
        this.time = time
        this.userUid = userUid
        this.doctorUid = doctorUid
        this.userName = userName
        this.doctorName = doctorName
    }

    constructor()
}