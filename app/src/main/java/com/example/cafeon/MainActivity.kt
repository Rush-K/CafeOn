package com.example.cafeon

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.cafeon.databinding.ActivityLoginBinding
import com.example.cafeon.databinding.ActivityMainBinding
import com.example.cafeon.datamodel.UserInfo
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener { // bottom navigation view를 이용하기 위한 상속

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

        mainActivityBinding.toolbarNickname.text = userInformation!!.nickname.toString().plus(" 님")
        mainActivityBinding.bottomNavigation.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_profile -> { // 프로필 Fragment 호출
                val profileFragment = ProfileFragment()

                var bundle = Bundle(); // profileFragment 의 Argument
                bundle.putString("requestedUid", FirebaseAuth.getInstance().currentUser!!.uid)
                bundle.putSerializable("uidUserInfo", userInformation)

                profileFragment.arguments = bundle;

                supportFragmentManager.beginTransaction().replace(R.id.main_content, profileFragment)
                    .commit()
                return true;
            }
            R.id.action_home -> {
                //val
                return true;
            }
        }
        return false;
    }
}