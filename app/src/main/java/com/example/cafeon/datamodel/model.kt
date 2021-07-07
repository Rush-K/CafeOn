package com.example.cafeon.datamodel

import java.io.Serializable

data class UserInfo (
    var email:String? = null,
    var nickname:String? = null
) : Serializable