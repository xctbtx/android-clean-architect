package com.xctbtx.cleanarchitectsample.data.user.mapper

import com.xctbtx.cleanarchitectsample.data.user.dto.UserDto
import com.xctbtx.cleanarchitectsample.domain.user.model.User

object UserMapper {
    fun UserDto.toDomain(): User {
        return User(id = this.id, name = name, avatar = avatar, dob = dob)
    }
}