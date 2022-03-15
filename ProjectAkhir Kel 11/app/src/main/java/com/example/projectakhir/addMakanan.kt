package com.example.projectakhir

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import com.google.firebase.firestore.FirebaseFirestore

class addMakanan : AppCompatActivity() {
    var dataMakanan : ArrayList<makanan> = ArrayList<makanan>()
    lateinit var lvAdapter : ArrayAdapter<makanan>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_makanan)

        val db : FirebaseFirestore = FirebaseFirestore.getInstance()
        var submit = findViewById<Button>(R.id.submit)
        var back = findViewById<ImageButton>(R.id.backBtn)
        var nama = findViewById<EditText>(R.id.nama)
        var kalori = findViewById<EditText>(R.id.kalori)

        submit.setOnClickListener {
            TambahData(db, nama.text.toString(), kalori.text.toString())
            readData(db)
        }

        back.setOnClickListener {
            val intent = Intent(this@addMakanan, pgInputMakanan::class.java)
            startActivity(intent)
        }
    }

    fun TambahData(db: FirebaseFirestore, nama: String, kalori: String){
        val namaBaru = makanan(nama, kalori)
        db.collection("tbMakanan").document(nama)
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
        db.collection("tbMakanan").get()
            .addOnSuccessListener { result ->
                dataMakanan.clear() // DICLEAR SUPAYA NDA DOBEL
                for (document in result){
                    val namaBaru = makanan(document.data.get("nama").toString(), document.data.get("kalori").toString()) //AMBIL SEMUA DATA DI DATABASE SATU-SATU
                    dataMakanan.add(namaBaru) // DI ADD KE ARRAYLIST DATANAMA
                }
                lvAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener{
                Log.d("Firebase", it.message.toString())
            }
    }

}