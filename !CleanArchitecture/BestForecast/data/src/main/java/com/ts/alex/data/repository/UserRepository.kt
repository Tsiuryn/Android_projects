package com.ts.alex.data.repository

import android.util.Log
import com.google.gson.Gson
import com.ts.alex.data.datasource.IUserDataSource
import com.ts.alex.domain.model.User
import com.ts.alex.domain.usecase.IUserUseCase

class UserRepository(private val iUserDataSource: IUserDataSource): IUserUseCase {

    override fun getUser(): User {
        val userString = iUserDataSource.getUser()
        return Gson().fromJson(userString, User::class.java)
    }

    override fun saveUser(user: User) {
        iUserDataSource.saveUser(Gson().toJson(user))
        Log.d("TAG11", "saveUser: ${Gson().toJson(user)}")
    }
}