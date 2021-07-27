package com.example.cafeon.datamodel

import java.io.Serializable

data class UserInfo (
    var email:String? = null,
    var nickname:String? = null,
    var followers:MutableMap<String, Boolean> = HashMap(),
    var followings:MutableMap<String, Boolean> = HashMap()
) : Serializable