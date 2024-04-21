package com.example.reserhub.entities.types
import java.util.Date

interface UserData {
    var name: String
    var email: String
    var password: String
    var userName: String
    var rol: String
    var createdAt: Date
    var updatedAt: Date
}

open class UserDataImpl(
    override var name: String,
    override var email: String,
    override var password: String,
    override var userName: String,
    override var rol: String,
    override var createdAt: Date,
    override var updatedAt: Date,
) : UserData




