package com.example.cafeon

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cafeon.databinding.FragmentProfileBinding
import com.example.cafeon.datamodel.UserInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {
    // Firebase
    var auth: FirebaseAuth? = null
    var firestore: FirebaseFirestore? = null

    // uid 분류
    var uid: String? = null // 사용자
    var currentUserUid: String? = null // 나

    var uidUserInfo: UserInfo? = null // 프로필 요청 사용자의 정보

    var profileFragMentView: FragmentProfileBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileFragMentView = FragmentProfileBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        currentUserUid = auth?.currentUser?.uid

        if (arguments != null) {

            uid = arguments?.getString("requestedUid") // 프로필 요청된 사용자 UID 받아오기
            uidUserInfo = arguments?.getSerializable("uidUserInfo") as UserInfo

            if (uid == currentUserUid) { // 내 프로필을 볼 경우
                profileFragMentView!!.emailProfile.text = uidUserInfo!!.email
                profileFragMentView!!.nicknameProfile.text = uidUserInfo!!.nickname
                profileFragMentView!!.buttonUpload.text = getString(R.string.profile_image)
            } else { // 다른 사용자의 프로필을 볼 경우
                //profileFragMentView!!.buttonUpload.text = getString(R.string.profile_image)
            }

        }

        return profileFragMentView!!.root
    }
}