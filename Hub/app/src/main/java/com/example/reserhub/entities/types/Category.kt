package com.example.reserhub.entities.types


interface CategoryData {
    var name: String?
}

open class CategoryDataImpl(
   override var name: String?,
) : CategoryData

interface Category {

    fun getAllCategories() : List<CategoryDataImpl>
    fun getCategory(id: Int) : CategoryDataImpl
    fun createCategory(newDataImpl: UserDataImpl) : Boolean
    fun modifyCategory(id: Int, newData: UserDataImpl) : Boolean
    fun deleteCategory(id: Int) : Boolean

    // TODO: para ambos roles, edición de "mi cuenta"
}