package com.example.projectakhir

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class adapter_cheatday(private val _listNote: ArrayList<cheatdayData>) :
    RecyclerView.Adapter<adapter_cheatday.ListViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val view : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_adapter_cheatday, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var _list = _listNote[position]

        holder._nama.setText(_list.nama)
        holder._tanggal.setText(_list.tanggal)
        holder._tahun.setText(_list.tahun)
        holder._bulan.setText(_list.bulan)
    }

    override fun getItemCount(): Int {
        return _listNote.size
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var _nama: TextView = itemView.findViewById(R.id.nama_cheatday2)
        var _tanggal: TextView = itemView.findViewById(R.id.tanggal_cheatday2)
        var _tahun: TextView = itemView.findViewById(R.id.tahun_cheatday2)
        var _bulan: TextView = itemView.findViewById(R.id.bulan_cheatday2)
    }
}