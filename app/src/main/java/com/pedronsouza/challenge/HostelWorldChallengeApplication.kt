package com.pedronsouza.challenge

import android.app.Application
import timber.log.Timber

class HostelWorldChallengeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}