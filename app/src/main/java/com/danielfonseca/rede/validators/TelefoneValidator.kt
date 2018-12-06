package com.danielfonseca.rede.validators

import br.com.youse.forms.validators.ValidationMessage
import br.com.youse.forms.validators.ValidationTypes
import br.com.youse.forms.validators.Validator

class TelefoneValidator(val message: String) : Validator<String> {

    private val validationMessage = ValidationMessage(message = message, validationType = ValidationTypes.MIN_LENGTH)

    override fun validationMessage(): ValidationMessage {
        return validationMessage
    }

    override fun isValid(telefone: String?): Boolean {
        telefone?.let {
            val telefonePattern = "^\\([1-9][1-9]\\) 9[6-9][0-9]{3}-[0-9]{4}\$"
            val telefoneMatcher = Regex(telefonePattern)

            return telefoneMatcher.find(telefone) != null
        } ?: return false
    }
}