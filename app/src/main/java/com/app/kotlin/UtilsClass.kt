package com.app.kotlin

import android.content.Context
import io.branch.referral.util.BRANCH_STANDARD_EVENT
import io.branch.referral.util.BranchEvent

object UtilsClass {


    fun getRandomId(): String {
        return Math.random().toString()

    }

    fun addBranchEvent(
        context: Context,
        event: BRANCH_STANDARD_EVENT,
        propertyName: String,
        propertyValue: String
    ) {
        BranchEvent(event)
            .addCustomDataProperty(propertyName, propertyValue)
            .logEvent(context)
    }

    fun addBranchEvent(
        context: Context,
        event: String,
        propertyName: String,
        propertyValue: String
    ) {
        BranchEvent(event)
            .addCustomDataProperty(propertyName, propertyValue)
            .logEvent(context)
    }

}