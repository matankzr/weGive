package com.example.wegive.models

import com.google.firebase.firestore.PropertyName

public class Person (
    @get:PropertyName("id") @set: PropertyName("id") var id: String = ""
)