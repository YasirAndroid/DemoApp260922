package com.demo.demoapp

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.demo.demoapp.databinding.ActivityLoginBinding
import com.demo.demoapp.databinding.ActivityRegisterBinding
import com.demo.demoapp.viewmodel.MainViewModel
import com.octel.crysta.utils.Status
import java.io.File
import java.io.FileOutputStream

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    lateinit var filePath: String
    lateinit var fileUri: Uri

    var pickPhoto = registerForActivityResult(
    ActivityResultContracts.GetContent(),
    ActivityResultCallback {
            filePath = uriToFile(this, it!!, "temp_image").toString()
            fileUri = it
        }
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.result.observe(this) {
            when(it.status) {
                Status.SUCCESS -> startActivity(Intent(this, MainActivity::class.java))
                Status.ERROR -> Toast.makeText(this, "Error with server", Toast.LENGTH_LONG).show()
                Status.LOADING -> Toast.makeText(this, "Error with server", Toast.LENGTH_LONG).show()
                Status.FAILED -> startActivity(Intent(this, RegisterActivity::class.java))
            }
        }
        binding.btnPicklogo.setOnClickListener {
             pickImageScopedStorage()
        }

        binding.btnSignup.setOnClickListener {
            val firstname = binding.etPassSignup.text
            val lastname = binding.etPassSignup.text
            val email = binding.etEmailSignup.text
            val pass = binding.etPassSignup.text
            val conpass = binding.etPassSignup.text
            val phone = binding.etPassSignup.text
            val address = binding.etPassSignup.text
            val city = binding.etPassSignup.text
            val state = binding.etPassSignup.text
            val postal = binding.etPassSignup.text
            if (email.isNotEmpty() && pass.isNotEmpty() && firstname.isNotEmpty() && lastname.isNotEmpty() && conpass.isNotEmpty() && phone.isNotEmpty() && address.isNotEmpty() && city.isNotEmpty() && state.isNotEmpty() && postal.isNotEmpty()){
                viewModel.Signup(firstname.toString(), lastname.toString(), email.toString(), pass.toString(), conpass.toString(), phone.toString(), address.toString(), city.toString(), state.toString(), postal.toString(), filePath)
            }
        }
    }
    private fun pickImageScopedStorage() {

        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            pickPhoto.launch("image/*")

        } else {
            Toast.makeText(this, "Read Permission is not Granted", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createImageFile(filename: String = "temp_image"): File {
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(filename, ".jpg", storageDir)
    }

    private fun uriToFile(context: Context, uri: Uri, filename: String): File? {
        context.contentResolver.openInputStream(uri).let {
            val tempFile = createImageFile(filename)
            val fileOutputStream = FileOutputStream(tempFile)
            it!!.copyTo(fileOutputStream)
            it.close()
            fileOutputStream.close()
            return tempFile
        }
        return null
    }

}