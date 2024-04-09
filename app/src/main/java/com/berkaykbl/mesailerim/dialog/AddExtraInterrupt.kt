package com.berkaykbl.mesailerim.dialog

import android.annotation.SuppressLint
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

class AddExtraInterrupt(defaultVal: Int = 0, incomingDate: String, type: String) : DialogFragment() {

    private lateinit var callback: CallbackExtraInterrupt
    private var defaultValue = defaultVal
    private var date = incomingDate
    private var type = type

    private var mListener: CallbackExtraInterrupt? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mListener = context as CallbackExtraInterrupt
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement MyDialogListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_extra_interrupt, container, false)

        val extraInterruptValue: EditText = view.findViewById(R.id.extraInterruptValue)
        val confirm: TextView = view.findViewById<Button>(R.id.confirm)
        val cancel: TextView = view.findViewById<Button>(R.id.cancel)

        if (defaultValue != 0) {
            extraInterruptValue.setText(defaultValue.toString())
        }
        confirm.setOnClickListener {
            if (extraInterruptValue.text.isEmpty()) {
                dismiss()
            }else if ( extraInterruptValue.text.isDigitsOnly()) {
                    mListener!!.onExtraInterruptChange(extraInterruptValue.text.toString(), date, type)
                    dismiss()
                }

        }

        cancel.setOnClickListener {
            dismiss()
        }

        return view
    }
}



interface CallbackExtraInterrupt {
    fun onExtraInterruptChange(newValue: String, date: String, type: String)
}