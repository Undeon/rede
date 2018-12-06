package com.danielfonseca.rede.usuarios

import android.content.ContentValues
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AlertDialog
import com.danielfonseca.rede.R

class CadastrarUsuarioPassosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastrar_usuario_passos)

        val dialog = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.mensagem_layout, null)
        dialog.setView(dialogView)
        dialog.setCancelable(false)
        dialog.setPositiveButton("OK", {dialog2, _ ->})
        dialog.show()

//        fun launchCamera() {
//            val values = ContentValues(1)
//            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
//            val fileUri = contentResolver
//                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                    values)
//            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            if(intent.resolveActivity(packageManager) != null) {
//                mCurrentPhotoPath = fileUri.toString()
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
//                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
//                        or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
//                startActivityForResult(intent, TAKE_PHOTO_REQUEST)
//            }
//        }
    }
}
