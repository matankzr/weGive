package com.example.wegive.models

import com.google.firebase.firestore.PropertyName
import java.util.*

public class Charity (
    @get:PropertyName("name") @set: PropertyName("name") var charityName: String = "",
    @get:PropertyName("website") @set: PropertyName("website")  var organizationWebsite: String = "",
    @get:PropertyName("image_url") @set: PropertyName("image_url") var imageURL: String = "",
    var favorite: Boolean = false,
    @get:PropertyName("charity_type") @set: PropertyName("charity_type") var charityType: String = ""
)