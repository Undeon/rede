package com.danielfonseca.rede.formatter

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class FormatadorCPF(val input : EditText) {

    fun listen(){
        input.addTextChangedListener(mCPFEntryWatcher)
    }

    private val mCPFEntryWatcher = object : TextWatcher {

        var edited = false
        var pontoCharacter = "."
        var tracoCharacter = "-"

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (edited) {
                edited = false
                return
            }

            var working = getEditText()

            working = manageCPFPonto(working, 3, start, before)
            working = manageCPFPonto(working, 7, start, before)
            working = manageCPFTraco(working, 11, start, before)

            edited = true
            input.setText(working)
            input.setSelection(input.text.length)
        }
        private fun manageCPFPonto(working: String, position : Int, start: Int, before: Int) : String{
            if (working.length == position) {
                return if (before <= position && start < position)
                    working + pontoCharacter
                else
                    working.dropLast(1)
            }
            return working
        }

        private fun manageCPFTraco(working: String, position : Int, start: Int, before: Int) : String{
            if (working.length == position) {
                return if (before <= position && start < position)
                    working + tracoCharacter
                else
                    working.dropLast(1)
            }
            return working
        }

        private fun getEditText() : String {
            return if (input.text.length >= 14)
                input.text.toString().substring(0,14)
            else
                input.text.toString()
        }

        override fun afterTextChanged(s: Editable) {}
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    }
}