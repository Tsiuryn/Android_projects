package com.ts.alex.domain.usecase

import com.ts.alex.domain.model.User

interface IUserUseCase {
    fun getUser(): User
    fun saveUser(user: User)
}