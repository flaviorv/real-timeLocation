package com.example.real_time_location

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.ExtractedText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlin.coroutines.coroutineContext

internal class Adapter(private var lista: List<String>): RecyclerView.Adapter<Adapter.MyViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int, name: String)
    }
    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val arquivos = LayoutInflater.from(parent.context).inflate(R.layout.lista_adapter, parent, false)
        return MyViewHolder(arquivos, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val arquivo = lista[position]
        holder.arquivo_textView.text = arquivo

    }

    override fun getItemCount(): Int {
        return lista_arquivos.size
    }
    class MyViewHolder(view: View, listener: onItemClickListener) : RecyclerView.ViewHolder(view){
        var arquivo_textView: TextView = view.findViewById(R.id.arquivo_textView)
        init{
            view.setOnClickListener { listener.onItemClick(adapterPosition, arquivo_textView.text.toString()) }
        }


    }
}