package com.example.wegive.models

import com.google.firebase.firestore.PropertyName

public class Store (
    @get:PropertyName("name") @set: PropertyName("name") var storeName: String = "",
    @get:PropertyName("website") @set: PropertyName("website")  var storeWebsite: String = "",
    @get:PropertyName("image_url") @set: PropertyName("image_url") var imageURL: String = "",
    var favorite: Boolean = false
)