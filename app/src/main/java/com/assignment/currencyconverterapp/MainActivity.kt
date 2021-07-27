package com.assignment.currencyconverterapp

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged


class MainActivity : AppCompatActivity(),View.OnClickListener{
    var currency_adapter: ArrayAdapter<String>? = null
    var fromSpinner:Spinner?=null
    var toSpinner:Spinner?=null
    var amountEditText:EditText?=null
    var convertButton:Button?=null
    var rateTextView:TextView?=null
    var convertedAmountTextview:TextView?=null
    var datetimeTextView:TextView?=null
    var dialogbuilder: AlertDialog.Builder ? = null
    var AppConstants =AppConstants()

    var fromCurrency:String?=null
    var toCurrency:String?=null

    var amount:Double?=0.0
    var toRate:Double?=0.0
    var fromRate:Double?=0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        dialogbuilder = AlertDialog.Builder(this)
        //accessing our layout controls
        fromSpinner = findViewById<Spinner>(R.id.from_currency_spinner) as Spinner
        toSpinner = findViewById<Spinner>(R.id.to_currency_spinner) as Spinner
        amountEditText = findViewById<EditText>(R.id.amount_edittxt) as EditText
        convertButton=findViewById<Button>(R.id.convert_btn) as Button
        rateTextView=findViewById<TextView>(R.id.rate_txtvw) as TextView
        convertedAmountTextview=findViewById<TextView>(R.id.convertedamount_txtvw) as TextView
        datetimeTextView=findViewById<TextView>(R.id.datetime_txtvw) as TextView
        convertButton?.setOnClickListener (this)



        if (AppGlobals.currencyList.size > 0) {

            if (AppGlobals.dateTime!=null){
                datetimeTextView?.setText(AppGlobals.dateTime)
            }
            currency_adapter = ArrayAdapter(
                this,
                R.layout.spinner_item,
                AppGlobals.currencyList
            )
            fromSpinner?.adapter = currency_adapter
            toSpinner?.adapter = currency_adapter
            updateRate()
        } else {
            startActivity(Intent(this, SplashScreenActivity::class.java))
            finish()
        }



        amountEditText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {


            }
            override fun beforeTextChanged(
                    s: CharSequence, start: Int,
                    count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                    s: CharSequence, start: Int,
                    before: Int, count: Int
            ) {
                convertedAmountTextview?.visibility=View.GONE
            }
        })

        fromSpinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,view: View, position: Int, id: Long) {

                fromCurrency=AppGlobals.currencyList.get(position)
                fromRate=AppGlobals.rateList.get(position).toDouble()
                convertedAmountTextview?.visibility=View.GONE
                updateRate()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
                fromRate=0.0
            }
        }

        toSpinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,view: View, position: Int, id: Long) {

                toCurrency=AppGlobals.currencyList.get(position)
                toRate=AppGlobals.rateList.get(position).toDouble()
                convertedAmountTextview?.visibility=View.GONE
                updateRate()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
                toRate=0.0
            }
        }


    }

    override fun onClick(view: View?) {
        if(view?.equals(convertButton)!!){
            if(amountEditText?.text.toString().length>0){
                amount=amountEditText?.text.toString().toDouble()
                if(fromRate!=0.0){
                    if(toRate!=0.0){

                        updateRate()
                        calculateCurrencyRate()
                    }else{
                        showAlert(AppConstants.toCurrencyError)
                    }
                }else{
                    showAlert(AppConstants.fromCurrencyError)
                }

            }else{
                showAlert(AppConstants.amountError)
            }

        }
    }

    private fun updateRate() {
        var txt="Rate : "+fromRate.toString()+" * "+toRate.toString()
        rateTextView?.setText(txt)
    }

    private fun calculateCurrencyRate() {
        val convertedAmount: Double = (toRate!!/fromRate!!)* amount!!
        var txt="Converted Amount : "+String.format("%.4f", convertedAmount)
        convertedAmountTextview?.visibility=View.VISIBLE
        convertedAmountTextview?.setText(txt)
       }

    private fun showAlert(message:String){
        // set message of alert dialog
        dialogbuilder?.setMessage(message)
                ?.setCancelable(false)
                ?.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                    dialog.dismiss()
                })
        val alert = dialogbuilder?.create()
        alert?.show()

    }

}