package com.example.wegive

class User{

    //lateinit var name:String
    lateinit var email:String
    //var coins: Int = 0

    lateinit var firstName: String
    lateinit var lastName: String
    var totalAmountGiven: Int = 0
    var myCoins: Int = 0
    var TotalDonations: Int = 0
    lateinit var userName: String

// Total amount given
    //Number of donations made
    //Number of causes donated to



    //Default constructor required for calls to
    //DataSnapshot.getValue(User.class)
    constructor(){

    }

    constructor(userName: String, firstName: String, lastName: String, email: String){
        this.userName = userName
        this.firstName = firstName
        this.lastName = lastName
        this.email = email
    }
}
