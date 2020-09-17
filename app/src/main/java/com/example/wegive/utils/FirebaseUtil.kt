package com.example.wegive.utils

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

private const val TAG="FirebaseUtil"

class FirebaseUtil {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestoreInstance: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val user= FirebaseAuth.getInstance().currentUser
    private val userId: String= user!!.uid
    private var userRef = firestoreInstance.collection("users").document(userId)

    private val userFavoritesRef: CollectionReference = userRef.collection("favorites")
    private val userDonationsRef:CollectionReference = userRef.collection("donations")


    private val donationsRef: DocumentReference = firestoreInstance.collection("donations").document("donationManager")
    private val charityOrganizationsRef: CollectionReference = firestoreInstance.collection("charityOrganization")
    private val storesRef: CollectionReference = firestoreInstance.collection("stores")

    fun getFirestoreInstance(): FirebaseFirestore{
        return firestoreInstance
    }
    fun getUser(): FirebaseUser?{
        return user
    }
    fun getUserRef(): DocumentReference{
        return userRef
    }
    fun getUserID():String{
        return userId
    }
    fun getUserFavoritesRef(): CollectionReference{
        return userFavoritesRef
    }
    fun getOrganizationsRef(): CollectionReference{
        return charityOrganizationsRef
    }
    fun getStoresRef(): CollectionReference{
        return storesRef
    }
    fun getUserDonationsRef(): CollectionReference{
        return userDonationsRef
    }
    fun getDonationsRef(): DocumentReference{
        return donationsRef
    }
}