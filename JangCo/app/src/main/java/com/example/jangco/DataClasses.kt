package com.example.jangco

import android.net.Uri
import java.io.Serializable
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet

data class User(
    val id: String? = null,
    var nickName: String? = null,
    var proFileImageUri: String? = null,
/*
    var address: Address? = null,
    var school: School? = null,
    var grade: Grade? = null,
    var income: Income? = null,
    var sQualification: SQualification? = null,
*/
    var bookMarkMap: HashMap<String, Boolean>? = HashMap()
    ): Serializable


data class ScholarShip(

    val id: String? = null,
    val name: String? = null,
    val type: String? = null,  // 유형 : 장학금, 대출지원, 등.....
    val startDate: String? = null,
    val endDate: String? = null,

    val recruitment : Long? = null, // 선발인원
    val benefit: Long? = null, // 혜택 금액 (최대금액)
    val detailInfoURL: String? = null,

    //태그
    val tagList: List<Boolean>? = null, // 0: 성적기준, 1: 지역기준, 2: 특수기준, 3: 소득기준

    //지원대상 + 신청자격을 합친다. --> 신청자격으로
    var address: Address? = null,
    var school: School? = null,
    var grade: Grade? = null,
    var income: Income? = null,
    var sQualification: SQualification? = null
    //var likeMap: HashMap<String, Boolean>? = null
    ): Serializable


data class Address(
    var cityProvince: String? = null,
    var district: String?  = null,
    var residenceYear: Long? = null
): Serializable

data class School(
    var name: ArrayList<String>? = null,
    var major: ArrayList<String>? = null,
    var track: ArrayList<String>? = null,
    var collageDivision: ArrayList<String>? = null,
    var status: ArrayList<String>?= null,
    var completeSemester: ArrayList<Long>?  = null
//    var establishDivision: String? = null,
//    var areaDivision: String? = null,
//    var period: String? = null,
    ): Serializable


data class Grade(
    var totalAverageGrade: Double? = null,
    var totalPercentage: Long? = null,
    var lastAverageGrade: Double?= null,
    var lastPercentage: Long? = null
): Serializable


//  basicTier 는 기초생활수급자,
//  secondTier 는 차상위계층을 의미한다.
//  incomePercentage 소득분위
//  familyYearlyIncome 은 가족 연단위 수입이며 단위는 만이다.

data class Income(

//    var basicTier: Boolean? = null, // 기초생활 수급자.
//    var secondTier: Boolean? = null, // 차상위 계층.
    var incomePercentage: Long? = null, // 예상 소득분위
    var medianIncomePercentage: Long? = null // 예상 기준중위소득 분위
//    var familyNumber: Long? = null, //  가족 수
//    var familyMonthIncome: Long? = null // 소득인정액

): Serializable

data class SQualification(

    var official: Boolean? = false, // 공무원 본인 또는 자녀
    var nationalMerit: Boolean? = false, // 국가유공자 및 배우자
    var industrialAccident: Boolean? = false, // 산재근로자 본인, 배우자 또는 자녀
    var activeSoldier: Boolean? = false, // 현역군인 및 군무원의 자녀
    var longTermSoldier: Boolean? = false, // 장기복무군인 본인 또는 자녀
    var privateSchool: Boolean? = false, // 사립학교교직원 본인 또는 자녀
    var employmentInsurance: Boolean? = false, // 고용보험 피보험자
    var studentHead: Boolean? = false, // 학생가장(보육원출신, 조손가정포함)
    var singleParentFamily: Boolean? = false, // 한부모가정
    var disabled: Boolean? = false, // 장애인/장애우
    var multiculturalFamily: Boolean? = false, // 다문화가정
    var agricultureForestryFisheries: Boolean? = false, // 농림수산업인 본인 또는 자녀
    var northKoreaDefector: Boolean? = false, // 북한이탈주민자녀
    var highwayAccident: Boolean? = false, // 고속도로 사고 관련자
    var smallBusiness: Boolean? = false // 중소기업 근로자의 자녀

): Serializable

// NoticeBoard
data class Notice(
    val date: Date? = null,
    val rate: Long? = null, // 공지사항 등급.
    val title: String? = null,
    val contents: String? = null
)

// EventBoard
data class Event(
    val title: String? = null,
    val imageUrl: String? = null,
    val startDate: Date? = null,
    val endDate: Date? = null
)