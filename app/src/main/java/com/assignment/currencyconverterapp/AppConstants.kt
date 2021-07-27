package com.assignment.currencyconverterapp

import android.app.Application

class AppConstants: Application() {

    val tag="CCA_TAGG"
    val url="http://api.exchangeratesapi.io/v1/latest"
    val access_key="55f63b0f2efcbd906ce711156c3fcdaa"
    val INTERNET_CONNECTION_ERROR: String ="Please check internet connection and try again!"
    val amountError="Enter Amount"
    val fromCurrencyError="Select From Currency"
    val toCurrencyError="Select To Currency"
    //  http://api.exchangeratesapi.io/v1/latest?access_key=55f63b0f2efcbd906ce711156c3fcdaa&format=1
}