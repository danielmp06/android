package com.example.projectakhir

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class adapter_login (private val _listNote: ArrayList<dataUser>) :
    RecyclerView.Adapter<adapter_login.ListViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val view : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_adapter_login, parent, false)
        return ListViewHolder(view)
    }

    private lateinit var onItemClickCallback : OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data:dataUser)
        fun deleteAkun(data:dataUser)
    }

    fun setOnItemClickCallback(onItemClickCallback : OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var _list = _listNote[position]

        holder._tipe.setText(_list.nama)
        holder._judul.setText(_list.bb)
        holder._jumlah.setText(_list.tb)
        holder._Category.setText(_list.totalkalori)

        holder._deleteAkun.setOnClickListener {
            onItemClickCallback.deleteAkun(_listNote[position])
        }

        holder._tipe.setOnClickListener {
            onItemClickCallback.onItemClicked(_listNote[position])
        }
    }

    override fun getItemCount(): Int {
        return _listNote.size
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var _tipe: TextView = itemView.findViewById(R.id.tipe_home2)
        var _judul: TextView = itemView.findViewById(R.id.Judul_home2)
        var _jumlah: TextView = itemView.findViewById(R.id.jumlah_home2)
        var _Category: TextView = itemView.findViewById(R.id.category_home2)
        var _deleteAkun : Button = itemView.findViewById(R.id.deleteAkun)
    }
}