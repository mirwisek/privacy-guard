package com.fyp.privacyguard.network.model

import com.fyp.privacyguard.data.model.LoggedInUser

data class ApiResult(
    var error: String = "",
    var result: String = ""
)

data class ApiUserResult(
    var error: String = "",
    var result: LoggedInUser = LoggedInUser()
)

//data class ApiUser(
//    var name: String = "",
//    var email: String = "",
//    var phone: String = "",
//    var password: String = ""
//)
