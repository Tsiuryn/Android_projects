package com.ts.alex.kotlincleanproject

import android.app.Application
import com.ts.alex.kotlincleanproject.modules.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(applicationContext)
            androidLogger(Level.DEBUG)
            modules(listOf(viewModel, restApi, dataSource, repository))
        }
    }
}