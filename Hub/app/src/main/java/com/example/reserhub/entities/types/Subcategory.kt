package com.example.reserhub.entities.types

import java.sql.Date

data class Subcategory(var name: String,
                       var createdAt: Date,
                       var updatedAt: Date,
                       var categoryId: Int
) {
    override fun toString(): String {
        return "Subcategory, name: $name, createdAt: $createdAt, updatedAt: $updatedAt"
    }

}