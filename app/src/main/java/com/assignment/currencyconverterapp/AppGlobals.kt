package com.assignment.currencyconverterapp

import java.util.*

class AppGlobals {
    companion object {
        @JvmStatic

        val currencyList = ArrayList<String>()//Creating an empty arraylist
        val rateList = ArrayList<String>()//Creating an empty arraylist
        var dateTime:String?=null
    }
}