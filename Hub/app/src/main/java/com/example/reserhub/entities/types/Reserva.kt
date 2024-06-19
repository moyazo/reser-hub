package com.example.reserhub.entities.types

interface ReservaData {
    var userId: String?
    var serviceId: Int?
}

open class ReservaDataImpl(
    override var userId: String?,
    override var serviceId: Int?
) : ReservaData

interface Reserva {

    fun getAllReserva() : List<ReservaDataImpl>
    fun getAllReservaOfUser(userId: String) : List<ServiceDataImpl>
    fun getReserva(id: Int, userId: Int) : ReservaDataImpl
    fun createReserva(newDataImpl: ReservaDataImpl) : Boolean
    fun modifyReserva(id: Int, userId: Int, newData: ReservaDataImpl) : Boolean
    fun deleteReserva(id: Int,userId: Int) : Boolean

}