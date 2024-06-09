package com.example.reserhub.entities.types

import java.sql.Date

interface ServiceData {
    var title: String?
    var description: String?
    var startDate: Date?
    var endDate: Date?
    var categoryId: Int
}

open class ServiceDataImpl(
    override var title: String?,
    override var description: String?,
    override var startDate: Date?,
    override var endDate: Date?,
    override var categoryId: Int

) : ServiceData

interface Service {

    fun getAllServices() : List<ServiceDataImpl>
    fun getService(id: Int) : ServiceDataImpl
    fun createService(newDataImpl: ServiceDataImpl) : Boolean
    fun modifyService(id: Int, newData: ServiceDataImpl) : Boolean
    fun deleteService(id: Int) : Boolean

    // TODO: para ambos roles, edición de "mi cuenta"
}