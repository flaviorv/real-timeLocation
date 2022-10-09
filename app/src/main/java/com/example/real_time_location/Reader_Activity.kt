package com.example.real_time_location

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.delay
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import android.os.Handler
import androidx.core.os.postDelayed

class Reader_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reader)
        val name = intent.getStringExtra("name")


        val reading_textView = findViewById<TextView>(R.id.reading_textView)
        reading_textView.text = readFile(name.toString())



    }
    fun readFile(put_name: String): String{

        val file = File(getExternalFilesDir(null), put_name)
        if (!file.exists()) {
            Toast.makeText(
                this@Reader_Activity,
                "Arquivo não encontrado",
                Toast.LENGTH_SHORT
            ).show()
        }
        val fileInputStream = FileInputStream(file)
        var inputStreamReader = InputStreamReader(fileInputStream)
        val bufferedReader = BufferedReader(inputStreamReader)
        val stringBuilder = StringBuilder()
        var text: String? = null
        while ({ text = bufferedReader.readLine(); text }() != null) {
            stringBuilder.append(text)

            val handler = Handler()
            handler.postDelayed({Toast.makeText(
                this@Reader_Activity,
                "Arquivo encontrado: $stringBuilder",
                Toast.LENGTH_SHORT
            ).show()
            }, 3000)

        }
        return stringBuilder.toString()
        fileInputStream.close()
    }
}