package com.assignment.currencyconverterapp

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class SplashScreenActivity : AppCompatActivity() {
    var AppConstants = com.assignment.currencyconverterapp.AppConstants()
    private var ok_dialogbuilder: AlertDialog.Builder? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        ok_dialogbuilder = AlertDialog.Builder(this)

        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


        Handler().postDelayed({
            if (AppFunctions.checkIfNetworkExist(this)) {
                WS_getexchangerates()
            } else {
                showAlert(AppConstants.INTERNET_CONNECTION_ERROR)
            }
        }, 3000) // 3000 is the delayed time in milliseconds.
    }

    private fun showAlert(message: String) {
        // set message of alert dialog
        ok_dialogbuilder?.setMessage(message)
            ?.setCancelable(false)
            ?.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                dialog.dismiss()
            })
        val alert = ok_dialogbuilder?.create()
        alert?.show()

    }


    fun WS_getexchangerates() {
        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url = AppConstants.url + "?access_key=" + AppConstants.access_key
        Log.i(AppConstants.tag, "WS_getexchangerates : " + url)

        // Request a string response from the provided URL.
        val stringReq = StringRequest(
                Request.Method.GET, url,
                { response ->
                    try {
                        var strResp = response.toString()
                        if (AppFunctions.isJSONValid(strResp)) {
                            val response_from_server = JSONObject(strResp)

                            if (response_from_server.has("success")) {
                                val successValue = response_from_server.getBoolean("success")
                                if (successValue) {
                                    AppGlobals.currencyList.clear()
                                    AppGlobals.rateList.clear()
                                    AppGlobals.currencyList.add("--Please Select--")
                                    AppGlobals.rateList.add("0")
                                   // val date = response_from_server.getString("date")

                                    val unix_seconds: Long = response_from_server.getLong("timestamp")
                                   // format of the date
                                    val jdf = SimpleDateFormat("dd-MMM-yyyy HH:mm:ss z")
                                    jdf.timeZone = TimeZone.getTimeZone("UTC")
                                    AppGlobals.dateTime= jdf.format(Date(unix_seconds * 1000L))


                                   Log.d(AppConstants.tag, "WS_getexchangerates datetime: " + AppGlobals.dateTime)

                                    val rates = response_from_server.getString("rates")
                                    val rates_obj = JSONObject(rates)
                                    val keys: Iterator<*> = rates_obj.keys()

                                    while (keys.hasNext()) {
                                        // loop to get the dynamic key
                                        val currency = keys.next() as String
                                        val rate: String = rates_obj.getString(currency)
                                        AppGlobals.currencyList.add(currency)
                                        AppGlobals.rateList.add(rate)

                                    }

                                    val intent = Intent(this, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }else{//success false :Error
                                    handleError(response_from_server)

                                }

                            }else{
                                handleError(response_from_server)
                            }

                        } else {
                            showAlert(strResp)
                        }

                    } catch (e: JSONException) {
                       // Log.d(AppConstants.tag, "WS_getexchangerates : JSONException:" + e.toString())
                        e.printStackTrace()
                    }


                },
                {
                    //Volley Error
                    showAlert("Volley Error")
                    //Log.d(AppConstants.tag, "that didn't work")
                })
        queue.add(stringReq)
    }

    private fun handleError(responseFromServer: JSONObject) {
       try {
           val error = responseFromServer.getString("error")
           val error_obj = JSONObject(error)
           val info = error_obj.getString("info")
           showAlert(info)
       }catch (e: JSONException) {
           e.printStackTrace()
       }
    }


}