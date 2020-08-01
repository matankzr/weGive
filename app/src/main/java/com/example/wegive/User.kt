package com.example.wegive

class User{

    lateinit var name:String
    lateinit var email:String
    var coins: Int = 0
//    lateinit var userName: String
//    lateinit var firstName: String
//    lateinit var lastName: String
// Total amount given
    //Number of donations made
    //Number of causes donated to



    //Default constructor required for calls to
    //DataSnapshot.getValue(User.class)
    constructor(){

    }

    constructor(name:String,email:String){
        this.name=name
        this.email=email
        this.coins = coins
//        this.userName = "defaultUsername"
//        this.firstName = "FirstName"


    }
}
