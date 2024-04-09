package com.berkaykbl.mesailerim.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.DialogFragment
import com.berkaykbl.mesailerim.R
import com.berkaykbl.mesailerim.setDecimalInputFilter

class ChangeShift(defaultVal: Int = 0, incomingDate: String) : DialogFragment() {

    private lateinit var callback: CallbackShift
    private var defaultValue = defaultVal
    private var date = incomingDate

    private var mListener: CallbackShift? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mListener = context as CallbackShift
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement MyDialogListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_shift, container, false)

        val shiftValue: EditText = view.findViewById<EditText>(R.id.shiftValue)
        setDecimalInputFilter(shiftValue, 2, 1)
        val confirm: TextView = view.findViewById<Button>(R.id.confirm)
        val cancel: TextView = view.findViewById<Button>(R.id.cancel)

        if (defaultValue != 0) {
            shiftValue.setText(defaultValue.toString())
        }
        confirm.setOnClickListener {
            if (shiftValue.text.isEmpty() || shiftValue.text.isDigitsOnly()) {
                mListener!!.onShiftChange(shiftValue.text.toString(), date)
                dismiss()
            }
        }

        cancel.setOnClickListener {
            dismiss()
        }

        return view
    }
}


interface CallbackShift {
    fun onShiftChange(newValue: String, date: String)
}