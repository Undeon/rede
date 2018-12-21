package com.danielfonseca.rede.users

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Toast
import com.danielfonseca.rede.R
import kotlinx.android.synthetic.main.activity_cadastrar_usuario_fotos.*

class CadastrarUsuarioPassosActivity : AppCompatActivity(), View.OnClickListener {

    private val CAMERA_REQUEST_CODE = 12345
    private val REQUEST_GALLERY_CAMERA = 54654

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastrar_usuario_fotos)

        val dialog = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.mensagem_layout, null)
        dialog.setView(dialogView)
        dialog.setCancelable(false)
        dialog.setPositiveButton("OK", {dialog2, _ ->})
        dialog.show()

        botaoSelfieRG.setOnClickListener(this)
        botaoFrenteRG.setOnClickListener(this)
        botaoVersoRG.setOnClickListener(this)
    }

    fun onClick(){
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_GALLERY_CAMERA)
            }
            else {
                openCamera()
            }
        }
        else {
            openCamera()
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null)
            startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_GALLERY_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                Toast.makeText(this@CadastrarUsuarioPassosActivity, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {

                    val extras = data?.getExtras()
                    val imageBitmap = extras?.get("data") as Bitmap
                    botaoSelfieRG.setImageBitmap(imageBitmap)
                    botaoSelfieRG.background.

                }
            }
        }
    }
}
