package com.app.kotlin

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.branch.referral.Branch
import io.branch.referral.validators.IntegrationValidator


@HiltAndroidApp
class ApplicationClass : Application() {

    override fun onCreate() {
        super.onCreate()
        Branch.enableLogging()
        Branch.getAutoInstance(this)


    }


}