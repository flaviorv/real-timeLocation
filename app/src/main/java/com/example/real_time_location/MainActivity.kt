package com.example.real_time_location


import android.Manifest.permission.*
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.getInstance



class MainActivity : AppCompatActivity() {

    private lateinit var _location: String
    private var availability: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().add(R.id.fragmentContainer, HomeFragment()).commit()

    }

    private fun readMyCurrentCoordinates() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (!isGPSEnabled && !isNetworkEnabled) {
            Log.d("Permissao", "Ative os servicos necessários")
            Toast.makeText(this, "Ative as configurações de localização do dispositivo.",
            Toast.LENGTH_SHORT).show()
        }else {
            if (isGPSEnabled) {
                try {
                  locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000L, 0f, locationListener)
                    Log.d("Permissao", "Security Exception")
                }catch (e: SecurityException){
                    Toast.makeText(this, "Erro",
                        Toast.LENGTH_SHORT).show()

                }
            }else if(isNetworkEnabled){
                try {
                    locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, 5000L, 0f, locationListener)

                }catch(ex:SecurityException){
                    Log.d("Permissao", "Security Exception")
                    Toast.makeText(this, "Erro",
                        Toast.LENGTH_SHORT).show()
                }


            }
        }
    }

    private val locationListener: LocationListener = object: LocationListener{
        override fun onLocationChanged(location: Location){
            _location = location.toString().subSequence(9, 34).toString()
            availability = true
            Toast.makeText(applicationContext, _location,
            Toast.LENGTH_SHORT).show()

        }
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle){}
        override fun onProviderEnabled(provider: String){}
        override fun onProviderDisabled(provider: String){}
    }
    val REQUEST_PERMISSIONS_CODE = 128
    fun ativarLocalizacao(view: View?) {
        val permissionAFL = ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
        val permissionACL = ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION)
        if (permissionAFL != PackageManager.PERMISSION_GRANTED &&
            permissionACL != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this, ACCESS_FINE_LOCATION)) {
                callDialog("É preciso liberar ACCESS_FINE_LOCATION",
                    arrayOf(ACCESS_FINE_LOCATION))
            } else {
                ActivityCompat.requestPermissions(this,
                    arrayOf(ACCESS_FINE_LOCATION),
                    REQUEST_PERMISSIONS_CODE)

            }
        } else {

            readMyCurrentCoordinates()
        }
    }
    private fun callDialog(mensagem: String,
                           permissions: Array<String>) {
        val mDialog = AlertDialog.Builder(this)
            .setTitle("Permissão")
            .setMessage(mensagem)
            .setPositiveButton("Ok")
            { dialog, id ->
                ActivityCompat.requestPermissions(
                    this@MainActivity, permissions,
                    REQUEST_PERMISSIONS_CODE)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel")
            { dialog, id ->
                dialog.dismiss()
            }
        mDialog.show()
    }



    private fun createFile() {
        if (availability == true){
            try {
                val getDateTime = getInstance().time
                var dateTimeFormat = SimpleDateFormat("dd.MM.yyyy-HH-mm-ss", Locale.getDefault())
                val dateTime = dateTimeFormat.format(getDateTime)
                val file = File(getExternalFilesDir(null), "$dateTime.txt")
                val fileOutputStream = FileOutputStream(file)
                fileOutputStream.write(_location.toByteArray())
                Toast.makeText(applicationContext,
                    "Arquivo salvo:\n$_location",
                    Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                Toast.makeText(applicationContext,
                    "Erro: ${e.printStackTrace()}",
                    Toast.LENGTH_SHORT).show()

                e.printStackTrace()
            }
        }else{
            Toast.makeText(applicationContext, "Localização não disponível", Toast.LENGTH_SHORT).show()
        }
    }
    fun readFolders(): String {
        val storageDirectory =
            getExternalFilesDir(null)?.absolutePath

        val file = File(
            storageDirectory)
            .walk()
            .forEach {
                lista_arquivos.add(it.name.toString())
                Toast.makeText(applicationContext,
                    "Arquivos encontrados: $lista_arquivos",
                    Toast.LENGTH_SHORT).show()
            }
        return lista_arquivos.toString()

    }






    fun salvarLocalizacao(view: View?) {
        if (ContextCompat.checkSelfPermission(
                this, WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this, WRITE_EXTERNAL_STORAGE
                )) {
                callDialog(
                    "É preciso liberar WRITE_EXTERNAL_STORAGE",
                    arrayOf(WRITE_EXTERNAL_STORAGE)
                )
            } else {
                ActivityCompat.requestPermissions(this,
                    arrayOf(WRITE_EXTERNAL_STORAGE),
                    REQUEST_PERMISSIONS_CODE)
            }
        } else {
            createFile()
        }
    }
    fun acessarRegistro(view: View?) {
        if (ContextCompat.checkSelfPermission(
                this, READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this, READ_EXTERNAL_STORAGE
                )
            ) {
                callDialog(
                    "É preciso a liberar READ_EXTERNAL_STORAGE",
                    arrayOf(READ_EXTERNAL_STORAGE)
                )
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(READ_EXTERNAL_STORAGE),
                        REQUEST_PERMISSIONS_CODE
                )
            }
        } else {
            readFolders()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, ListaFragment(applicationContext)).commit()
                
        }
    }


}

