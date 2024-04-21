package com.example.reserhub.entities.types

import java.sql.Date

data class Category(
    var name: String,
    var createdAt: Date,
    var updatedAt: Date
) {
    override fun toString(): String {
        return "Category, name: $name, createdAt: $createdAt, updatedAt: $updatedAt"
    }

}