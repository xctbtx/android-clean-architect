package com.xctbtx.cleanarchitectsample.data.user

import com.xctbtx.cleanarchitectsample.data.api.FireStoreApiService
import com.xctbtx.cleanarchitectsample.data.user.mapper.UserMapper.toDomain
import com.xctbtx.cleanarchitectsample.domain.user.model.User
import com.xctbtx.cleanarchitectsample.domain.user.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: FireStoreApiService,
) : UserRepository {

    override suspend fun getUserById(id: String): User {
        val response = api.getUser(id)
        return response.toDomain()
    }
}