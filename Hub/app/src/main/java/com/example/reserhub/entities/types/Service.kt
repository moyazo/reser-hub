package com.example.reserhub.entities.types

import java.sql.Date

interface ServiceData {
    var id: Int
    var title: String?
    var description: String?
    var startDate: String?
    var endDate: String?
    var categoryId: Int
    var userId: Int
}

open class ServiceDataImpl(
    override var id: Int,
    override var title: String?,
    override var description: String?,
    override var startDate: String?,
    override var endDate: String?,
    override var categoryId: Int,
    override var userId: Int

) : ServiceData

interface Service {

    fun getAllServices() : List<ServiceDataImpl>
    fun getService(id: Int) : ServiceDataImpl
    fun getServicesByUser(userId: Int?) : List<ServiceDataImpl>
    fun createService(newDataImpl: ServiceDataImpl) : Boolean
    fun modifyService(id: Int, newData: ServiceDataImpl) : Boolean
    fun deleteService(id: Int) : Boolean

    // TODO: para ambos roles, edición de "mi cuenta"
}