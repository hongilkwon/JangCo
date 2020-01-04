package com.example.jangco

import com.opencsv.CSVReader
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class CvsReader {

    var data: List<Array<String>> = ArrayList()
    var map: HashMap<String,ArrayList<String>> = HashMap<String,ArrayList<String>>()

    fun readCsvFile(inputstream: InputStream):  HashMap<String,ArrayList<String>>{

        var inStreamReader = InputStreamReader(inputstream)
        var bufferedReader = BufferedReader(inStreamReader)
        var csvReader = CSVReader(bufferedReader)

        data = csvReader.readAll()
        for ( item in data ){
            if(map.containsKey(item.get(0))){
                //map.get(item.get(0))?.add(item.get(1)+"*"+item.get(2)+"*"+item.get(3)+"*"+item.get(4)+"*"+item.get(5)+"*"+item.get(6))
                map.get(item.get(0))?.add(item.get(1)+"*"+item.get(2)+"*"+item.get(3))

            }else{
                //map.put(item.get(0), arrayListOf(item.get(1)+"*"+item.get(2)+"*"+item.get(3)+"*"+item.get(4)+"*"+item.get(5)+"*"+item.get(6)))
                map.put(item.get(0), arrayListOf(item.get(1)+"*"+item.get(2)+"*"+item.get(3)))
            }
        }
        return map
    }

}