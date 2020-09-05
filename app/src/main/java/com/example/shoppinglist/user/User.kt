package com.example.shoppinglist.user

import com.example.shoppinglist.R

class User (){
    var displayName = String()
    var email = String()
    var avatar = 0
    var online = false
    var personalGroupName = String()
    var sharedGroupName = String()

    constructor(displayName: String, email: String, avatar: Int): this(){
        this.displayName = displayName
        this.email = email
        this.avatar = avatar
    }

    override fun equals(other: Any?): Boolean {
        super.equals(other)
        val ot = other as User
        return (this.displayName == ot.displayName) && (this.email == ot.email)
    }
}