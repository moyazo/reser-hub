package com.example.reserhub.entities.types


interface SubCategoryData {
    var name: String?
}

open class SubCategoryDataImpl(
    override var name: String?,
) : SubCategoryData

interface SubCategory {

    fun getAllSubCategories() : List<SubCategoryDataImpl>
    fun getAllSubCategoriesOfCat(categoryId: Int) : List<SubCategoryDataImpl>
    fun getSubCategory(id: Int) : SubCategoryDataImpl
    fun createSubCategory(newDataImpl: SubCategoryDataImpl) : Boolean
    fun modifySubCategory(id: Int, newData: SubCategoryDataImpl) : Boolean
    fun deleteSubCategory(id: Int) : Boolean

    // TODO: para ambos roles, edición de "mi cuenta"
}