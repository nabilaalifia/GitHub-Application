package com.dicoding.githubapp

import android.content.Context
import com.cioccarellia.ksprefs.KsPrefs

object KsPref {

    private lateinit var appContext: Context
    private val prefs by lazy { KsPrefs(appContext) }

    fun instance(context: Context) {
        appContext = context
    }

    private const val DAILY_REMINDER = "daily_reminder_key"
    fun setDailyReminder() {

        val isActive = prefs.pull(DAILY_REMINDER, false)
        prefs.push(DAILY_REMINDER, !isActive)
        prefs.save()

    }

    fun isDailyReminderActive() = prefs.pull(DAILY_REMINDER, false)
}