package com.example.jangco

import android.annotation.SuppressLint
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.collections.HashMap

class DataBaseHelper(val id: String, val fireStore: FirebaseFirestore = FirebaseFirestore.getInstance()) {

    companion object
    {
        @SuppressLint("StaticFieldLeak")
        val fireStore = FirebaseFirestore.getInstance()

        @JvmStatic fun registerNewUserData(user: User, address :Address, school: School, grade: Grade, income: Income, sQualification: SQualification){
            fireStore.collection("User").document(user.id!!).set(user)
            var userInfoRef = fireStore.collection("User").document(user.id).collection("UserInfo")
            userInfoRef.document("address").set(address)
            userInfoRef.document("school").set(school)
            userInfoRef.document("grade").set(grade)
            userInfoRef.document("income").set(income)
            userInfoRef.document("squalification").set(sQualification)
        }
    }

    val userDocumnetRef = fireStore.collection("User").document(id)
    val userInfoCollectionRef =  userDocumnetRef.collection("UserInfo")

    val addressDocumentRef =userInfoCollectionRef.document("address")
    val schoolDocumentRef = userInfoCollectionRef.document("school")
    val gradeDocumentRef = userInfoCollectionRef.document("grade")
    val incomeDoucumentRef = userInfoCollectionRef.document("income")
    val sQualificationDoucumentRef  = userInfoCollectionRef.document("squalification")


    // 메인화면 집입시 사용자의 모든 문서와 기본정보(id,nickname...) 로딩
    // HashMap<String(data class 이름과 동일.), Object> 형태로
    fun getUserAllInfo(): HashMap<String, Any>{
        // 유저의 모든 정보를 담아 반환되는 맵 선언
        var allUserInfo = HashMap<String, Any>()
        // 유저프로필정보는 Ref가 다르기 때문에 따로 호출.
        var user = getCurrentUserProfile()
        allUserInfo.put("user",user)
        // 나머지 문서형태의 정보를 획득후 맵에 저장.
        var task = userInfoCollectionRef.get()
        Tasks.await(task)
        // 문서 이름으로 각각의 데이터 클래스로 분기
        for (document in task.result!!){
            when(document.id){
                "address" -> {
                    val address = document.toObject(Address::class.java)
                    allUserInfo.put("address", address)
                }
                "school" -> {
                    val school = document.toObject(School::class.java)
                    allUserInfo.put("school", school)
                }
                "grade" -> {
                    val grade = document.toObject(Grade::class.java)
                    allUserInfo.put( "grade", grade)
                }
                "income" -> {
                    val income = document.toObject(Income::class.java)
                    allUserInfo.put( "income", income)
                }
                "squalification" -> {
                    val sQualification = document.toObject(SQualification::class.java)
                    allUserInfo.put( "sQualification", sQualification)
                }
            }
        }
        Log.d("test","완료")
        return allUserInfo
    }

    //사용자 프로필 정보 User 객체로 반환
    fun getCurrentUserProfile(): User{
        var userProfile: User
        // 유저 프로필 정보를 받아옴.
        var task = userDocumnetRef.get()
        Tasks.await(task)
        // 결과 할당
        var result = task.result
        userProfile = result?.toObject(User::class.java)!!
        return userProfile
    }

    fun upDateUserProfile(userProfile: User){
        userDocumnetRef.set(userProfile)
    }

    fun upDateUserAddress(userAddress: Address){
        var task = addressDocumentRef.set(userAddress)
        Tasks.await(task)
    }

    fun upDateUserSchool(userSchool: School){
        var task =  schoolDocumentRef.set(userSchool)
        Tasks.await(task)
    }

    fun upDateUserGrade(userGrade: Grade){
        var task = gradeDocumentRef.set(userGrade)
        Tasks.await(task)
    }
    fun upDateUserIncome(userIncome: Income){
        var task = incomeDoucumentRef.set(userIncome)
        Tasks.await(task)
    }
    fun upDateUsersQualification(userSQualification: SQualification){
        var task = sQualificationDoucumentRef.set(userSQualification)
        Tasks.await(task)
    }

    fun updateBookmark(bookMarkMap: HashMap<String, Boolean>){
        userDocumnetRef.update("bookMarkMap", bookMarkMap)
    }

}

