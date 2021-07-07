package com.example.cafeon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.cafeon.databinding.ActivityLoginBinding
import com.example.cafeon.databinding.ActivityMainBinding
import com.example.cafeon.datamodel.UserInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

//import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() { // ? : null 일 수 있음, !! : null 이 아님

    // Firebase Authentication 관리 클래스
    var auth: FirebaseAuth? = null
    var firestore: FirebaseFirestore? = null
    var userInformation: UserInfo? = null // 유저 정보가 담기는 데이터 클래스
    private lateinit var loginActivityBinding: ActivityLoginBinding // 액티비티 바인딩

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginActivityBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginActivityBinding.root)

        auth = FirebaseAuth.getInstance() // Firebase 객체 생성
        firestore = FirebaseFirestore.getInstance() // Firestore 객체 생성
        loginActivityBinding.emailLoginButton.setOnClickListener { emailLogin() }
    }

    fun emailLogin() { // 이메일 로그인
        if (loginActivityBinding.emailEdittext.text.toString().isNullOrEmpty() || loginActivityBinding.passwordEdittext.text.toString().isNullOrEmpty()) { // ID, 비번 하나라도 비어있는 경우
            Toast.makeText(this, getString(R.string.signout_fail_null), Toast.LENGTH_SHORT).show()
        } else {
            loginActivityBinding.progressBar.visibility = View.VISIBLE
            createAndLogin()
        }
    }

    fun createAndLogin() { // 로그인 정보가 없을 경우 자동 가입
        auth?.createUserWithEmailAndPassword(loginActivityBinding.emailEdittext.text.toString(), loginActivityBinding.passwordEdittext.text.toString())
            ?.addOnCompleteListener {
                task ->
                loginActivityBinding.progressBar.visibility = View.GONE

                if (task.isSuccessful) {
                    Toast.makeText(this, getString(R.string.signup_complete), Toast.LENGTH_SHORT).show()
                    movePage(auth?.currentUser)
                } else if (task.exception?.message.isNullOrEmpty()) { // 회원가입 에러
                    Toast.makeText(this, task.exception!!.message, Toast.LENGTH_SHORT).show()
                } else { // 이미 계정이 있는 경우
                    login()
                }
            }
    }

    fun login() {
        auth?.signInWithEmailAndPassword(loginActivityBinding.emailEdittext.text.toString(), loginActivityBinding.passwordEdittext.text.toString())
            ?.addOnCompleteListener {
                task ->
                loginActivityBinding.progressBar.visibility = View.GONE

                if (task.isSuccessful) { // 로그인 성공
                    movePage(auth?.currentUser)
                } else { // 로그인 실패
                    Toast.makeText(this, task.exception!!.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun movePage(user : FirebaseUser?) {
        if (user != null) {
            firestore?.collection("UserInfo")?.document(user.uid)?.get()?.addOnCompleteListener {
                    task ->
                if (task.isSuccessful) {
                    userInformation = task.result?.toObject(UserInfo::class.java)
                    if (userInformation != null) { // 유저정보가 있는 경우
                        var intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("userinformation", userInformation)
                        startActivity(intent)
                        finish()
                    } else { // 유저정보가 없는 경우
                        var intent = Intent(this, NewUserActivity::class.java)
                        intent.putExtra("email", user.email)
                        intent.putExtra("uid", user.uid)
                        startActivity(intent)
                        finish()
                    }
                } else { // Firestore 접속 실패

                }
            }
        }
    }
}