package com.example.assignmentindividu

import android.content.Intent
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Profile
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.assignmentindividu.database.DatabaseHelper
import com.example.assignmentindividu.databinding.ActivityDollDetailBinding
import com.example.assignmentindividu.items.Doll
import com.example.assignmentindividu.items.Transaction
import com.example.assignmentindividu.`object`.Data
import com.google.android.material.textfield.TextInputLayout
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.example.assignmentindividu.adapter.DollAdapter
import com.example.assignmentindividu.database.loadImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class DollDetail : AppCompatActivity() {


    private lateinit var dollDetailBinder: ActivityDollDetailBinding
    private lateinit var dollNameTV: TextView
    private lateinit var dollPriceTV: TextView
    private lateinit var dollRatingTV: TextView
    private lateinit var dollDescTV: TextView
    private lateinit var dollSizeTV: TextView
    private lateinit var dollImageView: ImageView
    private lateinit var quantityET: EditText
    private lateinit var quantityLayout: TextInputLayout
    private lateinit var purchaseButton: Button
    private lateinit var backButton: ImageButton

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dollDetailBinder = ActivityDollDetailBinding.inflate(layoutInflater)
        setContentView(dollDetailBinder.root)

        dollNameTV = dollDetailBinder.dollName
        dollPriceTV = dollDetailBinder.realPrice
        dollRatingTV = dollDetailBinder.dollRating
        dollDescTV = dollDetailBinder.dollDescription
        dollSizeTV = dollDetailBinder.realSize
        dollImageView = dollDetailBinder.dollImageView
        quantityET = dollDetailBinder.quantityText
        quantityLayout = dollDetailBinder.quantityTL
        purchaseButton = dollDetailBinder.purchaseButton
        backButton = dollDetailBinder.dollDetailBackButton

        val dollData = intent.getParcelableExtra("doll", Doll::class.java)

        val dollID = dollData?.id
        val dollName = dollData?.name
        val dollPrice = dollData?.price
        val dollRating = dollData?.rating
        val dollDescription = dollData?.description
        val dollSize = dollData?.size
        val dollPath = dollData?.path

        dollNameTV.text = dollName
        dollPriceTV.text = ("Rp.$dollPrice")
        dollRatingTV.text = dollRating.toString()
        dollDescTV.text = dollDescription
        dollSizeTV.text = ("$dollSize")
        if (dollPath != null) {
            lifecycleScope.launch{
                loadImage(dollImageView,dollPath)
            }
//            dollImageView.setImageResource(dollPath)
        }


        purchaseButton.setOnClickListener {
            var quantity = quantityET.text.toString().toIntOrNull()
            if(quantity == null || quantity <= 0){
                val toast = Toast.makeText(this,"Please insert the doll quantity you want to buy",Toast.LENGTH_SHORT)
                toast.show()
            }
            else{
                val date = LocalDate.now()
                date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                val userID = Data.myProfile?.userID
                val size = Data.transactionList?.size
                val transactionID = Data.transactionList?.size
                val newTransaction = Transaction(userID,dollPath,dollID, transactionID,dollName,quantity,date.toString())
                Data.transactionList?.add(newTransaction)
                databaseHelper = DatabaseHelper(this)
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