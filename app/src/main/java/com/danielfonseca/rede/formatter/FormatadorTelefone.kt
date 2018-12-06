package com.danielfonseca.rede.formatter

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class FormatadorTelefone(val input : EditText) {

    fun listen() {
        input.addTextChangedListener(mDateEntryWatcher)
    }

    private val mDateEntryWatcher = object : TextWatcher {

        var edited = false
        val abreParentesesCaracter = "("
        val fechaParentesesCaracter = ") "
        val tracoCaracter = "-"

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (edited) {
                edited = false
                return
            }

            var working = getEditText()

            working = abreParentesesCaracter(working, 1, start, before)
            working = fechaParentesesCaracter(working, 3, start, before)
            working = tracoCaracter(working, 10, start, before)

            edited = true
            input.setText(working)
            input.setSelection(input.text.length)
        }

        private fun abreParentesesCaracter(working: String, position : Int, start: Int, before: Int) : String{
            if (working.length == position) {
                return if (before <= position && start < position)
                    abreParentesesCaracter + working
                else
                    working.dropLast(1)
            }
            return working
        }

        private fun fechaParentesesCaracter(working: String, position : Int, start: Int, before: Int) : String{
            if (working.length == position) {
                return if (before <= position && start < position)
                    working + fechaParentesesCaracter
                else
                    working.dropLast(1)
            }
            return working
        }

        private fun tracoCaracter(working: String, position : Int, start: Int, before: Int) : String{
            if (working.length == position) {
                return if (before <= position && start < position)
                    working +tracoCaracter
                else
                    working.dropLast(1)
            }
            return working
        }

        private fun getEditText() : String {
            return if (input.text.length >= 15)
                input.text.toString().substring(0,15)
            else
                input.text.toString()
        }

        override fun afterTextChanged(s: Editable) {}
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    }
}