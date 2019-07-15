package com.bt_121shoppe.lucky_app.Buy_Sell_Rent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import com.bt_121shoppe.lucky_app.Activity.*
import com.bt_121shoppe.lucky_app.Buy_Sell_Rent.Sell.fragment_sell_eletronics
import com.bt_121shoppe.lucky_app.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class Sell_Main2 : AppCompatActivity() {

    private var content: FrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        content = findViewById(R.id.content) as FrameLayout
        supportFragmentManager.beginTransaction().replace(R.id.content, fragment_sell_eletronics()).commit()
        val bnavigation = findViewById<BottomNavigationView>(R.id.navigation)
        bnavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    val intent = Intent(this@Sell_Main2,Home::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
                }
                R.id.notification -> {
                    val intent = Intent(this@Sell_Main2,Notification::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
                }
                R.id.camera ->{
                    val intent = Intent(this@Sell_Main2, Camera::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
                }
                R.id.message -> {
                    val intent = Intent(this@Sell_Main2,Message::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
                }
                R.id.account ->{
                    val intent = Intent(this@Sell_Main2, Account::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)
                }
            }
            true
        }
    }
}