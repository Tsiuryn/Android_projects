package com.ts.alex.bestforecast.device.service

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import com.ts.alex.bestforecast.device.job.SyncJob
import com.ts.alex.data.network.providePlaceHolderApi
import com.ts.alex.domain.usecase.IGetForecastUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class ForecastJobIntentService : JobIntentService() {

    override fun onHandleWork(intent: Intent) {
        GlobalScope.launch (Dispatchers.IO){
            val retro = providePlaceHolderApi()
            retro.getWeatherByCity("Minsk")
            SyncJob.cancelJob()
        }
    }

    companion object {
        fun enqueueWork(context: Context, work: Intent?) {
            enqueueWork(context, ForecastJobIntentService::class.java, 123, work!!)
        }
    }
}
