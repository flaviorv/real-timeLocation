package com.example.real_time_location


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.real_time_location.databinding.FragmentListaBinding
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.ArrayList
import android.Manifest.permission.*
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import androidx.appcompat.app.AlertDialog
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.getInstance


internal val lista_arquivos = ArrayList<String>()
internal lateinit var lista_adapter: Adapter
class ListaFragment(val applicationContext: Context): Fragment() {
    private var _binding: FragmentListaBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        _binding = FragmentListaBinding.inflate(inflater, container, false)
        val view = binding.root
        activateFeatures()




        return view

    }




    override fun onDestroy() {
        super.onDestroy()
        lista_arquivos.removeAll(lista_arquivos)

    }
    fun activateFeatures() {
        val recycler_view: RecyclerView = binding.recyclerView
        lista_adapter = Adapter(lista_arquivos)
        val layoutManager = LinearLayoutManager(applicationContext)
        recycler_view.layoutManager = layoutManager

        recycler_view.adapter = lista_adapter
        recycler_view.setHasFixedSize(true)
        lista_adapter.setOnItemClickListener(object : Adapter.onItemClickListener {

            override fun onItemClick(position: Int, name: String) {

                Toast.makeText(
                    applicationContext, "You clicked on item of number $position: $name",
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(applicationContext, Reader_Activity::class.java)
                intent.putExtra("name", name)
                startActivity(intent)

            }

        })

    }



}