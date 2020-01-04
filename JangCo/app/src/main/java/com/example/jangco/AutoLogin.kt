package com.example.jangco


import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager



class AutoLogin(val context: Context){

    val PREF_USER_ID = "userID"
    val PREF_USER_PW = "userPW"

    fun getSharedPreferences(): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }
    // 계정 정보 저장
    fun setAutoLogin( userId: String, userpw: String) {
        val editor = getSharedPreferences().edit()
        editor.putString(PREF_USER_ID, userId)
        editor.putString(PREF_USER_PW, userpw)
        editor.apply()
    }
    // 저장된 정보 가져오기
    fun getUserIDPW(): Array<String> {
        var id = getSharedPreferences().getString(PREF_USER_ID, "")
        var pw =getSharedPreferences().getString(PREF_USER_PW, "")
        var autoLoginInfo = arrayOf(id,pw)
        return autoLoginInfo
    }
    // 로그아웃
    fun clearUserName() {
        val editor = getSharedPreferences().edit()
        editor.clear()
        editor.commit()
    }
}