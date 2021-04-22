package com.ts.alex.data.datasource.impl

import com.ts.alex.data.datasource.IUserDataSource
import com.ts.alex.data.local.prererences.SharedPreference

class UserDataSource(private val sharedPreference: SharedPreference): IUserDataSource {
    override fun getUser(): String {
        return sharedPreference.getUser()?: ""
    }

    override fun saveUser(user: String){
        sharedPreference.saveUser(user)
    }


}