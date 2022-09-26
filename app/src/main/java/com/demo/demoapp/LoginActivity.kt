package com.demo.demoapp

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Binder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.demo.demoapp.databinding.ActivityLoginBinding
import com.demo.demoapp.databinding.ActivityMainBinding
import com.demo.demoapp.viewmodel.MainViewModel
import com.octel.crysta.utils.Status

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var isReadPermissionGranted = false
    private var isWritePermissionGranted = false
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ permissions ->
            isReadPermissionGranted = permissions[android.Manifest.permission.READ_EXTERNAL_STORAGE] ?: isReadPermissionGranted
            isWritePermissionGranted = permissions[android.Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: isWritePermissionGranted
        }
        requestPermission()

        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.result.observe(this) {
            when(it.status) {
                Status.SUCCESS -> startActivity(Intent(this, MainActivity::class.java))
                Status.ERROR -> Toast.makeText(this, "Error with server", Toast.LENGTH_LONG).show()
                Status.LOADING -> Toast.makeText(this, "Error with server", Toast.LENGTH_LONG).show()
                Status.FAILED -> startActivity(Intent(this, RegisterActivity::class.java))
            }
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmailLogin.text
            val pass = binding.etPassLogin.text
            if (email.isNotEmpty() && pass.isNotEmpty()){
                viewModel.Login(email.toString(), pass.toString())
            }
        }
        binding.tvGotoSignup.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
    private fun requestPermission(){

        val isReadPermission = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        val isWritePermission = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        val minSdkLevel = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

        isReadPermissionGranted = isReadPermission
        isWritePermissionGranted = isWritePermission || minSdkLevel

        val permissionRequest = mutableListOf<String>()
        if (!isWritePermissionGranted){
            permissionRequest.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (!isReadPermissionGranted){
            permissionRequest.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (permissionRequest.isNotEmpty())
        {
            permissionLauncher.launch(permissionRequest.toTypedArray())
        }
    }
}