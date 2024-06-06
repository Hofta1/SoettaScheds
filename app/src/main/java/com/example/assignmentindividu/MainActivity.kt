package com.example.assignmentindividu

import Profile
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
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
import com.example.assignmentindividu.items.Doll
import com.example.assignmentindividu.items.Transaction
import com.example.assignmentindividu.`object`.Data
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import androidx.lifecycle.lifecycleScope
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.launch
import org.json.JSONException

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

//    private var transactions = arrayListOf(
//        Transaction(1,R.drawable.shark,1,1,"Bruce",1, "03-02-2024"),
//        Transaction(1,R.drawable.cat,2,2,"Kitty",2, "05-02-2024"),
//        Transaction(1,R.drawable.bigteddy,3,3,"Big Teddy",1, "08-02-2024"),
//        Transaction(1,R.drawable.normalteddy,4,4,"Teddy Bear",4, "17-02-2024"),
//        Transaction(1,R.drawable.bluebear,5,5,"Clubby",3, "17-02-2024"),
//        Transaction(1,R.drawable.corgi,6,6,"Cappuccino",3, "17-02-2024"),
//        Transaction(2,R.drawable.normalteddy,7,7,"Teddy Bear",1, "25-02-2024"),
//        Transaction(2,R.drawable.fairy,8,8,"Aradum",1, "29-02-2024"),
//        Transaction(2,R.drawable.pillowpengu,9,9,"Pillow Penguin",1, "03-03-2024"),
//        Transaction(2,R.drawable.pillowdog,10,10,"Pillow Dog",1, "05-03-2024")
//    )

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
        if(databaseHelper.getDolls().isEmpty()){
            val url = "https://api.npoint.io/9d7f4f02be5d5631a664"
            requestQueue = Volley.newRequestQueue(this)

            val request = JsonObjectRequest(
                Request.Method.GET, url, null,

                Response.Listener { response ->
                    try {
                        parseJSON(response)
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
        //initialize
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


    private fun parseJSON(jsonObject: JSONObject){
        databaseHelper = DatabaseHelper(this)
        try {
            val dollArray = jsonObject.getJSONArray("dolls")
            for (i in 0 until dollArray.length()){
                val dollObject = dollArray.getJSONObject(i)
                val path = dollObject.getString("imageLink")
                val id = 1
                val name = dollObject.getString("name")
                val size = dollObject.getString("size")
                val rating = dollObject.getString("rating").toDouble()
                val price = dollObject.getString("price").toDouble()
                val description = dollObject.getString("desc")
                databaseHelper.insertDoll(Doll(path,1, name,size, rating, price, description))
            }
        }catch (e: JSONException) {
            e.printStackTrace()
            null
        }
    }

}