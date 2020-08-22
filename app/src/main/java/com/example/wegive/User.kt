package com.example.wegive

//
//data class User(val userName: String, val email: String, val firstName: String, val lastName: String,
//                val totalAmountGiven: Int, val myCoins: Int, val totalNumOfDonations: Int){
//
//    constructor(): this("", "", "","",0,0,0)
//}


class User{

    lateinit var userName: String
    lateinit var email:String
    lateinit var firstName: String
    lateinit var lastName: String
    var totalAmountGiven: Int = 0
    var myCoins: Int = 0
    var TotalDonations: Int = 0

    constructor(){ }

    constructor(userName: String, firstName: String, lastName: String, email: String){
        this.userName = userName
        this.firstName = firstName
        this.lastName = lastName
        this.email = email
    }
}
