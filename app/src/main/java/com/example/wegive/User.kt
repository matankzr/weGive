package com.example.wegive

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
