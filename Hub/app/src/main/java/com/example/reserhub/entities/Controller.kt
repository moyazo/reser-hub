package com.example.reserhub.entities

import com.example.reserhub.entities.types.Category
import com.example.reserhub.entities.types.Reserva
import com.example.reserhub.entities.types.Service
import com.example.reserhub.entities.types.SubCategory
import com.example.reserhub.entities.types.User


interface Controller: User, Category, Service, Reserva, SubCategory

