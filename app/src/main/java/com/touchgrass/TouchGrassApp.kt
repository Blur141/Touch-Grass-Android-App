package com.touchgrass

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.touchgrass.utils.NotificationHelper
import com.touchgrass.worker.DailyResetWorker
import com.touchgrass.worker.UsageStatsWorker
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class TouchGrassApp : Application(), Configuration.Provider {

    @Inject lateinit var workerFactory: HiltWorkerFactory
    @Inject lateinit var notificationHelper: NotificationHelper

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        notificationHelper.createChannels()
        scheduleBackgroundWork()
    }

    private fun scheduleBackgroundWork() {
        val workManager = WorkManager.getInstance(this)

        workManager.enqueueUniquePeriodicWork(
            UsageStatsWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            UsageStatsWorker.buildRequest(),
        )

        workManager.enqueueUniquePeriodicWork(
            DailyResetWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            DailyResetWorker.buildRequest(),
        )
    }
}
