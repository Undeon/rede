package com.danielfonseca.rede.usuarios

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.disposables.CompositeDisposable
import br.com.youse.forms.rxform.IRxForm
import br.com.youse.forms.rxform.models.RxField
import br.com.youse.forms.rxform.RxForm
import br.com.youse.forms.validators.MinLengthValidator
import br.com.youse.forms.validators.RequiredValidator
import br.com.youse.forms.validators.Validator
import com.danielfonseca.rede.formatter.FormatadorData
import com.danielfonseca.rede.R
import com.danielfonseca.rede.formatter.FormatadorCPF
import com.danielfonseca.rede.formatter.FormatadorTelefone
import com.danielfonseca.rede.validators.*
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.textChanges
import kotlinx.android.synthetic.main.activity_cadastrar_usuario.*

class CadastrarUsuarioDadosActivity : AppCompatActivity() {

    lateinit var form: IRxForm<Int>
    val disposables = CompositeDisposable()

    val nomeValidators = listOf<Validator<String>>(
        RequiredValidator("O campo nome não pode ficar vazio.")
    )

    val dataNascimentoValidators = listOf(
        RequiredValidator("A data de nascimento não pode ficar vazio."),
        DataNascimentoValidator("A data de nascimento não é válida.")
    )

    val cpfValidators = listOf(
        RequiredValidator("O CPF não pode ficar vazio."),
        CPFValidator("O CPF não é válido.")
    )

    val telefoneValidators = listOf(
        RequiredValidator("O telefone não pode ficar vazio."),
        TelefoneValidator("O telefone não é válido. Digite um telefone no padrão (xx) xxxxx-xxxx.")
    )

    val emailValidators = listOf(
        RequiredValidator("O campo e-mail não pode ficar vazio."),
        EmailValidator("O e-mail digitado não é válido")
    )

    val passwordValidators = listOf(
        RequiredValidator("O campo senha não pode ficar vazio."),
        MinLengthValidator("A senha precisa ter pelo menos 6 caracteres", 6)
    )

    val passwordConfirmaValidators by lazy{ listOf(
        RequiredValidator("O campo senha não pode ficar vazio."),
        EqualsValidator("As senhas digitadas não são iguais. Favor verificar.", textoCadastroSenha)
    )}


    private val reference = FirebaseFirestore.getInstance().document("Rede/Usuarios")
    private val processoCadastro = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastrar_usuario)

        FormatadorData(textoCadastroDataNascimento).listen()
        FormatadorCPF(textoCadastroCPF).listen()
        FormatadorTelefone(textoCadastroTelefone).listen()

        val nomeChanges = textoCadastroNome.textChanges().map{ it.toString()}
        val dataNascimentoChanges = textoCadastroDataNascimento.textChanges().map{it.toString()}
        val cpfChanges = textoCadastroCPF.textChanges().map{it.toString()}
        val telefoneChanges = textoCadastroTelefone.textChanges().map{it.toString()}
        val emailChanges = textoCadastroEmail.textChanges().map{ it.toString()}
        val passwordChanges = textoCadastroSenha.textChanges().map{ it.toString()}
        val passwordConfirmaChanges = textoCadastroConfirmaSenha.textChanges().map{ it.toString()}

        val submitHappens = botaoCadastroCadastrar.clicks()

        val nomeField = RxField(key = layoutCadastroNome.id,
            input = nomeChanges,
            validators = nomeValidators)

        val dataNascimentoField = RxField(key = layoutCadastroDataNascimento.id,
            input = dataNascimentoChanges,
            validators = dataNascimentoValidators)

        val emailField = RxField(key = layoutCadastroEmail.id,
            input = emailChanges,
            validators = emailValidators)

        val cpfField = RxField(key = layoutCadastroCPF.id,
            input = cpfChanges,
            validators = cpfValidators)

        val telefoneField = RxField(key = layoutCadastroTelefone.id,
            input = telefoneChanges,
            validators = telefoneValidators)

        val passwordField = RxField(key = layoutCadastroSenha.id,
            input = passwordChanges,
            validators = passwordValidators)

        val passwordConfirmaField = RxField(key = layoutCadastroConfirmaSenha.id,
            input = passwordConfirmaChanges,
            validators = passwordConfirmaValidators)

        form = RxForm.Builder<Int>(submitHappens)
            .addField(nomeField)
            .addField(dataNascimentoField)
            .addField(cpfField)
            .addField(telefoneField)
            .addField(emailField)
            .addField(passwordField)
            .addField(passwordConfirmaField)
            .build()

        disposables.add(form.onFieldValidationChange()
            .subscribe{
                findViewById<TextInputLayout>(it.first)
                    .error = it.second.firstOrNull()?.message
            })

        disposables.add(form.onFormValidationChange().subscribe{
            botaoCadastroCadastrar.isEnabled = it
        })

        disposables.add(form.onValidSubmit().subscribe{

            botaoCadastroCadastrar.isEnabled = false

            val nome = textoCadastroNome.text.toString()
            val dataNascimento = textoCadastroDataNascimento.text.toString()
            val email = textoCadastroEmail.text.toString()
            val cpf = textoCadastroCPF.text.toString()
            val telefone = textoCadastroTelefone.text.toString()
            val password = textoCadastroSenha.text.toString()

            val usuario = Usuario()
            usuario.nomeCompleto = nome
            usuario.dataNascimento = dataNascimento
            usuario.email = email
            usuario.cpf = cpf
            usuario.telefone = telefone

            processoCadastro.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    reference
                        .collection(usuario.nomeCompleto.toString())
                        .document("Cadastro")
                        .set(usuario)
                        .addOnSuccessListener { void: Void? -> Toast.makeText(this, "Cadastrado inicial efetuado com sucesso.", Toast.LENGTH_LONG).show() }
                        .addOnFailureListener {
                            exception: java.lang.Exception -> println(exception.toString())

                        }
                    val intent = Intent(this, CadastrarUsuarioPassosActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else {
                    botaoCadastroCadastrar.isEnabled = true
                    if(task.exception is FirebaseAuthException){
                        val errorCode = (task.exception as FirebaseAuthException).errorCode
                        if(errorCode == "ERROR_EMAIL_ALREADY_IN_USE") {
                            Toast.makeText(this, "E-mail já cadastrado", Toast.LENGTH_LONG).show()
                        }
                        if(errorCode == "WEAK_PASSWORD"){
                            Toast.makeText(this, "A senha informada não atende os requisitos de segurança", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        })
    }
}