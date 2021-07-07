package com.example.cafeon

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.cafeon.databinding.ActivityLoginBinding
import com.example.cafeon.databinding.ActivityMainBinding
import com.example.cafeon.datamodel.UserInfo
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    private lateinit var mainActivityBinding: ActivityMainBinding // 액티비티 바인딩
    var firestore: FirebaseFirestore? = null
    var userInformation: UserInfo? = null // 유저 정보가 담기는 데이터 클래스

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(mainActivityBinding.root)

        firestore = FirebaseFirestore.getInstance();
        runBlocking {
            userInformation = intent.getSerializableExtra("userinformation") as UserInfo
        }

        mainActivityBinding.userId.text = userInformation!!.nickname.toString().plus("님, 안녕하세요 :)")
    }
}