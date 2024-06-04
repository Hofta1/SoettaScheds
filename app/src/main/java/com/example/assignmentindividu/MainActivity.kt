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
//            databaseHelper.insertDoll(Doll(downloadImage("https://images-cdn.ubuy.co.id/633b0a3f4467b50d284845c5-toys-studio-36-inch-big-teddy-bear-cute.jpg"),1, "Teddy Bear","Medium", 4.8, 5.0, "A classic teddy bear that's loved by all kids!"))
//            databaseHelper.insertDoll(Doll(R.drawable.barbie,2, "Barbie","Medium", 4.5, 7.0, "Customize your barbie and choose what barbie wear!"))
//            databaseHelper.insertDoll(Doll(R.drawable.bigteddy,3, "Big Teddy","Medium", 4.7, 20.0, "A classic but twice more hug-able"))
//            databaseHelper.insertDoll(Doll(R.drawable.bluebear,4, "Clubby","Medium", 4.1, 4.5, "Teddy's new friend. Likes blueberry!"))
//            databaseHelper.insertDoll(Doll(R.drawable.cat,5, "Kitty","Medium", 4.9, 6.0, "A cute white cat that likes to laze around. Meow!"))
//            databaseHelper.insertDoll(Doll(R.drawable.corgi,6, "Cappuccino","Medium", 4.8, 6.0, "A cute corgi that will follow you around!"))
//            databaseHelper.insertDoll(Doll(R.drawable.dog,7, "Coffee","Medium", 4.5, 6.0, "A talkative dog. Bark bark!"))
//            databaseHelper.insertDoll(Doll(R.drawable.elephant,8, "Dumban","Medium", 4.4, 5.5, "They say she can fly with her huge ear"))
//            databaseHelper.insertDoll(Doll(R.drawable.fairy,9, "Aradum","Medium", 4.1, 4.0, "A brave fairy that protects the forest"))
//            databaseHelper.insertDoll(Doll(R.drawable.monkey,10, "Manke","Medium", 3.5, 5.5, "A polite monkey that always say please"))
//            databaseHelper.insertDoll(Doll(R.drawable.mushroom,11, "Fungee","Medium", 4.7, 5.0, "It likes to chill around and sunbath"))
//            databaseHelper.insertDoll(Doll(R.drawable.pillowdog,12, "Pillow Dog","Medium", 4.6, 7.0, "A dog that will accompany you to sleep"))
//            databaseHelper.insertDoll(Doll(R.drawable.pillowpengu,13, "Pillow Penguin","Medium", 4.9, 7.0, "A penguin that will accompany you to sleep"))
//            databaseHelper.insertDoll(Doll(R.drawable.rabbit,14, "Bunny","Medium", 4.9, 5.0, "A friendly rabbit that wants to be friend with you. Just don't take his carrot"))
//            databaseHelper.insertDoll(Doll(R.drawable.reversibleoctopus,15, "Octopy","Medium", 4.8, 7.0, "A moody octopus"))
//            databaseHelper.insertDoll(Doll(R.drawable.shark,16, "Bruce","Medium", 4.4, 6.0, "A vegetarian shark that likes to eat"))
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



        fun checkData(username: String, password: String): Profile? {
            Data.profileList?.forEach(){ profile ->
                if(profile.username == username && profile.password == password){
                    return profile
                }
            }
            return null
        }

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
            if(loginProfile2.username ==null){
                val toast = Toast.makeText(this, "There is no username or password", Toast.LENGTH_SHORT)
                toast.show()
            }
            else{
                val intent = Intent(this,HomePage::class.java)
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