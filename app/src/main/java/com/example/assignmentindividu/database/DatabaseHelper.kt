package com.example.assignmentindividu.database

import Profile
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.assignmentindividu.items.Airline
import com.example.assignmentindividu.items.Flight
import com.example.assignmentindividu.items.Transaction

class DatabaseHelper(context: Context):SQLiteOpenHelper(context,"cgk_flight.db",null,1){
    override fun onCreate(db: SQLiteDatabase?) {
        val queryCreateAirlines = "CREATE TABLE IF NOT EXISTS \"Airlines\" (" +
                "\"AirlineID\"INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "\"Name\"TEXT," +
                "\"Image\"TEXT," +
                "\"Airplane\"TEXT" +
                ")"

        val queryCreateFlights = "CREATE TABLE IF NOT EXISTS\"Flights\" (\n" +
                "\t\"FlightID\"\tINTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "\t\"Price\"\tINTEGER NOT NULL,\n" +
                "\t\"AirlineID\"\tINTEGER NOT NULL,\n" +
                "\t\"FlightNumber\"\tTEXT NOT NULL,\n" +
                "\t\"Duration\"\tINT NOT NULL,\n" +
                "\t\"DepartureAirport\"\tTEXT NOT NULL,\n" +
                "\t\"DepartureTime\"\tTEXT NOT NULL,\n" +
                "\t\"DepartureID\"\tTEXT NOT NULL,\n" +
                "\t\"ArrivalAirport\"\tTEXT NOT NULL,\n" +
                "\t\"ArrivalTime\"\tTEXT NOT NULL,\n" +
                "\t\"ArrivalID\"\tTEXT NOT NULL,\n" +
                "\tFOREIGN KEY(\"AirlineID\") REFERENCES \"Airlines\"(\"AirlineID\") ON UPDATE CASCADE ON DELETE CASCADE\n" +
                ");"

        val queryCreateUser = "CREATE TABLE IF NOT EXISTS \"Users\" (\n" +
                "\t\"UserID\"\tINTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "\t\"Username\"\tTEXT NOT NULL,\n" +
                "\t\"Email\"\tTEXT NOT NULL,\n" +
                "\t\"Password\"\tTEXT NOT NULL,\n" +
                "\t\"TelephoneNumber\"\tTEXT NOT NULL,\n" +
                "\t\"Gender\"\tTEXT NOT NULL\n" +
                ");"

        val queryCreateTransaction = "CREATE TABLE IF NOT EXISTS \"Transactions\" (\n" +
                "\t\"TransactionID\"\tINTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "\t\"UserID\"\tINTEGER NOT NULL,\n" +
                "\t\"FlightID\"\tINTEGER NOT NULL,\n" +
                "\tFOREIGN KEY(\"UserID\") REFERENCES \"Users\"(\"UserID\") ON UPDATE CASCADE ON DELETE CASCADE,\n" +
                "\tFOREIGN KEY(\"FlightID\") REFERENCES \"Flights\"(\"FlightID\") ON UPDATE CASCADE ON DELETE CASCADE\n" +
                ");"

        db?.execSQL(queryCreateAirlines)
        db?.execSQL(queryCreateFlights)
        db?.execSQL(queryCreateUser)
        db?.execSQL(queryCreateTransaction)
    }

    fun insertAirline(airline: Airline){
        val db = writableDatabase
        val values = ContentValues().apply {
            put("Name", airline.name)
            put("Image", airline.path)
            put("Airplane",airline.airplane)
        }
        db.insert("Airlines",null,values)
        db.close()
    }

    fun getAirlines(): List<Airline>{
        val airlines = ArrayList<Airline>()
        val db = readableDatabase
        val query = "SELECT * FROM Airlines"
        val cursor = db.rawQuery(query,null)
        cursor.moveToFirst()

        if(cursor.count > 0){
            do{
                val airline = Airline()
                airline.id = cursor.getInt(cursor.getColumnIndexOrThrow("AirlineID"))
                airline.name = cursor.getString(cursor.getColumnIndexOrThrow("Name"))
                airline.path = cursor.getString(cursor.getColumnIndexOrThrow("Image"))
                airline.airplane = cursor.getString(cursor.getColumnIndexOrThrow("Airplane"))
                airlines.add(airline)
            }while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return (airlines)
    }

    fun checkAirline(airlineName: String): Int?{
        val airline = Airline()
        val db = readableDatabase
        val query = "SELECT * FROM Airlines WHERE Name = '$airlineName'"
        val cursor = db.rawQuery(query,null)
        cursor.moveToFirst()

        if(cursor.count > 0){
            airline.id = cursor.getInt(cursor.getColumnIndexOrThrow("AirlineID"))
            airline.name = cursor.getString(cursor.getColumnIndexOrThrow("Name"))
        }
        cursor.close()
        db.close()
        if(airline.name!=null){
            return airline.id
        }
        return 0
    }



    fun insertProfile(profile: Profile){
        val db = writableDatabase
        val values = ContentValues().apply {
//            put("UserID",profile.userID)
            put("Username", profile.username)
            put("Email", profile.email)
            put("Password", profile.password)
            put("TelephoneNumber", profile.telephoneNumber)
            put("Gender", profile.gender)
        }
        db.insert("Users",null,values)
        db.close()
    }

    fun getProfile(username: String, password: String): Profile{
        val profile = Profile()
        val db = readableDatabase
        val query = "SELECT * FROM Users WHERE Username = '$username'  AND  Password = '$password'"
        val cursor = db.rawQuery(query,null)
        cursor.moveToFirst()

        if(cursor.count > 0){
            profile.userID = cursor.getInt(cursor.getColumnIndexOrThrow("UserID"))
            profile.username = cursor.getString(cursor.getColumnIndexOrThrow("Username"))
            profile.email = cursor.getString(cursor.getColumnIndexOrThrow("Email"))
            profile.password = cursor.getString(cursor.getColumnIndexOrThrow("Password"))
            profile.telephoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("TelephoneNumber"))
            profile.gender = cursor.getString(cursor.getColumnIndexOrThrow("Gender"))
        }
        cursor.close()
        db.close()
        return (profile)
        //need check if profile null
    }

    fun checkUsername(username: String): Boolean{
        val profile = Profile()
        val db = readableDatabase
        val query = "SELECT * FROM Users WHERE Username = '$username'"
        val cursor = db.rawQuery(query,null)
        cursor.moveToFirst()

        if(cursor.count > 0){
            profile.userID = cursor.getInt(cursor.getColumnIndexOrThrow("UserID"))
            profile.username = cursor.getString(cursor.getColumnIndexOrThrow("Username"))
            profile.email = cursor.getString(cursor.getColumnIndexOrThrow("Email"))
            profile.password = cursor.getString(cursor.getColumnIndexOrThrow("Password"))
            profile.telephoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("TelephoneNumber"))
            profile.gender = cursor.getString(cursor.getColumnIndexOrThrow("Gender"))
        }
        cursor.close()
        db.close()
        if(profile.username!=null){
            return true
        }
        return false
    }

    fun checkPassword(password: String): Boolean{
        val profile = Profile()
        val db = readableDatabase
        val query = "SELECT * FROM Users WHERE Password = '$password'"
        val cursor = db.rawQuery(query,null)
        cursor.moveToFirst()

        if(cursor.count > 0){
            profile.userID = cursor.getInt(cursor.getColumnIndexOrThrow("UserID"))
            profile.username = cursor.getString(cursor.getColumnIndexOrThrow("Username"))
            profile.email = cursor.getString(cursor.getColumnIndexOrThrow("Email"))
            profile.password = cursor.getString(cursor.getColumnIndexOrThrow("Password"))
            profile.telephoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("TelephoneNumber"))
            profile.gender = cursor.getString(cursor.getColumnIndexOrThrow("Gender"))
        }
        cursor.close()
        db.close()
        if(profile.password!=null){
            return true
        }
        return false
        //need check if profile null
    }

    fun checkEmail(email: String): Boolean{
        val profile = Profile()
        val db = readableDatabase
        val query = "SELECT * FROM Users WHERE Email = '$email'"
        val cursor = db.rawQuery(query,null)
        cursor.moveToFirst()

        if(cursor.count > 0){
            profile.userID = cursor.getInt(cursor.getColumnIndexOrThrow("UserID"))
            profile.username = cursor.getString(cursor.getColumnIndexOrThrow("Username"))
            profile.email = cursor.getString(cursor.getColumnIndexOrThrow("Email"))
            profile.password = cursor.getString(cursor.getColumnIndexOrThrow("Password"))
            profile.telephoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("TelephoneNumber"))
            profile.gender = cursor.getString(cursor.getColumnIndexOrThrow("Gender"))
        }
        cursor.close()
        db.close()
        if(profile.email!=null){
            return true
        }
        return false
        //need check if profile null
    }

    fun insertFlight(flight: Flight){
        val db = writableDatabase
        val values = ContentValues().apply {
            put("AirlineID", flight.airlineID)
            put("Price",flight.price)
            put("FlightNumber", flight.flightNumber)
            put("Duration",flight.duration)
            put("DepartureAirport",flight.departureAirport)
            put("DepartureTime", flight.departureTime)
            put("DepartureID", flight.departureId)
            put("ArrivalAirport",flight.arrivalAirport)
            put("ArrivalTime",flight.arrivalTime)
            put("ArrivalID", flight.arrivalId)
        }
        db.insert("Flights",null,values)
        db.close()
    }

    fun getDepartureFlights(): MutableList<Flight>{
        val flights = ArrayList<Flight>()
        val db = readableDatabase
        val query = "SELECT fl.FlightID AS 'FlightID', fl.Price AS 'Price', fl.AirlineID AS 'AirlineID', ai.Image AS 'Image', ai.Name AS 'Name', ai.Airplane AS 'Airplane'," +
                " fl.FlightNumber AS 'FlightNumber', fl.Duration AS 'Duration', fl.DepartureAirport AS 'DepartureAirport', " +
                "fl.DepartureTime AS 'DepartureTime', fl.DepartureID AS 'DepartureID', fl.ArrivalID AS 'ArrivalID', fl.ArrivalAirport AS 'ArrivalAirport', fl.ArrivalTime AS 'ArrivalTime' " +
                "FROM Flights fl JOIN Airlines ai ON fl.AirlineID = ai.AirlineID "+
                "WHERE fl.DepartureID = 'CGK'"
        val cursor = db.rawQuery(query,null)
        cursor.moveToFirst()

        if(cursor.count > 0){
            do{
                val flight = Flight()
                flight.flightID = cursor.getInt(cursor.getColumnIndexOrThrow("FlightID"))
                flight.price = cursor.getInt(cursor.getColumnIndexOrThrow("Price"))
                flight.airlineID = cursor.getInt(cursor.getColumnIndexOrThrow("AirlineID"))
                flight.airlineImage = cursor.getString(cursor.getColumnIndexOrThrow("Image"))
                flight.airlineName = cursor.getString(cursor.getColumnIndexOrThrow("Name"))
                flight.airplane = cursor.getString(cursor.getColumnIndexOrThrow("Airplane"))
                flight.duration = cursor.getInt(cursor.getColumnIndexOrThrow("Duration"))
                flight.flightNumber = cursor.getString(cursor.getColumnIndexOrThrow("FlightNumber"))
                flight.departureAirport = cursor.getString(cursor.getColumnIndexOrThrow("DepartureAirport"))
                flight.departureTime = cursor.getString(cursor.getColumnIndexOrThrow("DepartureTime"))
                flight.departureId = cursor.getString(cursor.getColumnIndexOrThrow("DepartureID"))
                flight.arrivalAirport = cursor.getString(cursor.getColumnIndexOrThrow("ArrivalAirport"))
                flight.arrivalTime = cursor.getString(cursor.getColumnIndexOrThrow("ArrivalTime"))
                flight.arrivalId = cursor.getString(cursor.getColumnIndexOrThrow("ArrivalID"))
                flights.add(flight)
            }while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return (flights)
    }

    fun getArrivalFlights(): MutableList<Flight>{
        val flights = ArrayList<Flight>()
        val db = readableDatabase
        val query = "SELECT fl.FlightID AS 'FlightID', fl.Price AS 'Price', fl.AirlineID AS 'AirlineID', ai.Image AS 'Image', ai.Name AS 'Name', ai.Airplane AS 'Airplane'," +
                " fl.FlightNumber AS 'FlightNumber', fl.Duration AS 'Duration', fl.DepartureAirport AS 'DepartureAirport', " +
                "fl.DepartureTime AS 'DepartureTime', fl.DepartureID AS 'DepartureID', fl.ArrivalID AS 'ArrivalID', fl.ArrivalAirport AS 'ArrivalAirport', fl.ArrivalTime AS 'ArrivalTime' " +
                "FROM Flights fl JOIN Airlines ai ON fl.AirlineID = ai.AirlineID "+
                "WHERE fl.ArrivalID = 'CGK'"
        val cursor = db.rawQuery(query,null)
        cursor.moveToFirst()

        if(cursor.count > 0){
            do{
                val flight = Flight()
                flight.flightID = cursor.getInt(cursor.getColumnIndexOrThrow("FlightID"))
                flight.price = cursor.getInt(cursor.getColumnIndexOrThrow("Price"))
                flight.airlineID = cursor.getInt(cursor.getColumnIndexOrThrow("AirlineID"))
                flight.airlineImage = cursor.getString(cursor.getColumnIndexOrThrow("Image"))
                flight.airlineName = cursor.getString(cursor.getColumnIndexOrThrow("Name"))
                flight.airplane = cursor.getString(cursor.getColumnIndexOrThrow("Airplane"))
                flight.duration = cursor.getInt(cursor.getColumnIndexOrThrow("Duration"))
                flight.flightNumber = cursor.getString(cursor.getColumnIndexOrThrow("FlightNumber"))
                flight.departureAirport = cursor.getString(cursor.getColumnIndexOrThrow("DepartureAirport"))
                flight.departureTime = cursor.getString(cursor.getColumnIndexOrThrow("DepartureTime"))
                flight.departureId = cursor.getString(cursor.getColumnIndexOrThrow("DepartureID"))
                flight.arrivalAirport = cursor.getString(cursor.getColumnIndexOrThrow("ArrivalAirport"))
                flight.arrivalTime = cursor.getString(cursor.getColumnIndexOrThrow("ArrivalTime"))
                flight.arrivalId = cursor.getString(cursor.getColumnIndexOrThrow("ArrivalID"))
                flights.add(flight)
            }while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return (flights)
    }
    fun deleteTransaction(userId:Int?, flightId: Int?){
        val db = writableDatabase
        db.delete("Transactions","UserID=? AND FlightID=?", arrayOf(userId.toString(),flightId.toString()))
        db.close()
    }

    fun checkFlight(flightNumber: String): Boolean{
        val flight = Flight()
        val db = readableDatabase
        val query = "SELECT * FROM Flights WHERE FlightNumber = '$flightNumber'"
        val cursor = db.rawQuery(query,null)
        cursor.moveToFirst()

        if(cursor.count > 0){
            flight.flightID = cursor.getInt(cursor.getColumnIndexOrThrow("FlightID"))
        }
        cursor.close()
        db.close()
        if(flight.flightID!=null){
            return true
        }
        return false
    }

    fun insertTransaction(transaction: Transaction){
        val db = writableDatabase
        val values = ContentValues().apply {
            put("UserID", transaction.userID)
            put("FlightID", transaction.flightID)
        }
        db.insert("Transactions",null,values)
        db.close()
    }

    fun getTransaction(userID: Int?): MutableList<Flight>{
        val flights = ArrayList<Flight>()
        val db = readableDatabase
        val query = "SELECT tr.FlightID AS 'FlightID', fl.Price AS 'Price', fl.AirlineID AS 'AirlineID', ai.Image AS 'Image', ai.Name AS 'Name', ai.Airplane AS 'Airplane'," +
                " fl.FlightNumber AS 'FlightNumber', fl.Duration AS 'Duration', fl.DepartureAirport AS 'DepartureAirport', " +
                "fl.DepartureTime AS 'DepartureTime', fl.DepartureID AS 'DepartureID', fl.ArrivalID AS 'ArrivalID', fl.ArrivalAirport AS 'ArrivalAirport', fl.ArrivalTime AS 'ArrivalTime' " +
                "FROM Transactions tr JOIN Flights fl ON tr.FlightID = fl.FlightID JOIN Airlines ai ON fl.AirlineID = ai.AirlineID " +
                "JOIN Users us ON tr.UserID = us.UserID "+
                "WHERE tr.UserID = $userID"
        val cursor = db.rawQuery(query,null)
        cursor.moveToFirst()

        if(cursor.count > 0){
            do{
                val flight = Flight()
                flight.flightID = cursor.getInt(cursor.getColumnIndexOrThrow("FlightID"))
                flight.price = cursor.getInt(cursor.getColumnIndexOrThrow("Price"))
                flight.airlineID = cursor.getInt(cursor.getColumnIndexOrThrow("AirlineID"))
                flight.airlineImage = cursor.getString(cursor.getColumnIndexOrThrow("Image"))
                flight.airlineName = cursor.getString(cursor.getColumnIndexOrThrow("Name"))
                flight.airplane = cursor.getString(cursor.getColumnIndexOrThrow("Airplane"))
                flight.duration = cursor.getInt(cursor.getColumnIndexOrThrow("Duration"))
                flight.flightNumber = cursor.getString(cursor.getColumnIndexOrThrow("FlightNumber"))
                flight.departureAirport = cursor.getString(cursor.getColumnIndexOrThrow("DepartureAirport"))
                flight.departureTime = cursor.getString(cursor.getColumnIndexOrThrow("DepartureTime"))
                flight.departureId = cursor.getString(cursor.getColumnIndexOrThrow("DepartureID"))
                flight.arrivalAirport = cursor.getString(cursor.getColumnIndexOrThrow("ArrivalAirport"))
                flight.arrivalTime = cursor.getString(cursor.getColumnIndexOrThrow("ArrivalTime"))
                flight.arrivalId = cursor.getString(cursor.getColumnIndexOrThrow("ArrivalID"))
                flights.add(flight)
            }while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return (flights)
    }

    fun checkTransaction(flightId: Int?, userId: Int?): Boolean{
        val transaction = Transaction()
        val db = readableDatabase
        val query = "SELECT * FROM Transactions WHERE FlightID = $flightId AND UserID = $userId"
        val cursor = db.rawQuery(query,null)
        cursor.moveToFirst()

        if(cursor.count > 0){
            transaction.transactionID = cursor.getInt(cursor.getColumnIndexOrThrow("TransactionID"))
        }
        cursor.close()
        db.close()
        if(transaction.transactionID!=null){
            return true
        }
        return false
    }

    fun deleteFlights(){
        val db = writableDatabase
        db?.execSQL("DELETE FROM Flights")
        db.close()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS Flights")
        db?.execSQL("DROP TABLE IF EXISTS Airlines")
        db?.execSQL("DROP TABLE IF EXISTS Users")
        db?.execSQL("DROP TABLE IF EXISTS Transactions")
        onCreate(db)
    }

}