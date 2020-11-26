package study.android.foodrecipes.utils

import com.google.common.util.concurrent.ListeningExecutorService
import com.google.common.util.concurrent.MoreExecutors
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

object AppExecutors {

    val scheduledExecutorService : ScheduledExecutorService = Executors.newScheduledThreadPool(3)

    val networkIo: ListeningExecutorService =
        MoreExecutors.listeningDecorator(scheduledExecutorService)
}