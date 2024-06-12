package com.example.reserhub.entities.types


interface CategoryData {
    var id: Int?
    var name: String?
}

open class CategoryDataImpl(
    override var id: Int?,
   override var name: String?,
) : CategoryData

interface Category {

    fun getAllCategories() : List<CategoryDataImpl>
    fun getCategory(id: Int) : CategoryDataImpl
    fun createCategory(newDataImpl: CategoryDataImpl) : Boolean
    fun modifyCategory(id: Int, newData: CategoryDataImpl) : Boolean
    fun deleteCategory(id: Int) : Boolean

    // TODO: para ambos roles, edición de "mi cuenta"
}