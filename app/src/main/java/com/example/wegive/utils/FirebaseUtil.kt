package com.example.wegive.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


private const val TAG="FirebaseUtil"

class FirebaseUtil {
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var firestoreInstance: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var user= FirebaseAuth.getInstance().currentUser
    private var userId: String= user!!.uid
    private var usersRef = firestoreInstance.collection("users")
    private var userRef = firestoreInstance.collection("users").document(userId)

    private val userFavoritesRef: CollectionReference = userRef.collection("favorites")
    private val userDonationsRef:CollectionReference = userRef.collection("donations")


    private val donationsRef: DocumentReference = firestoreInstance.collection("donations").document("donationManager")
    private val charityOrganizationsRef: CollectionReference = firestoreInstance.collection("charityOrganization")
    private val charityPersonsRef: CollectionReference = firestoreInstance.collection("charityPersons")

    private val storesRef: CollectionReference = firestoreInstance.collection("stores")

    private val mStorageRef: StorageReference = FirebaseStorage.getInstance().getReference()


    fun getAuth(): FirebaseAuth {
        return firebaseAuth
    }
    fun getFirestoreInstance(): FirebaseFirestore{
        return firestoreInstance
    }
    fun getUser(): FirebaseUser?{
        return user
    }
    fun getUserRef(): DocumentReference{
        return userRef
    }
    fun getUsersRef(): CollectionReference{
        return usersRef
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
    fun getStorageRef(): StorageReference{
        return mStorageRef
    }
    fun getCharityPersonsRef(): CollectionReference{
        return charityPersonsRef
    }
}