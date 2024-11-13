package com.example.trajanmarket.data.repository

import com.example.trajanmarket.data.local.UserDao
import com.example.trajanmarket.data.model.UserEntity
import javax.inject.Inject

class UserRepository @Inject constructor(private val userDao: UserDao) {
    suspend fun insertUser(userEntity: UserEntity) = userDao.insert(userEntity)
    suspend fun getUserById(id: Int) = userDao.getUserById(id)
}