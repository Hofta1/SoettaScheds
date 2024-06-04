package com.example.assignmentindividu.database

import Profile
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.assignmentindividu.items.Doll
import com.example.assignmentindividu.items.Transaction

class DatabaseHelper(context: Context):SQLiteOpenHelper(context,"puffpoof.db",null,1){
    override fun onCreate(db: SQLiteDatabase?) {
        val queryCreateDoll = "CREATE TABLE IF NOT EXISTS \"Dolls\" (" +
                "\"DollID\"INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "\"Name\"TEXT," +
                "\"Size\"INTEGER," +
                "\"Rating\"REAL," +
                "\"Price\"REAL," +
                "\"Image\"TEXT," +
                "\"Description\"BLOB" +
                ")"

        val queryCreateTransaction = "CREATE TABLE IF NOT EXISTS\"Transactions\" (\n" +
                "\t\"TransactionID\"\tINTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "\t\"UserID\"\tINTEGER NOT NULL,\n" +
                "\t\"DollID\"\tINTEGER NOT NULL,\n" +
                "\t\"TransactionDate\"\tTEXT NOT NULL,\n" +
                "\t\"Quantity\"\t INT NOT NULL,\n" +
                "\tFOREIGN KEY(\"DollID\") REFERENCES \"Dolls\"(\"DollID\") ON UPDATE CASCADE,\n" +
                "\tFOREIGN KEY(\"UserID\") REFERENCES \"Users\"(\"UserID\") ON UPDATE CASCADE\n" +
                ");"

        val queryCreateUser = "CREATE TABLE IF NOT EXISTS \"Users\" (\n" +
                "\t\"UserID\"\tINTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "\t\"Username\"\tTEXT NOT NULL,\n" +
                "\t\"Email\"\tTEXT NOT NULL,\n" +
                "\t\"Password\"\tTEXT NOT NULL,\n" +
                "\t\"TelephoneNumber\"\tTEXT NOT NULL,\n" +
                "\t\"Gender\"\tTEXT NOT NULL\n" +
                ");"

        db?.execSQL(queryCreateDoll)
        db?.execSQL(queryCreateTransaction)
        db?.execSQL(queryCreateUser)
    }

    fun insertDoll(doll: Doll){
        val db = writableDatabase
        val values = ContentValues().apply {
//            put("DollID",doll.id)
            put("Name", doll.name)
            put("Size", doll.size)
            put("Rating", doll.rating)
            put("Price", doll.price)
            put("Image", doll.path)
            put("Description", doll.description)
        }
        db.insert("Dolls",null,values)
        db.close()
    }

    fun getDolls(): List<Doll>{
        val dolls = ArrayList<Doll>()
        val db = readableDatabase
        val query = "SELECT * FROM Dolls"
        val cursor = db.rawQuery(query,null)
        cursor.moveToFirst()

        if(cursor.count > 0){
            do{
                val doll = Doll()
                doll.id = cursor.getInt(cursor.getColumnIndexOrThrow("DollID"))
                doll.name = cursor.getString(cursor.getColumnIndexOrThrow("Name"))
                doll.size = cursor.getString(cursor.getColumnIndexOrThrow("Size"))
                doll.rating = cursor.getDouble(cursor.getColumnIndexOrThrow("Rating"))
                doll.price = cursor.getDouble(cursor.getColumnIndexOrThrow("Price"))
                doll.path = cursor.getString(cursor.getColumnIndexOrThrow("Image"))
                doll.description = cursor.getString(cursor.getColumnIndexOrThrow("Description"))
                dolls.add(doll)
            }while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return (dolls)
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

    fun insertTransaction(transaction: Transaction){
        val db = writableDatabase
        val values = ContentValues().apply {
            put("UserID", transaction.userID)
            put("DollID", transaction.dollID)
            put("TransactionDate", transaction.transactionDate)
            put("Quantity", transaction.transactionQuantity)
        }
        db.insert("Transactions",null,values)
        db.close()
    }

    fun deleteTransaction(transaction: Transaction){
        val db = writableDatabase
        db.delete("Transactions","TransactionID=?", arrayOf(transaction.transactionID.toString()))
        db.close()
    }

    fun getTransaction(userID: Int?): MutableList<Transaction>{
        val transactions = ArrayList<Transaction>()
        val db = readableDatabase
        val query = "SELECT tr.UserID AS 'UserID', dl.Image AS 'Image', tr.DollID AS 'DollID', tr.TransactionID AS 'TransactionID', dl.Name AS 'Name', tr.Quantity AS 'Quantity', tr.TransactionDate AS 'TransactionDate' " +
                "FROM Transactions tr JOIN Dolls dl ON tr.DollID = dl.DollID JOIN Users us ON tr.UserID = us.UserID "+
                "WHERE tr.UserID = $userID"
        val cursor = db.rawQuery(query,null)
        cursor.moveToFirst()

        if(cursor.count > 0){
            do{
                val transaction = Transaction()
                transaction.userID = cursor.getInt(cursor.getColumnIndexOrThrow("UserID"))
                transaction.path = cursor.getString(cursor.getColumnIndexOrThrow("Image"))
                transaction.dollID = cursor.getInt(cursor.getColumnIndexOrThrow("DollID"))
                transaction.transactionID = cursor.getInt(cursor.getColumnIndexOrThrow("TransactionID"))
                transaction.dollName = cursor.getString(cursor.getColumnIndexOrThrow("Name"))
                transaction.transactionQuantity = cursor.getInt(cursor.getColumnIndexOrThrow("Quantity"))
                transaction.transactionDate = cursor.getString(cursor.getColumnIndexOrThrow("TransactionDate"))
                transactions.add(transaction)
            }while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return (transactions)
    }

    fun editQuantity(quantity: Int, transactionId: Int){
        val db = writableDatabase
        val query = "UPDATE Transactions "+
                    "SET Quantity = $quantity "+
                    "WHERE TransactionID = $transactionId"
        db?.execSQL(query)
        db.close()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS Transactions")
        db?.execSQL("DROP TABLE IF EXISTS Dolls")
        db?.execSQL("DROP TABLE IF EXISTS Users")
        onCreate(db)
    }

}