package com.mgb.randomwallpaper.dialogs

import android.app.AlarmManager
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.mgb.randomwallpaper.R
import kotlinx.android.synthetic.main.dialog_select_interval.*

/**
 * Created by mgradob on 6/5/17.
 */
class SelectIntervalDialog(val initialInterval: Long, val onOkClicked: (interval: Long) -> Unit) : DialogFragment() {

    private var interval: Long = AlarmManager.INTERVAL_FIFTEEN_MINUTES

    override fun onStart() {
        super.onStart()

        dialog.window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater?.inflate(R.layout.dialog_select_interval, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        intervalRadioGroup.check(when (initialInterval) {
            AlarmManager.INTERVAL_FIFTEEN_MINUTES -> R.id.intervalFifteenMins
            AlarmManager.INTERVAL_HALF_HOUR -> R.id.intervalHalfHour
            AlarmManager.INTERVAL_HOUR -> R.id.intervalHour
            AlarmManager.INTERVAL_HALF_DAY -> R.id.intervalHalfDay
            AlarmManager.INTERVAL_DAY -> R.id.intervalDay
            else -> R.id.intervalFifteenMins
        })

        intervalRadioGroup.setOnCheckedChangeListener { radioGroup, i ->
            interval = when (i) {
                R.id.intervalFifteenMins -> AlarmManager.INTERVAL_FIFTEEN_MINUTES
                R.id.intervalHalfHour -> AlarmManager.INTERVAL_HALF_HOUR
                R.id.intervalHour -> AlarmManager.INTERVAL_HOUR
                R.id.intervalHalfDay -> AlarmManager.INTERVAL_HALF_DAY
                R.id.intervalDay -> AlarmManager.INTERVAL_DAY
                else -> AlarmManager.INTERVAL_FIFTEEN_MINUTES
            }
        }

        okButton.setOnClickListener {
            onOkClicked(interval)
            dismiss()
        }

        cancelButton.setOnClickListener { dismiss() }
    }
}