package com.ts.alex.bestforecast.device.job

import android.content.Intent
import com.evernote.android.job.Job
import com.evernote.android.job.JobCreator
import com.evernote.android.job.JobManager
import com.evernote.android.job.JobRequest
import com.ts.alex.bestforecast.device.notification.Notification
import com.ts.alex.bestforecast.device.service.ForecastJobIntentService
import com.ts.alex.bestforecast.ui.MyApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


class ForecastJobCreator : JobCreator {

    override fun create(tag: String): Job? {
        return when (tag) {
            SyncJob.TAG -> SyncJob()
            else -> null
        }
    }
}

class SyncJob() : Job() {

    override fun onRunJob(params: Params): Result {
        Notification(context).start(
            MyApp.CHANNEL_ID,
            "Update weather",
            "Scheduled weather forecast update"
        )
        val serviceIntent = Intent(context, ForecastJobIntentService::class.java)
        ForecastJobIntentService.enqueueWork(context, serviceIntent)

        return Result.SUCCESS
    }

    companion object {
        const val TAG = "job_demo_tag"
        private var jobId: Int = -1

        fun startJob(min: Long) {
            jobId = JobRequest.Builder(SyncJob.TAG)
                .setPeriodic(TimeUnit.MINUTES.toMillis(min))
                .build()
                .schedule()
        }

        fun cancelJob() {
            JobManager.instance().cancel(jobId)
        }
    }
}

