package com.example.projectakhir

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import com.google.firebase.firestore.FirebaseFirestore

class addNewOlahraga : AppCompatActivity() {
    var dataallOlahraga : ArrayList<dataOlahraga> = ArrayList<dataOlahraga>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_olahraga)

        val db : FirebaseFirestore = FirebaseFirestore.getInstance()
        var submit = findViewById<Button>(R.id.submit)
        var back = findViewById<ImageButton>(R.id.backBtn)
        var nama = findViewById<EditText>(R.id.nama)
        var kalori = findViewById<EditText>(R.id.kalori)
        var durasi = findViewById<EditText>(R.id.durasi)
        var link = findViewById<EditText>(R.id.link)
        var _namaUser = intent.getStringExtra("namaUser")
        var bb = intent.getStringExtra("beratBadan")
        var tb = intent.getStringExtra("tinggiBadan")
        var totalkalori = intent.getStringExtra("totalKalori")


//        lvAdapter = ArrayAdapter(
//            this,
//            android.R.layout.simple_list_item_1,
//            dataallOlahraga
//        )
//        //_lvData.adapter = lvAdapter
//        readData(db)

        submit.setOnClickListener {
            TambahData(db, nama.text.toString(), kalori.text.toString(), durasi.text.toString(), link.text.toString())
            readData(db)
            val intent = Intent(this@addNewOlahraga, addOlahragaSendiri::class.java)
            startActivity(intent)
        }

        back.setOnClickListener {
            val intent = Intent(this@addNewOlahraga, addOlahragaSendiri::class.java)
            intent.putExtra("namaUser", _namaUser.toString())
            intent.putExtra("beratBadan", bb.toString())
            intent.putExtra("tinggiBadan", tb.toString())
            intent.putExtra("totalKalori", totalkalori.toString())
            startActivity(intent)
        }
    }


    fun TambahData(db: FirebaseFirestore, nama: String, kalori: String, durasi: String, link: String){
        val namaBaru = dataOlahraga(nama, kalori, durasi, link)
        db.collection("tbOlahraga").document(nama)
            .set(namaBaru) // NAMBAH DATA
            .addOnSuccessListener {
                readData(db) // REFRESH ARRAYLIST DATANAMA
                Log.d("Firebase", "Simpan Data Berhasil")
            }
            .addOnFailureListener{
                Log.d("Firebase", it.message.toString())
            }
    }

    fun readData (db: FirebaseFirestore){
        db.collection("tbOlahraga").get()
            .addOnSuccessListener { result ->
                dataallOlahraga.clear() // DICLEAR SUPAYA NDA DOBEL
                for (document in result){
                    val namaBaru = dataOlahraga(document.data.get("nama").toString(), document.data.get("kalori").toString(), document.data.get("durasi").toString(), document.data.get("link").toString()) //AMBIL SEMUA DATA DI DATABASE SATU-SATU
                    dataallOlahraga.add(namaBaru) // DI ADD KE ARRAYLIST DATANAMA
                }
            }
            .addOnFailureListener{
                Log.d("Firebase", it.message.toString())
            }
    }
}