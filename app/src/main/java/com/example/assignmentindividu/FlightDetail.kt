package com.example.assignmentindividu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.lifecycle.lifecycleScope
import com.example.assignmentindividu.database.DatabaseHelper
import com.example.assignmentindividu.items.Flight
import com.example.assignmentindividu.database.loadImage
import com.example.assignmentindividu.databinding.ActivityFlightDetailBinding
import com.example.assignmentindividu.items.Transaction
import com.example.assignmentindividu.`object`.Data
import kotlinx.coroutines.launch


class FlightDetail : AppCompatActivity() {


    private lateinit var flightDetailBinder: ActivityFlightDetailBinding
    private lateinit var LogoImageView: ImageView
    private lateinit var AirlineNameTV: TextView
    private lateinit var AirlineCodeTV: TextView
    private lateinit var RealAirplaneTypeTV: TextView
    private lateinit var FlightDestination: TextView
    private lateinit var FlightDurationTV: TextView
    private lateinit var DepartureAiportTV: TextView
    private lateinit var TimeDepartureTV: TextView
    private lateinit var ArrivalAirportTV: TextView
    private lateinit var TimeArrivalTV: TextView
    private lateinit var addFlightButton: Button
    private lateinit var backButton: ImageButton

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        flightDetailBinder = ActivityFlightDetailBinding.inflate(layoutInflater)
        setContentView(flightDetailBinder.root)

        LogoImageView = flightDetailBinder.LogoImageView
        AirlineNameTV = flightDetailBinder.AirlineName
        AirlineCodeTV = flightDetailBinder.AirlineCode
        RealAirplaneTypeTV = flightDetailBinder.RealAirplane
        FlightDestination = flightDetailBinder.Destination
        FlightDurationTV = flightDetailBinder.FlightDuration
        DepartureAiportTV = flightDetailBinder.DepartureAirport
        TimeDepartureTV = flightDetailBinder.TimeDeparture
        ArrivalAirportTV = flightDetailBinder.ArrivalAirport
        TimeArrivalTV = flightDetailBinder.TimeArrival
        addFlightButton = flightDetailBinder.AddFlightButton
        backButton = flightDetailBinder.FlightDetailBackButton

        val flightData = intent.getParcelableExtra("flight", Flight::class.java)

        val airlineLogo = flightData?.airlineImage
        val realAirplane = flightData?.airplane
        val airlineName = flightData?.airlineName
        val airlineCode = flightData?.flightNumber
        val airplaneType = flightData?.airplane
        val flightDuration = flightData?.duration
        val departureAirport = flightData?.departureAirport
        val timeDeparture = flightData?.departureTime
        val arrivalAirport = flightData?.arrivalAirport
        val timeArrival = flightData?.arrivalTime

        RealAirplaneTypeTV.text = realAirplane
        AirlineNameTV.text = airlineName
        AirlineCodeTV.text = airlineCode
        RealAirplaneTypeTV.text = airplaneType
        FlightDurationTV.text = ("Duration: ${flightDuration.toString()} minutes")
        DepartureAiportTV.text = departureAirport
        TimeDepartureTV.text = timeDeparture
        ArrivalAirportTV.text = arrivalAirport
        TimeArrivalTV.text = timeArrival


        if (airlineLogo != null) {
            lifecycleScope.launch{
                loadImage(LogoImageView,airlineLogo)
            }
//            flightImageView.setImageResource(flightPath)
        }


        addFlightButton.setOnClickListener {
            val userID = Data.myProfile?.userID
            val flightID = flightData?.flightID
            val transactionID = Data.flightList?.size
            val newTransaction = Transaction(transactionID,userID,flightID)
            databaseHelper = DatabaseHelper(this)
            if(databaseHelper.checkTransaction(flightID,userID)){
                Toast.makeText(this, "You already booked this flight", Toast.LENGTH_SHORT).show()
            }
            else{
                Data.transactionList?.add(newTransaction)
                databaseHelper.insertTransaction(newTransaction)
                val intent = Intent(this,HomePage::class.java)
                startActivity(intent)
            }
        }

        backButton.setOnClickListener{
            val intent = Intent(this,HomePage::class.java)
            startActivity(intent)
        }
    }
}