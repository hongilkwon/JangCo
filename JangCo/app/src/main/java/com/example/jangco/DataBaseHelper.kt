package com.example.jangco

import android.annotation.SuppressLint
import com.google.firebase.firestore.FirebaseFirestore

class DataBaseHelper(val id: String, val fireStore: FirebaseFirestore = FirebaseFirestore.getInstance()) {

    companion object
    {
        @SuppressLint("StaticFieldLeak")
        val fireStore = FirebaseFirestore.getInstance()

        @JvmStatic fun registerNewUserData(user: User, address :Address, school: School, grade: Grade){
            fireStore.collection("User").document(user.id!!).set(user)
            var userInfoRef = fireStore.collection("User").document(user.id).collection("UserInfo")
            userInfoRef.document("address").set(address)
            userInfoRef.document("school").set(school)
            userInfoRef.document("grade").set(grade)
        }
    }

    val userDocumnetRef = fireStore.collection("User").document(id)
    val addressDocumentRef = userDocumnetRef.collection("UserInfo").document("address")
    val schoolDocumentRef = userDocumnetRef.collection("UserInfo").document("school")
    val gradeDocumentRef = userDocumnetRef.collection("UserInfo").document("grade")
    val incomeDoucumentRef = userDocumnetRef.collection("UserInfo").document("income")
    val sQualificationDoucumentRef  = userDocumnetRef.collection("UserInfo").document("sQualification")

}

