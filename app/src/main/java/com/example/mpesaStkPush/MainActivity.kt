package com.example.mpesaStkPush

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mpesaStkPush.Constants.BUSINESS_SHORT_CODE
import com.example.mpesaStkPush.Constants.PARTYB
import com.example.mpesaStkPush.Constants.PASSKEY
import com.example.mpesaStkPush.Constants.TRANSACTION_TYPE
import com.example.mpesaStkPush.Utils.getPassword
import com.example.mpesaStkPush.Utils.sanitizePhoneNumber
import com.example.mpesaStkPush.Utils.timestamp
import com.example.mpesaStkPush.model.AccessToken
import com.example.mpesaStkPush.model.STKPush
import com.example.mpesaStkPush.model.STKPushResponse
import com.example.mpesaStkPush.services.DarajaApiClient
import com.example.mpesaapp.databinding.ActivityMainBinding
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber


class MainActivity : AppCompatActivity(), MpesaListener {


    private lateinit var binding : ActivityMainBinding
    private lateinit var mApiClient: DarajaApiClient
    private lateinit var mProgressDialog: ProgressDialog

    companion object {
        lateinit var mpesaListener: MpesaListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mpesaListener = this
        mProgressDialog = ProgressDialog(this)
        mApiClient = DarajaApiClient()
        mApiClient.setIsDebug(true) //Set True to enable logging, false to disable.
        binding.mPay.setOnClickListener(::onClick)
        getAccessToken()
    }

    private fun getAccessToken() {
        mApiClient.setGetAccessToken(true)
        mApiClient.mpesaService().accessToken!!.enqueue(object : Callback<AccessToken?> {
            override fun onResponse(call: Call<AccessToken?>, response: Response<AccessToken?>) {
                if (response.isSuccessful) {
                    mApiClient.setAuthToken(response.body()?.accessToken)
                }
            }

            override fun onFailure(call: Call<AccessToken?>, t: Throwable) {}
        })
    }


    private fun onClick(view: View) {
        if (view === binding.mPay) {
            val phoneNumber = binding.mPhone.text.toString()
            val amount = binding.mAmount.text.toString()
            performSTKPush(phoneNumber, amount)
        }
    }


    private fun performSTKPush(phoneNumber: String?, amount: String) {
        mProgressDialog.setMessage("Processing your request")
        mProgressDialog.setTitle("Please Wait...")
        mProgressDialog.isIndeterminate = true
        mProgressDialog.show()
        val timestamp = timestamp
        val stkPush = STKPush(
            BUSINESS_SHORT_CODE,
            getPassword(BUSINESS_SHORT_CODE, PASSKEY, timestamp),
            timestamp,
            TRANSACTION_TYPE, amount,
            sanitizePhoneNumber(phoneNumber!!),
            PARTYB,
            sanitizePhoneNumber(phoneNumber),
            BuildConfig.FIREBASE_CALLBACK_URL,
            "Formatio",  //Account reference
            "Formatio Subscribe" //Transaction description
        )
        mApiClient.setGetAccessToken(false)

        //Sending the data to the Mpesa API, remember to remove the logging when in production.
        mApiClient.mpesaService().sendPush(stkPush)!!.enqueue(object : Callback<STKPushResponse?> {
            override fun onResponse(call: Call<STKPushResponse?>, response: Response<STKPushResponse?>) {
                mProgressDialog.dismiss()
                try {
                    if (response.isSuccessful) {
                        Timber.d("post submitted to API. %s", response.body())
                        FirebaseMessaging.getInstance()
                            .subscribeToTopic(response.body()!!.CheckoutRequestID)
                    } else {
                        Timber.e("Response %s", response.errorBody()?.string())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<STKPushResponse?>, t: Throwable) {
                mProgressDialog.dismiss()
                Timber.e(t)
            }
        })
    }

    override fun onPointerCaptureChanged(hasCapture: Boolean) {}

    override fun sendSuccesfull(amount: String, phone: String, date: String, receipt: String) {
        runOnUiThread {
            Toast.makeText(
                this, "Payment Succesfull\n" +
                        "Receipt: $receipt\n" +
                        "Date: $date\n" +
                        "Phone: $phone\n" +
                        "Amount: $amount", Toast.LENGTH_LONG
            ).show()

        }
    }

    override fun sendFailed(reason: String) {
        runOnUiThread {
            Toast.makeText(
                this, "Payment Failed\n" +
                        "Reason: $reason"
                , Toast.LENGTH_LONG
            ).show()
        }
    }
}