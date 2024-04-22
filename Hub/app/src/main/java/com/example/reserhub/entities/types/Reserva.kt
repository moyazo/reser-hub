package com.example.reserhub.entities.types

interface ReservaData {
    var userId: Int?
    var serviceId: Int?
}

open class ReservaDataImpl(
    override var userId: Int?,
    override var serviceId: Int?
) : ReservaData

interface Reserva {

    fun getAllReserva() : List<ReservaDataImpl>
    fun getAllReservaOfUser(userId: Int) : List<ReservaDataImpl>
    fun getReserva(id: Int, userId: Int) : ReservaDataImpl
    fun createReserva(newDataImpl: ReservaDataImpl) : Boolean
    fun modifyReserva(id: Int, userId: Int, newData: ReservaDataImpl) : Boolean
    fun deleteReserva(id: Int,userId: Int) : Boolean

    // TODO: para ambos roles, edición de "mi cuenta"
}