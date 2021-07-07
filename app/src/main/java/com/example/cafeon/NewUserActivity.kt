package com.example.cafeon

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.cafeon.datamodel.UserInfo
import com.google.firebase.firestore.FirebaseFirestore

class NewUserActivity : AppCompatActivity() {

    var firestore: FirebaseFirestore? = null
    var userId: String? = null
    var userInformation: UserInfo? = null // 유저 정보가 담기는 데이터 클래스

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firestore = FirebaseFirestore.getInstance() // Firestore 객체 생성
        userId = intent.getStringExtra("uid")

        showDialog() // 닉네임 정하기
    }

    fun showDialog() {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.dialog_userinfo, null)

        var alertDialog = AlertDialog.Builder(this).setTitle("처음이시군요? 사용하실 닉네임을 입력해주세요.")
            .setPositiveButton("확인") {
                    dialog, which ->
                var textView: EditText = view.findViewById(R.id.input_nickname)
                var nickName: String = textView.text.toString()
                userInformation = UserInfo(intent.getStringExtra("email"), nickName)

                firestore?.collection("UserInfo")?.document(userId.toString())?.set(userInformation!!)?.addOnCompleteListener {
                        task ->
                    if (task.isSuccessful) { // 데이터 생성 성공 -> 메인 페이지로
                        var intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("userinformation", userInformation)
                        startActivity(intent)
                        finish()
                    }
                }
            }
            .create()

        alertDialog.setCancelable(false)
        alertDialog.setView(view)
        alertDialog.show()
    }
}