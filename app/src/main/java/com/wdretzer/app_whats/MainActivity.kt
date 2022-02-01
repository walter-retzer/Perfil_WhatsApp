package com.wdretzer.app_whats

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val imagePerfil: ImageView?
        get() = this.findViewById(R.id.imagem_profile)


    private val cameraCallback =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data
                data?.extras?.get("data")?.let { photo ->
                    imagePerfil?.setImageBitmap(photo as Bitmap)
                }
            }
        }

    private val galleryCallback =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val image = it.data?.data
                imagePerfil?.setImageURI(image)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Desabilita a Action Bar que exibe o nome do Projeto:
        getSupportActionBar()?.hide()

        val btn_changePhoto = findViewById<ImageView>(R.id.icone_camera)
        val imagePerfil = findViewById<ImageView>(R.id.imagem_profile)

        btn_changePhoto.setOnClickListener{
            dialogPhoto(it.context)
            Toast.makeText(this, "Clicouuu", Toast.LENGTH_LONG).show()
        }

    }


    private fun dialogPhoto(context: Context) {
        val itens = arrayOf("Tirar foto", "Buscar na Galeria")
        AlertDialog
            .Builder(context)
            .setTitle("qual você deseja usar?")
            .setItems(itens) { dialog, index ->
                when (index) {
                    0 -> getFromCamera(context)
                    1 -> getFromGallery()
                }
                dialog.dismiss()
            }.show()
    }

    private fun getFromCamera(context: Context) {
        val permission =
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)

        if (permission == PackageManager.PERMISSION_DENIED) {
            val intent = Intent().apply {
                action = MediaStore.ACTION_IMAGE_CAPTURE
            }

            cameraCallback.launch(intent)
        }
    }

    private fun getFromGallery() {
        val intent = Intent().apply {
            action = Intent.ACTION_PICK
            type = "image/*"
        }

        galleryCallback.launch(intent)
    }

}