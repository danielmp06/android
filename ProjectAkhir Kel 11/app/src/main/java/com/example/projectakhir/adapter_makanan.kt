package com.example.projectakhir

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class adapter_makanan (private val _listNote: ArrayList<makanan>) :
    RecyclerView.Adapter<adapter_makanan.ListViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_adapter_makanan, parent, false)
        return ListViewHolder(view)
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data : makanan)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var _list = _listNote[position]

        holder._nama.setText(_list.nama)
        holder._calori.setText(_list.kalori)

        holder._nama.setOnClickListener {
            onItemClickCallback.onItemClicked(_listNote[position])
        }
    }

    override fun getItemCount(): Int {
        return _listNote.size
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var _nama: TextView = itemView.findViewById(R.id.nama_makanan2)
        var _calori: TextView = itemView.findViewById(R.id.calori_makanan2)
    }
}