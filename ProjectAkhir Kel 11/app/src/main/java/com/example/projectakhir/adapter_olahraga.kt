package com.example.projectakhir

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class adapter_olahraga (private val _listNote: ArrayList<dataOlahraga>) :
    RecyclerView.Adapter<adapter_olahraga.ListViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val view : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_adapter_olahraga, parent, false)
        return ListViewHolder(view)
    }

    private lateinit var onItemClickCallback : OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data:dataOlahraga)
    }

    fun setOnItemClickCallback(onItemClickCallback : OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var _list = _listNote[position]

        holder._nama.setText(_list.nama)
        holder._calori.setText(_list.kalori)
        holder._durasi.setText(_list.durasi)
        holder._link.setText(_list.link)

        holder._nama.setOnClickListener {
            onItemClickCallback.onItemClicked(_listNote[position])
        }
    }

    override fun getItemCount(): Int {
        return _listNote.size
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var _nama: TextView = itemView.findViewById(R.id.nama_olahraga2)
        var _calori: TextView = itemView.findViewById(R.id.calori_olahraga2)
        var _durasi: TextView = itemView.findViewById(R.id.Durasi_olahraga2)
        var _link: TextView = itemView.findViewById(R.id.Link_olahraga2)
    }
}