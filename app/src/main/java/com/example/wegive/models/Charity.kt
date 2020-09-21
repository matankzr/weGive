package com.example.wegive.models

import com.google.firebase.firestore.PropertyName

class Charity(
    @get:PropertyName("name") @set: PropertyName("name") var charityName: String = "",
    @get:PropertyName("image_url") @set: PropertyName("image_url") var imageURL: String = "",
    @get:PropertyName("charity_type") @set: PropertyName("charity_type") var charityType: String = ""
)