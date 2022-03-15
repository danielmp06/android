package com.example.projectakhir

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class addOlahragaSendiri : AppCompatActivity() {
    var totalkaloriburned = 0
    private lateinit var _lvData:RecyclerView
    var dataAllOlahraga : ArrayList<dataOlahraga> = ArrayList<dataOlahraga>()
    var datasemuaolahraga : ArrayList<dataOlahraga> = ArrayList<dataOlahraga>()
    var dataolahragasendiri : ArrayList<dataOlahraga> = ArrayList<dataOlahraga>()
    private lateinit var totalkal : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_olahraga_sendiri)

        val db : FirebaseFirestore = FirebaseFirestore.getInstance()
        _lvData = findViewById(R.id.lvDataAllOlahraga)
        var backtoOlahraga = findViewById<ImageButton>(R.id.backtoOlahraga)
        var addnewsport = findViewById<Button>(R.id.addNewSport)
        var submitBtn = findViewById<Button>(R.id.submitBtn)
        var _namaUser = intent.getStringExtra("namaUser")
        var bb = intent.getStringExtra("beratBadan")
        var tb = intent.getStringExtra("tinggiBadan")
        var totalkalori = intent.getStringExtra("totalKalori")
        totalkal = findViewById(R.id.caloriesburned)

        resetdata(db)
        readData(db)
        totalkal.setText(totalkaloriburned.toString())

        backtoOlahraga.setOnClickListener {
            val intent = Intent(this@addOlahragaSendiri, pgOlahraga::class.java)
            intent.putExtra("namaUser", _namaUser.toString())
            intent.putExtra("beratBadan", bb.toString())
            intent.putExtra("tinggiBadan", tb.toString())
            intent.putExtra("totalKalori", totalkalori.toString())
            startActivity(intent)
        }

        submitBtn.setOnClickListener {



            for (i in dataolahragasendiri) {
                db.collection("tbRekomendasiOlahraga").document(i.nama)
                    .set(i) // NAMBAH DATA
                    .addOnSuccessListener {
                        Log.d("Firebase", "Simpan Data Berhasil")
                    }
                    .addOnFailureListener {
                        Log.d("Firebase", it.message.toString())
                    }
            }
            var text = "Rekomendasi Olahraga telah disimpan"
            Toast.makeText(this@addOlahragaSendiri, text, Toast.LENGTH_SHORT).show()
            val intent = Intent(this@addOlahragaSendiri, pgMakanan::class.java)
            intent.putExtra("namaUser", _namaUser.toString())
            intent.putExtra("beratBadan", bb.toString())
            intent.putExtra("tinggiBadan", tb.toString())
            intent.putExtra("totalKalori", totalkalori.toString())
            startActivity(intent)
        }

        addnewsport.setOnClickListener {
            val intent = Intent(this@addOlahragaSendiri, addNewOlahraga::class.java)
            intent.putExtra("namaUser", _namaUser.toString())
            intent.putExtra("beratBadan", bb.toString())
            intent.putExtra("tinggiBadan", tb.toString())
            intent.putExtra("totalKalori", totalkalori.toString())
            startActivity(intent)
        }

    }

    fun resetdata(db: FirebaseFirestore){
        db.collection("tbRekomendasiOlahraga").get()
            .addOnSuccessListener { result ->
                datasemuaolahraga.clear()
                for (document in result){
                    val namaBaru = dataOlahraga(document.data.get("nama").toString(), document.data.get("kalori").toString(), document.data.get("durasi").toString(), document.data.get("link").toString())
                    datasemuaolahraga.add(namaBaru)
                }
                for(i in datasemuaolahraga) {
                    db.collection("tbRekomendasiOlahraga").document(i.nama)
                        .delete()
                }
            }
            .addOnFailureListener{
                Log.d("Firebase", it.message.toString())
            }

    }

    fun readData (db:FirebaseFirestore){
        db.collection("tbOlahraga").get()
            .addOnSuccessListener { result ->
                dataAllOlahraga.clear() // DICLEAR SUPAYA NDA DOBEL
                for (document in result){
                    val namaBaru = dataOlahraga(document.data.get("nama").toString(), document.data.get("kalori").toString(), document.data.get("durasi").toString(), document.data.get("link").toString())
                    dataAllOlahraga.add(namaBaru)
                }
                _lvData.layoutManager= LinearLayoutManager(this)
                val Olahraga_adapter = adapter_olahraga(dataAllOlahraga)
                _lvData.adapter = Olahraga_adapter

                Olahraga_adapter.setOnItemClickCallback(object : adapter_olahraga.OnItemClickCallback{
                    override fun onItemClicked(data:dataOlahraga){
                        tambahKalori(db, data.nama)
                        dataolahragasendiri.add(data)
                    }
                })
            }
            .addOnFailureListener{
                Log.d("Firebase", it.message.toString())
            }
    }


    fun tambahKalori(db: FirebaseFirestore, nama: String){
        db.collection("tbOlahraga").get()
            .addOnSuccessListener { result ->
                for (document in result){
                    val namaBaru = dataOlahraga(document.data.get("nama").toString(), document.data.get("kalori").toString(),document.data.get("durasi").toString(), document.data.get("link").toString())
                    if(namaBaru.nama == nama){
                        totalkaloriburned += namaBaru.kalori.toInt()
                        //Toast.makeText(this@pgInputMakanan, totalkalori.toString(),Toast.LENGTH_SHORT).show()
                    }
                }
                totalkal.setText(totalkaloriburned.toString())
            }
            .addOnFailureListener{
                Log.d("Firebase", it.message.toString())
            }
    }
}