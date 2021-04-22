package com.ts.alex.data.datasource

interface IUserDataSource {
    fun getUser(): String
    fun saveUser(user: String)
}