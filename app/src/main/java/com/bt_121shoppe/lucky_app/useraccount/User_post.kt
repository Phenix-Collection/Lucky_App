package com.bt_121shoppe.lucky_app.useraccount

import android.content.Context
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.bt_121shoppe.lucky_app.Api.ConsumeAPI
import com.bt_121shoppe.lucky_app.Api.User
import com.bt_121shoppe.lucky_app.Product_dicount.Tab_Adapter
import com.bt_121shoppe.lucky_app.R
import com.bt_121shoppe.lucky_app.models.PostViewModel
import com.bt_121shoppe.lucky_app.utils.CommomAPIFunction
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.JsonParseException
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_user_post.pager
import kotlinx.android.synthetic.main.activity_user_post.tab
import kotlinx.android.synthetic.main.fragment_acount.*
import kotlinx.android.synthetic.main.item_grid.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class User_post : AppCompatActivity() {

    var user_id:Int=0
    var username:String=""
    var password:String=""
    var encode=""

    private lateinit var img_user:CircleImageView
    private lateinit var Username:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_post)

        var bundle :Bundle ?=intent.extras
        user_id = intent.getStringExtra("ID").toInt()
        val sharedPref: SharedPreferences = getSharedPreferences("Register", Context.MODE_PRIVATE)
        username = sharedPref.getString("name", "")
        password = sharedPref.getString("pass", "")
        encode = "Basic "+com.bt_121shoppe.lucky_app.utils.CommonFunction.getEncodedString(username,password)
        Username = findViewById(R.id.username)
        img_user = findViewById(R.id.img_user)

        findViewById<TextView>(R.id.tv_back).setOnClickListener { finish() }
        configureTabLayout()
        getUserProfile()
        //getUserPosts()
    }
    private fun configureTabLayout() {
        tab.addTab(tab.newTab().setText(getString(R.string.post)))
        tab.addTab(tab.newTab().setText(getString(R.string.contact)))

        val adapter = Tab_Adapter(supportFragmentManager, tab.tabCount)
        pager.adapter = adapter
        pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tab))
        tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                pager.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {
            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    fun getUserProfile(){
        var user1 = User()
        var URL_ENDPOINT= ConsumeAPI.BASE_URL+"api/v1/users/"+user_id
        var MEDIA_TYPE= MediaType.parse("application/json")
        var client= OkHttpClient()
        var request= Request.Builder()
                .url(URL_ENDPOINT)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                //.header("Authorization",encode)
                .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                val mMessage = e.message.toString()
                Log.w("failure Response", mMessage)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val mMessage = response.body()!!.string()
                val gson = Gson()
                try {
                    user1= gson.fromJson(mMessage, User::class.java)
                    runOnUiThread {

                            val profilepicture: String=if(user1.profile.profile_photo==null) "" else user1.profile.base64_profile_image
                            val coverpicture: String= if(user1.profile.cover_photo==null) "" else user1.profile.base64_cover_photo_image
                            if(user1.getFirst_name().isEmpty())
                            {
                                Username!!.setText(user1.getUsername())
                            }else {
                                Username!!.setText(user1.getFirst_name())
                            }

                        CommomAPIFunction.getUserProfileFB(this@User_post,img_user,user1.username)

                            if(coverpicture.isNullOrEmpty()){

                            }else {
                                val decodedString = Base64.decode(coverpicture, Base64.DEFAULT)
                                var decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
//                                imgCover!!.setImageBitmap(decodedByte)
                            }

                    }

                } catch (e: JsonParseException) {
                    e.printStackTrace()
                }
            }
        })
    }

    fun getUserPosts(){
        var posts= PostViewModel()
        val URL_ENDPOINT=ConsumeAPI.BASE_URL+"postbyuserfilter/?created_by="+user_id+"&approved_by=&rejected_by=&modified_by="
        var MEDIA_TYPE=MediaType.parse("application/json")
        val client= OkHttpClient()
        val request=Request.Builder()
                .url(URL_ENDPOINT)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                //.header("Authorization",encode)
                .build()
        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                val mMessage = e.message.toString()
                Log.w("failure Response", mMessage)
            }
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val mMessage = response.body()!!.string()
                val gson = Gson()
                Log.d("TAH","TT"+mMessage)

                try {
                    val jsonObject= JSONObject(mMessage)
                    val jsonArray=jsonObject.getJSONArray("results")
                    val jsonCount=jsonObject.getInt("count")
                    runOnUiThread {
                        if(jsonCount>0){
                            for (i in 0 until jsonArray.length()) {
                                val obj=jsonArray.getJSONObject(i)
                                Log.e("TAG","T"+obj)
                            }

                        }

                    }

                } catch (e: JsonParseException) {
                    e.printStackTrace()
                }

            }
        })
    }
}
