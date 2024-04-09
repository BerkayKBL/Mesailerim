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

class ChangeWage(defaultValue: String = "", year : Int) : DialogFragment() {

    private lateinit var callback: CallbackWage
    private var year = year
    private var defaultValue = defaultValue

    private var mListener: CallbackWage? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mListener = context as CallbackWage
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement MyDialogListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_wage, container, false)

        val wageValue: EditText = view.findViewById<EditText>(R.id.changeWageValue)
        val confirm: TextView = view.findViewById<Button>(R.id.confirm)
        val cancel: TextView = view.findViewById<Button>(R.id.cancel)

        confirm.setOnClickListener {
            if (wageValue.text.isEmpty() || wageValue.text.isDigitsOnly()) {
                mListener!!.onWageChange(wageValue.text.toString().toInt(), year)
                dismiss()
            }
        }

        cancel.setOnClickListener {
            dismiss()
        }

        return view
    }
}


interface CallbackWage {
    fun onWageChange(newValue: Int, year: Int)
}