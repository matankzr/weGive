package com.example.wegive

import com.google.firebase.firestore.PropertyName

//
data class User(var userName: String = "", var email: String= "", var firstName: String= "", var lastName: String= "",
                var totalAmountGiven: Int=0, var myCoins: Double= 0.0, var totalNumberOfDonations: Int=0, var useForColu: Boolean=true,
                @get:PropertyName("profile_image_url") @set: PropertyName("profile_image_url") var profile_image_url: String = "", var hasCC: Boolean = false, var last4: String=""){
    override fun toString(): String {
        return "$userName $firstName $lastName"
    }

    //constructor(): this("", "", "","",0,0,0)
}

//
//class User{
//
//    lateinit var userName: String
//    lateinit var email:String
//    lateinit var firstName: String
//    lateinit var lastName: String
//    var totalAmountGiven: Int = 0
//    var myCoins: Int = 0
//    var TotalDonations: Int = 0
//
//    constructor(){ }
//
//    constructor(userName: String, firstName: String, lastName: String, email: String){
//        this.userName = userName
//        this.firstName = firstName
//        this.lastName = lastName
//        this.email = email
//    }
//}
