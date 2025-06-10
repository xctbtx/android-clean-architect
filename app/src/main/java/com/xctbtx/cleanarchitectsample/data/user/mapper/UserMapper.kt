package com.xctbtx.cleanarchitectsample.data.user.mapper

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.xctbtx.cleanarchitectsample.data.user.dto.UserDto
import com.xctbtx.cleanarchitectsample.domain.user.model.User

object UserMapper {
    fun UserDto.toDomain(): User {
        return User(id = this.id, name = name, avatar = avatar, dob = dob)
    }

    fun QuerySnapshot.toUsersDto(): List<UserDto>? {
        val result: List<UserDto>? = try {
            this.map { it.toObject(UserDto::class.java).copy(id = it.id) }
        } catch (e: AssertionError) {
            null
        }
        return result
    }

    fun DocumentSnapshot.toUserDto(): UserDto {
        return this.toObject(UserDto::class.java)?.copy(id = id) ?: UserDto()
    }
}