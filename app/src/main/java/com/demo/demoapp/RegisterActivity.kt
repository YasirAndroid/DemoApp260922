package com.demo.demoapp

import android.app.ProgressDialog
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
    private var filePath: String? = null
    private lateinit var fileUri: Uri
    private lateinit var progress: ProgressDialog

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

        progress = ProgressDialog(this)
        progress.setTitle("Registering..")

        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.resultSignup.observe(this) {
            when(it.status) {
                Status.SUCCESS -> {
                    progress.dismiss()
                    Intent(this, MainActivity::class.java).also { intent ->
                        startActivity(intent)
                    }
                }
                Status.ERROR -> {
                    progress.dismiss()
                    Toast.makeText(this, "Error with server", Toast.LENGTH_LONG).show()
                }
                Status.FAILED ->  {
                    progress.dismiss()
                    Toast.makeText(this, "${it.message}", Toast.LENGTH_LONG).show()
                }
                Status.LOADING -> {

                }
            }
        }
        binding.btnPicklogo.setOnClickListener {
             pickImageScopedStorage()
        }

        binding.btnSignup.setOnClickListener {
            val firstname = binding.etFirstname.text
            val lastname = binding.etLastname.text
            val email = binding.etEmailSignup.text
            val pass = binding.etPassSignup.text
            val conpass = binding.etConpassSignup.text
            val phone = binding.etPhone.text
            val address = binding.etAddress.text
            val city = binding.etCity.text
            val state = binding.etState.text
            val postal = binding.etPostalcode.text
            if (email.isNotEmpty() && pass.isNotEmpty() && firstname.isNotEmpty() && lastname.isNotEmpty() && conpass.isNotEmpty() && phone.isNotEmpty() && address.isNotEmpty() && city.isNotEmpty() && state.isNotEmpty() && postal.isNotEmpty() && filePath!=null){
                progress.show()
                if (pass.toString()==conpass.toString()) {
                    viewModel.Signup(
                        firstname.toString(),
                        lastname.toString(),
                        email.toString(),
                        pass.toString(),
                        conpass.toString(),
                        phone.toString(),
                        address.toString(),
                        city.toString(),
                        state.toString(),
                        postal.toString(),
                        filePath!!
                    )
                }
                else {
                    progress.dismiss()
                    Toast.makeText(this, "Please Check Your Password", Toast.LENGTH_LONG).show()
                }
            }
            else {
                Toast.makeText(this, "Please Check Your Details", Toast.LENGTH_LONG).show()
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