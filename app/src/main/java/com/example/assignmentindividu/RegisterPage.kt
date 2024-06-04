package com.example.assignmentindividu

import Profile
import android.content.Intent
import android.graphics.Color
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.example.assignmentindividu.database.DatabaseHelper
import com.example.assignmentindividu.databinding.ActivityRegisterPageBinding
import com.example.assignmentindividu.`object`.Data
import com.google.android.material.textfield.TextInputLayout

class RegisterPage : AppCompatActivity() {

    private lateinit var registerBinder: ActivityRegisterPageBinding
    private lateinit var usernameLayout: TextInputLayout
    private lateinit var usernameET: EditText
    private lateinit var passLayout: TextInputLayout
    private lateinit var passET: EditText
    private lateinit var emailLayout: TextInputLayout
    private lateinit var emailET: EditText
    private lateinit var phoneLayout: TextInputLayout
    private lateinit var phoneET: EditText
    private lateinit var femaleRB: RadioButton
    private lateinit var maleRB: RadioButton

    private lateinit var registerBtn: Button
    private lateinit var loginText: TextView

    private  var userID: Int = 0
    private lateinit var username: String
    private lateinit var password: String
    private lateinit var email: String
    private lateinit var phoneNumber: String
    private lateinit var gender: String

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinder = ActivityRegisterPageBinding.inflate(layoutInflater)
        setContentView(registerBinder.root)

        usernameET = registerBinder.usernameText
        usernameLayout = registerBinder.UsernameTL
        passLayout = registerBinder.PasswordTL
        passET = registerBinder.passwordText
        emailLayout = registerBinder.EmailTL
        emailET = registerBinder.emailText
        phoneLayout = registerBinder.PhoneNumberTL
        phoneET = registerBinder.phonenumberText

        femaleRB = registerBinder.femaleButton
        maleRB = registerBinder.maleButton

        registerBtn = registerBinder.registerBtn
        loginText = registerBinder.loginText


        //initialize
        println("aaaaa" + Data.profileList)

        maleRB.isChecked

        usernameET.doOnTextChanged{text, start, before, count ->
            if(text!!.isEmpty()){
                usernameLayout.helperText = "username must be filled"
            }
            else{
                usernameLayout.helperText = null
            }
        }

        passET.doOnTextChanged{text, start, before, count ->
            if(text!!.isEmpty()){
                passLayout.helperText = "password must be filled"
            }
            else if(text.length < 8){
                passLayout.helperText = "password must be more than 8 characters"
            }
            else{
                passLayout.helperText = null
            }
        }

        emailET.doOnTextChanged{text, start, before, count ->
            if(text!!.isEmpty()){
                emailLayout.helperText = "email must be filled"
            }
            else if(!text.endsWith("@puff.com")){
                emailLayout.helperText = "email must end with @puff.com"
            }
            else{
                emailLayout.helperText = null
            }
        }

        phoneET.doOnTextChanged{text, start, before, count ->
            if(text!!.isEmpty()){
                phoneLayout.helperText = "phone number must be filled"
            }
            else if(text.length < 11 || text.length > 13){
                phoneLayout.helperText = "phone number must be between 11-13 digits"
            }
            else{
                phoneLayout.helperText = null
            }
        }

        registerBtn.setOnClickListener{
            if(usernameET.text.toString().isEmpty()){
                val toast = Toast.makeText(this, "Username can't be empty", Toast.LENGTH_SHORT)
                toast.show()
            }
            else if(usernameET.text.toString() == "admin"){
                val toast = Toast.makeText(this, "There is already this username", Toast.LENGTH_SHORT)
                toast.show()
            }

            else if(passET.text.toString().isEmpty()){
                val toast = Toast.makeText(this, "Password can't be empty", Toast.LENGTH_SHORT)
                toast.show()
            }
            else if(passET.text.toString().length < 8){
                val toast = Toast.makeText(this, "Password need to be longer than 8 characters", Toast.LENGTH_SHORT)
                toast.show()
            }
            else if(emailET.text.toString().isEmpty()){
                val toast = Toast.makeText(this, "Email can't be empty", Toast.LENGTH_SHORT)
                toast.show()
            }
            else if(!emailET.text.toString().endsWith("@puff.com")){
                val toast = Toast.makeText(this, "Email must ends with '@puff.com'", Toast.LENGTH_SHORT)
                toast.show()
            }
            else if(phoneET.text.toString().isEmpty()){
                val toast = Toast.makeText(this, "Phone number can't be empty", Toast.LENGTH_SHORT)
                toast.show()
            }
            else if(phoneET.text.toString().length < 11 || phoneET.text.toString().length > 13){
                val toast = Toast.makeText(this, "Phone number must be between 11-13 digits", Toast.LENGTH_SHORT)
                toast.show()
            }
            else{
                if (Data.profileList != null) {
                    userID = Data.profileList!!.size + 1
                }
                convertAllToString()

                val newProfile = Profile(userID, username, password, email, phoneNumber, gender)

                Data.profileList?.add(newProfile)
                databaseHelper = DatabaseHelper(this)
                databaseHelper.insertProfile(newProfile)
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }
        }

        loginText.setOnClickListener {
            loginText.setTextColor(Color.parseColor("#0000EE"))
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }
    fun convertAllToString(){
        username = usernameET.text.toString()
        password = passET.text.toString()
        email = emailET.text.toString()
        phoneNumber = phoneET.text.toString()

        if(maleRB.isChecked)
            gender = "Male"
        else
            gender = "Female"
    }
}