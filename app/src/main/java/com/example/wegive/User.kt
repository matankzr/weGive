package com.example.wegive

//
data class User(var userName: String = "", var email: String= "", var firstName: String= "", var lastName: String= "",
                var totalAmountGiven: Int=0, var myCoins: Int=0, var totalNumberOfDonations: Int=0){
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
