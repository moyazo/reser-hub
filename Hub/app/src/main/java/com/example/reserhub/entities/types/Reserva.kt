package com.example.reserhub.entities.types

import java.sql.Date

data class Reserva(
    var userId: Int,
    var serviceId: Int,
    var createdAt: Date,
    var updatedAt: Date,
) {
    override fun toString(): String {
        return "Service, userId: $userId, serviceId: $serviceId createdAt: $createdAt, updatedAt: $updatedAt"
    }
}