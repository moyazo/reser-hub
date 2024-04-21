package com.example.reserhub.entities.types

import java.sql.Date

data class Service(
    var title: String,
    var description: String,
    var startDate: java.util.Date,
    var endDate: java.util.Date,
    var createdAt: Date,
    var updatedAt: Date,
    var categoryId: Int
) {
    override fun toString(): String {
        return "Service, title: $title, description: $description, startDate: $startDate," +
                " endDate: $endDate, title: $title createdAt: $createdAt, updatedAt: $updatedAt, categoryId: $categoryId"
    }

}