package com.dicoding.githubapp

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.githubapp.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.llSetLanguage.setOnClickListener {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }

        KsPref.instance(this)

        binding.swDaily.apply {
            isChecked = KsPref.isDailyReminderActive()
            setOnCheckedChangeListener { _, isChecked ->
                KsPref.setDailyReminder()
                setAlarm(isChecked)
            }
        }
    }

    private fun setAlarm(activeIt: Boolean) {
        if (activeIt) {
            setDailyReminder()
        } else {
            removeDailyReminder()
        }
    }


    private val alarmReceiver = AlarmReceiver()

    private fun setDailyReminder() {
        alarmReceiver.setRepeatingAlarm(
            this,
            AlarmReceiver.TYPE_REPEATING,
            "9:00",
            "Ayo Cari User Github"
        ) {
            Toast.makeText(
                this,
                "Daily Reminder Active",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun removeDailyReminder() {
        alarmReceiver.cancelAlarm(
            this,
            AlarmReceiver.TYPE_REPEATING
        ) {
            Toast.makeText(
                this,
                "Berhasil diremove",
                Toast.LENGTH_SHORT
            ).show()

        }
    }
}