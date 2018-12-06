package com.danielfonseca.rede.validators

import br.com.youse.forms.validators.ValidationMessage
import br.com.youse.forms.validators.ValidationTypes
import br.com.youse.forms.validators.Validator

class DataNascimentoValidator(val message: String) : Validator<String> {

    private val validationMessage = ValidationMessage(message = message, validationType = ValidationTypes.MIN_LENGTH)

    override fun validationMessage(): ValidationMessage {
        return validationMessage
    }

    override fun isValid(dataAniversario: String?): Boolean {
        dataAniversario?.let {
            val dataAniversarioPattern = "^([0-2][0-9]|(3)[0-1])(/)(((0)[0-9])|((1)[0-2]))(/)\\d{4}\$"
            val dataAniversarioMatcher = Regex(dataAniversarioPattern)

            return dataAniversarioMatcher.find(dataAniversario) != null
        } ?: return false
    }
}
