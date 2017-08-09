package com.mgb.randomwallpaper.dialogs

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.mgb.randomwallpaper.R
import com.mgb.randomwallpaper.database.ChannelModel
import kotlinx.android.synthetic.main.dialog_add_channel.*

/**
 * Created by mgradob on 6/5/17.
 */
class AddChannelDialog(val onOkClicked: (channel: ChannelModel) -> Unit) : DialogFragment() {

    override fun onStart() {
        super.onStart()

        dialog.window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater?.inflate(R.layout.dialog_add_channel, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        okButton.setOnClickListener {
            onOkClicked(ChannelModel(channelName.text.toString(), channelId.text.toString().toLong(), 1))
            dismiss()
        }

        cancelButton.setOnClickListener { dismiss() }
    }
}