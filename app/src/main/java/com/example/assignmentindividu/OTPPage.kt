package com.example.assignmentindividu

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.assignmentindividu.databinding.ActivityOtpPageBinding
import com.example.assignmentindividu.`object`.Data


class OTPPage : AppCompatActivity() {

    private lateinit var binding: ActivityOtpPageBinding
    private lateinit var phoneNumberTV: TextView
    private lateinit var otpCodeET: EditText
    private lateinit var submitButton: Button
    private lateinit var smsManager: SmsManager
    private lateinit var resendTV: TextView

    private lateinit var phoneNumber: String
    private lateinit var message: String
    private lateinit var otpInput: String
    override fun onCreate(savedInstanceState: Bundle?) {
        var otpCode = random4Digit()
        super.onCreate(savedInstanceState)
        binding = ActivityOtpPageBinding.inflate(layoutInflater)

        setContentView(binding.root)

        phoneNumberTV = binding.phoneNumber
        otpCodeET = binding.OTPText
        submitButton = binding.submitButton
        resendTV = binding.resendTV

        smsManager = SmsManager.getDefault()

        phoneNumberTV.text = Data.myProfile?.telephoneNumber
        phoneNumber = phoneNumberTV.text.toString()


        message = "Please verify your account by inputting your OTP Code: $otpCode"
        checkSendSMSPermission(phoneNumber,message)


        submitButton.setOnClickListener {
            otpInput = otpCodeET.text.toString()
            if(otpInput == otpCode){
                val intent = Intent(this,HomePage::class.java)
                startActivity(intent)
            }else{
                val toast = Toast.makeText(this,"You have inputted the wrong OTP Code",Toast.LENGTH_SHORT)
                toast.show()
            }
        }

        resendTV.setOnClickListener {
            otpCode = random4Digit()
            message = "Please verify your account by inputting your OTP Code: $otpCode"
            checkSendSMSPermission(phoneNumber,message)
        }

    }


    fun checkSendSMSPermission(phonenumber: String, message: String){
        //request permission kalo belom ada
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.SEND_SMS),100)
        }
        else{
            sendSMS(phonenumber,message)
        }
    }
        private fun sendSMS(phonenumber: String, message: String){
            smsManager.sendTextMessage(phonenumber,null,message,null,null)
        }
    private fun random4Digit(): String {
        var i = 0
        var otp = ""
        while(i < 4){
            var num = (0 until 9).random()
            otp += num
            i++
        }

        return otp
    }
}