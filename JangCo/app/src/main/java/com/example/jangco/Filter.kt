package com.example.jangco

class Filter(val userAllinfo: HashMap<String, Any>, val scholarShipList: ArrayList<ScholarShip>){

    fun filter() {
        if(!scholarShipList.isEmpty())
            compareSchoolInfo()
        if(!scholarShipList.isEmpty())
            compareGradeInfo()
        if(!scholarShipList.isEmpty())
            compareAddressInfo()
        if(!scholarShipList.isEmpty())
            compareIncomeInfo()
        if(!scholarShipList.isEmpty())
            compareSQualificationInfo()
    }

    fun compareSchoolInfo(){

        val userSchoolInfo = userAllinfo.get("school") as School

        val iterator = scholarShipList.iterator()
        while(iterator.hasNext()) {
            val scholarShip = iterator.next()
            if(scholarShip.school?.name?.size != 0 && !scholarShip.school?.name!!.contains(userSchoolInfo.name!![0])) {
                iterator.remove()
                continue
            }
            if(scholarShip.school?.major?.size != 0 && !scholarShip.school?.major!!.contains(userSchoolInfo.major!![0])) {
                iterator.remove()
                continue
            }
            if(scholarShip.school?.track?.size != 0 && !scholarShip.school?.track!!.contains(userSchoolInfo.track!![0])) {
                iterator.remove()
                continue
            }
            if(scholarShip.school?.collageDivision?.size != 0 && !scholarShip.school?.collageDivision!!.contains(userSchoolInfo.collageDivision!![0])) {
                iterator.remove()
                continue
            }
            if(scholarShip.school?.status?.size != 0 && !scholarShip.school?.status!!.contains(userSchoolInfo.status!![0])) {
                iterator.remove()
                continue
            }
        }
    }

    fun compareGradeInfo(){
        val userGradeInfo = userAllinfo.get("grade") as Grade

        val iterator = scholarShipList.iterator()
        while(iterator.hasNext()) {
            val scholarShip = iterator.next()
            if(scholarShip.grade?.totalAverageGrade != 0.0 && scholarShip.grade?.totalAverageGrade!! > userGradeInfo.totalAverageGrade!!) {
                iterator.remove()
                continue
            }
            if(scholarShip.grade?.totalPercentage != 0L && scholarShip.grade?.totalPercentage!! > userGradeInfo.totalPercentage!!) {
                iterator.remove()
                continue
            }
            if(scholarShip.grade?.lastAverageGrade != 0.0 && scholarShip.grade?.lastAverageGrade!! > userGradeInfo.lastAverageGrade!!) {
                iterator.remove()
                continue
            }
            if(scholarShip.grade?.lastPercentage != 0L && scholarShip.grade?.lastPercentage!! > userGradeInfo.lastPercentage!!) {
                iterator.remove()
                continue
            }
        }

    }
    fun compareAddressInfo(){
        val userAddressInfo = userAllinfo.get("address") as Address

        val iterator = scholarShipList.iterator()
        while(iterator.hasNext()) {
            val scholarShip = iterator.next()

            if(scholarShip.address?.cityProvince != null && scholarShip.address?.cityProvince != userAddressInfo.cityProvince ){
                iterator.remove()
                continue
            }
            if(scholarShip.address?.district != null && scholarShip.address?.district != userAddressInfo.district ){
                iterator.remove()
                continue
            }

        }

    }

    fun compareIncomeInfo(){
        val userIncomeInfo = userAllinfo.get("income") as Income

        val iterator = scholarShipList.iterator()
        while(iterator.hasNext()) {
            val scholarShip = iterator.next()
            if(scholarShip.income?.incomePercentage != 0L && scholarShip.income?.incomePercentage!! < userIncomeInfo.incomePercentage!!) {
                iterator.remove()
                continue
            }
            if(scholarShip.income?.medianIncomePercentage != 0L && scholarShip.income?.medianIncomePercentage!! < userIncomeInfo.medianIncomePercentage!!) {
                iterator.remove()
                continue
            }
        }

    }

    fun compareSQualificationInfo(){
        val userSQualificationInfo = userAllinfo.get("sQualification") as SQualification

        val iterator = scholarShipList.iterator()
        while(iterator.hasNext()) {
            val scholarShip = iterator.next()
            if(scholarShip.sQualification?.official == true && userSQualificationInfo.official == false) {
                iterator.remove()
                continue
            }
            if(scholarShip.sQualification?.nationalMerit == true && userSQualificationInfo.nationalMerit == false) {
                iterator.remove()
                continue
            }
            if(scholarShip.sQualification?.industrialAccident == true && userSQualificationInfo.industrialAccident == false) {
                iterator.remove()
                continue
            }
            if(scholarShip.sQualification?.activeSoldier == true && userSQualificationInfo.activeSoldier == false) {
                iterator.remove()
                continue
            }
            if(scholarShip.sQualification?.longTermSoldier == true && userSQualificationInfo.longTermSoldier == false) {
                iterator.remove()
                continue
            }
            if(scholarShip.sQualification?.privateSchool == true && userSQualificationInfo.privateSchool == false) {
                iterator.remove()
                continue
            }
            if(scholarShip.sQualification?.employmentInsurance == true && userSQualificationInfo.employmentInsurance == false) {
                iterator.remove()
                continue
            }
            if(scholarShip.sQualification?.studentHead == true && userSQualificationInfo.studentHead == false) {
                iterator.remove()
                continue
            }
            if(scholarShip.sQualification?.singleParentFamily == true && userSQualificationInfo.singleParentFamily == false) {
                iterator.remove()
                continue
            }
            if(scholarShip.sQualification?.disabled == true && userSQualificationInfo.disabled == false) {
                iterator.remove()
                continue
            }
            if(scholarShip.sQualification?.multiculturalFamily == true && userSQualificationInfo.multiculturalFamily == false) {
                iterator.remove()
                continue
            }
            if(scholarShip.sQualification?.agricultureForestryFisheries == true && userSQualificationInfo.agricultureForestryFisheries == false) {
                iterator.remove()
                continue
            }
            if(scholarShip.sQualification?.northKoreaDefector == true && userSQualificationInfo.northKoreaDefector == false) {
                iterator.remove()
                continue
            }
            if(scholarShip.sQualification?.highwayAccident == true && userSQualificationInfo.highwayAccident == false) {
                iterator.remove()
                continue
            }
            if(scholarShip.sQualification?.smallBusiness == true && userSQualificationInfo.smallBusiness == false) {
                iterator.remove()
                continue
            }
        }
    }





}