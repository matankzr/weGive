package com.example.wegive

import com.google.firebase.firestore.PropertyName
import java.util.*

data class Donation(
    @get:PropertyName("donation_amount") @set: PropertyName("donation_amount") var donationAmount: Float = 0f,
    var memo: String = "",
    @get:PropertyName("receiver_id") @set: PropertyName("receiver_id")  var receiverId: String = "",
    @get:PropertyName("date_donation") @set: PropertyName("date_donation")  var donationDate: Date?= null
)