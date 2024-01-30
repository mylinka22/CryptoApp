package com.example.crypto

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.crypto.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.NumberFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {
    lateinit var Auth:FirebaseAuth
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        Auth = FirebaseAuth.getInstance()

        val user = Auth.currentUser
        val userName = user?.displayName
        val userEmail = user?.email

        binding.apply {
            btnsettings.setOnClickListener(logout())
        }

        if (userEmail != null) {
            println("Имя пользователя: $userEmail")
            binding.textusername.text = userEmail

        } else {
            println("Имя пользователя не найдено")
        }


        checkbtc { bitcoinPrice ->
            val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())
            binding.costbtc.text = "$${numberFormat.format(bitcoinPrice)}"
            binding.allcash.text = "$ ${numberFormat.format(bitcoinPrice.toString().toInt()*binding.countbtc.text.toString().toInt())}"
            binding.btccash.text = "$${numberFormat.format(bitcoinPrice.toString().toInt()*binding.countbtc.text.toString().toInt())}"
        }


    }

    private fun logout(): View.OnClickListener{
        return View.OnClickListener {
            val sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("user_id", null)
            editor.putString("user_email", null)
            editor.apply()
        }
    }


    private fun checkbtc(callback: (Int) -> Unit) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.coingecko.com/api/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val cryptoApi = retrofit.create(CryptoApiService::class.java)
        val call = cryptoApi.getBitcoinPrice()

        call.enqueue(object : Callback<BitcoinResponse> {
            override fun onResponse(call: Call<BitcoinResponse>, response: Response<BitcoinResponse>) {
                if (response.isSuccessful) {
                    val bitcoinResponse = response.body()
                    val bitcoinPrice: Int = bitcoinResponse?.bitcoin?.usd?.toInt() ?: 0
                    callback(bitcoinPrice)
                }
            }

            override fun onFailure(call: Call<BitcoinResponse>, t: Throwable) {
                println("Ошибка при получении курса биткоина: ${t.message}")
            }
        })
    }


}