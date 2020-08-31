package com.example.wegive.models

import com.google.firebase.firestore.PropertyName

public class Favorite (
    @get:PropertyName("cause") @set: PropertyName("cause") var cause: String = "",
    @get:PropertyName("totalGivenToCause") @set: PropertyName("totalGivenToCause") var totalGivenToCause: Float = 0f,
    @get:PropertyName("numTimesDonatedTo") @set: PropertyName("numTimesDonatedTo") var numTimesDonatedTo: Int = 0
)