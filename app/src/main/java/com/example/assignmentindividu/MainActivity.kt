package com.example.assignmentindividu

import Profile
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.example.assignmentindividu.database.DatabaseHelper
import com.example.assignmentindividu.databinding.ActivityMainBinding
import com.example.assignmentindividu.`object`.Data
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONObject
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.assignmentindividu.items.Airline
import com.example.assignmentindividu.items.Flight
import com.example.assignmentindividu.utils.FirstChecker
import org.json.JSONArray
import org.json.JSONException
import java.time.LocalDate


class MainActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var binding : ActivityMainBinding
    private lateinit var passLayout: TextInputLayout
    private lateinit var passET: EditText
    private lateinit var usernameLayout: TextInputLayout
    private lateinit var usernameET: EditText
    private lateinit var registerText: TextView
    private lateinit var loginButton: Button
    private lateinit var requestQueue: RequestQueue

    private val API_KEY = "64106f338bff416bdc4b48bd9031e0a09a26ee10f4b711c7270e3980a1544e05"
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        passLayout = binding.PasswordTL
        passET = binding.passwordText
        usernameLayout = binding.UsernameTL
        usernameET = binding.usernameText
        loginButton = binding.loginBtn
        registerText = binding.registerTV

        databaseHelper = DatabaseHelper(this)
        //initialize

        databaseHelper.deleteFlights()
        if(FirstChecker.isFirstRun(this)){
            val dateNow = LocalDate.now().plusDays(1).toString()
            val returnDate = LocalDate.now().plusDays(7).toString()
            val departureId = "CGK"
            val arrivalId = "BTJ%2C+KNO%2C+PDG%2C+PKU%2C+BTH%2C+CGK%2C+HLP%2C+KJT%2C+YIA%2C+SUB"
            val arrivalId2 = "DPS%2C+LOP%2C+LBJ%2C+BPN%2C+UPG%2C+MDC%2C+DJJ"
            val url2 = "https://serpapi.com/search.json?engine=google_flights&departure_id=$departureId&arrival_id=$arrivalId&gl=id&hl=en&currency=IDR&outbound_date=$dateNow&return_date=$returnDate&api_key=$API_KEY"
            val url3 = "https://serpapi.com/search.json?engine=google_flights&departure_id=$arrivalId&arrival_id=$departureId&gl=id&hl=en&currency=IDR&outbound_date=$dateNow&return_date=$returnDate&api_key=$API_KEY"
            val url4 = "https://serpapi.com/search.json?engine=google_flights&departure_id=$departureId&arrival_id=$arrivalId2&gl=id&hl=en&currency=IDR&outbound_date=$dateNow&return_date=$returnDate&api_key=$API_KEY"
            val url5 = "https://serpapi.com/search.json?engine=google_flights&departure_id=$arrivalId2&arrival_id=$departureId&gl=id&hl=en&currency=IDR&outbound_date=$dateNow&return_date=$returnDate&api_key=$API_KEY"
            requestData(url2)
            requestData(url3)
            requestData(url4)
            requestData(url5)
        }


        if(Data.profileList.isNullOrEmpty()){
            var listToAddData = arrayListOf(Profile(1, "admin", "admin", "admin@puff.com", "081213141516", "male"),
                Profile(2, "hofta", "hofta", "hofta@puff.com", "081213141516", "male"))
            Data.profileList= listToAddData
        }
//


        passET.doOnTextChanged{ text, _, _, _ ->
            if(text!!.isEmpty()){
                passLayout.helperText = "password must be filled"
            }
            else{
                passLayout.helperText = null
            }
        }

        usernameET.doOnTextChanged{ text, _, _, _ ->
            if(text!!.isEmpty()){
                usernameLayout.helperText = "username must be filled"
            }
            else{
                usernameLayout.helperText = null
            }
        }

        //handle when pressing enter
        usernameET.setOnKeyListener(View.OnKeyListener{ _, keyCode, event ->
            if(keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN){
                loginButton.performClick()
                return@OnKeyListener true
            }
            false
        })

        passET.setOnKeyListener(View.OnKeyListener{v, keyCode, event ->
            if(keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN){
                loginButton.performClick()
                return@OnKeyListener true
            }
            false
        })

        //handle when clicking Login Button
        loginButton.setOnClickListener{

            val loginProfile2 = databaseHelper.getProfile(usernameET.text.toString(), passET.text.toString())
            if(loginProfile2.username == null){
                val toast = Toast.makeText(this, "There is no username or password", Toast.LENGTH_SHORT)
                toast.show()
            }
            else{
                val intent = Intent(this,OTPPage::class.java)
                Data.myProfile = loginProfile2
                startActivity(intent)
            }
        }

        registerText.setOnClickListener{
            registerText.setTextColor(Color.parseColor("#0000EE"))
            val intent = Intent(this,RegisterPage::class.java)
            startActivity(intent)
        }


}
    private fun requestData(url: String){
        requestQueue = Volley.newRequestQueue(this)

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,

            Response.Listener { response ->
                try {
                    parseResponseAPI(response)
                }catch (e: JSONException){
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                Log.e("Volley Error", error.toString())
            }
        )
        requestQueue.add(request)
    }

    private fun parseResponseAPI(jsonObject: JSONObject){
        try {
            val allFlights = jsonObject.getJSONArray("best_flights")
            val otherFlights = jsonObject.getJSONArray("other_flights")
            getAllJSONData(allFlights)
            getAllJSONData(otherFlights)

        }catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun getAllJSONData(jsonArray: JSONArray){
        databaseHelper = DatabaseHelper(this)
        val defaultValue = "unavailable"
        var airplane : String
        var airlineName : String
        var airlineLogo : String
        var flightNumber : String
        var duration : Int
        var price : Int
        var departureAirportName : String
        var departureTime : String
        var departureId : String
        var arrivalAirportName : String
        var arrivalTime : String
        var arrivalId : String

        for (i in 0 until jsonArray.length()){
            val flightObject = jsonArray.getJSONObject(i)
            val flights = flightObject.getJSONArray("flights")
            price = flightObject.optString("price","0").toInt()
            for (j in 0 until flights.length()){

                val flightDetailObject = flights.getJSONObject(j)
                val departureAirport = flightDetailObject.getJSONObject("departure_airport")
                val arrivalAirport = flightDetailObject.getJSONObject("arrival_airport")

                airplane = flightDetailObject.optString("airplane",defaultValue)
                airlineName = flightDetailObject.optString("airline",defaultValue)
                airlineLogo = flightDetailObject.optString("airline_logo",defaultValue)
                flightNumber = flightDetailObject.optString("flight_number",defaultValue)
                duration = flightDetailObject.optString("duration","0").toInt()

                departureAirportName = departureAirport.optString("name",defaultValue)
                departureTime = departureAirport.optString("time",defaultValue)
                departureId = departureAirport.optString("id",defaultValue)

                arrivalAirportName = arrivalAirport.optString("name",defaultValue)
                arrivalTime = arrivalAirport.optString("time",defaultValue)
                arrivalId = arrivalAirport.optString("id",defaultValue)

                var airlineId = databaseHelper.checkAirline(airlineName)
                if(airlineId == 0){
                    databaseHelper.insertAirline(Airline(airlineLogo,1,airlineName,airplane))
                }
                airlineId = databaseHelper.checkAirline(airlineName)

                if(!databaseHelper.checkFlight(flightNumber)){
                    databaseHelper.insertFlight(Flight(1,price,airlineId,airlineLogo,airlineName,airplane,flightNumber,duration,departureAirportName,departureTime,departureId,arrivalAirportName,arrivalTime,arrivalId))
                }
            }
        }
    }





}