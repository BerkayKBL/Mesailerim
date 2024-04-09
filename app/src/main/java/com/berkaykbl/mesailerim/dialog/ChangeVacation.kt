package com.berkaykbl.mesailerim.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.DialogFragment
import com.berkaykbl.mesailerim.R

class ChangeVacation(defaultVal: String = "", incomingDate: String) : DialogFragment() {

    private lateinit var callback: CallbackShift
    private var defaultValue = defaultVal
    private var date = incomingDate

    private var mListener: CallbackVacation? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mListener = context as CallbackVacation
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement MyDialogListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_vacation, container, false)

        val radioGroup: RadioGroup = view.findViewById<RadioGroup>(R.id.radioGroup)
        val confirm: TextView = view.findViewById<Button>(R.id.confirm)
        val cancel: TextView = view.findViewById<Button>(R.id.cancel)
        var clickedText = ""
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val radioButton : RadioButton = view.findViewById(checkedId)
            clickedText = radioButton.text.toString()
        }
        confirm.setOnClickListener {
                mListener!!.onVacationChange(clickedText, date)
                dismiss()
        }

        cancel.setOnClickListener {
            dismiss()
        }

        return view
    }
}


interface CallbackVacation {
    fun onVacationChange(newValue: String, date: String)
}