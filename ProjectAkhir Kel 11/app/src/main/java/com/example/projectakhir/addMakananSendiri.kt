package com.example.projectakhir

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class addMakananSendiri : AppCompatActivity() {
    var totalkaloridiet = 0
    private lateinit var _lvData:RecyclerView
    var dataAllMakanan : ArrayList<makanan> = ArrayList<makanan>()
    var datasemuamakanan : ArrayList<makanan> = ArrayList<makanan>()
    var datamakanansendiri : ArrayList<makanan> = ArrayList<makanan>()
    private lateinit var totalkal:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_makanan_sendiri)

        val db : FirebaseFirestore = FirebaseFirestore.getInstance()
        _lvData = findViewById(R.id.lvDataAllMakanan)
        var backtoMakanan = findViewById<ImageButton>(R.id.backtoMakanan)
        var submitBtn = findViewById<Button>(R.id.submitBtn)
        var _namaUser = intent.getStringExtra("namaUser")
        var bb = intent.getStringExtra("beratBadan")
        var tb = intent.getStringExtra("tinggiBadan")
        var totalkalori = intent.getStringExtra("totalKalori")
        totalkal = findViewById(R.id.dietcalories)
        totalkal.setText(totalkaloridiet.toString())

        resetdata(db)
        readData(db)

        backtoMakanan.setOnClickListener {
            val intent = Intent(this@addMakananSendiri, pgMakanan::class.java)
            intent.putExtra("namaUser", _namaUser.toString())
            intent.putExtra("beratBadan", bb.toString())
            intent.putExtra("tinggiBadan", tb.toString())
            intent.putExtra("totalKalori", totalkalori.toString())
            startActivity(intent)
        }

        submitBtn.setOnClickListener {

            for (i in datamakanansendiri) {
                db.collection("tbRekomendasiMakanan").document(i.nama)
                    .set(i) // NAMBAH DATA
                    .addOnSuccessListener {
                        Log.d("Firebase", "Simpan Data Berhasil")
                    }
                    .addOnFailureListener {
                        Log.d("Firebase", it.message.toString())
                    }
            }
            var text = "Rekomendasi Diet telah disimpan"
            Toast.makeText(this@addMakananSendiri, text, Toast.LENGTH_SHORT).show()
            val intent = Intent(this@addMakananSendiri, pgJamTidur::class.java)
            intent.putExtra("namaUser", _namaUser.toString())
            intent.putExtra("beratBadan", bb.toString())
            intent.putExtra("tinggiBadan", tb.toString())
            intent.putExtra("totalKalori", totalkalori.toString())
            startActivity(intent)
        }

    }

    fun resetdata(db: FirebaseFirestore){
        db.collection("tbRekomendasiMakanan").get()
            .addOnSuccessListener { result ->
                datasemuamakanan.clear()
                for (document in result){
                    val namaBaru = makanan(document.data.get("nama").toString(), document.data.get("kalori").toString())
                    datasemuamakanan.add(namaBaru)
                }
                for(i in datasemuamakanan) {
                    db.collection("tbRekomendasiMakanan").document(i.nama)
                        .delete()
                }
            }
            .addOnFailureListener{
                Log.d("Firebase", it.message.toString())
            }



    }

    fun readData (db: FirebaseFirestore){
        db.collection("tbMakanan").get()
            .addOnSuccessListener { result ->
                dataAllMakanan.clear() // DICLEAR SUPAYA NDA DOBEL
                for (document in result){
                    val namaBaru = makanan(document.data.get("nama").toString(), document.data.get("kalori").toString())
                    dataAllMakanan.add(namaBaru)
                }
                _lvData.layoutManager= LinearLayoutManager(this)
                val Makanan_adapter = adapter_makanan(dataAllMakanan)
                _lvData.adapter = Makanan_adapter

                Makanan_adapter.setOnItemClickCallback(object : adapter_makanan.OnItemClickCallback{
                    override fun onItemClicked(data: makanan) {
                        tambahKalori(db, data.nama)
                        datamakanansendiri.add(data)
                    }
                })
            }
            .addOnFailureListener{
                Log.d("Firebase", it.message.toString())
            }
    }


    fun tambahKalori(db: FirebaseFirestore, nama: String){
        db.collection("tbMakanan").get()
            .addOnSuccessListener { result ->
                for (document in result){
                    val namaBaru = makanan(document.data.get("nama").toString(), document.data.get("kalori").toString())
                    if(namaBaru.nama == nama){
                        totalkaloridiet += namaBaru.kalori.toInt()
                        totalkal.setText(totalkaloridiet.toString())
                        //Toast.makeText(this@pgInputMakanan, totalkalori.toString(),Toast.LENGTH_SHORT).show()
                    }
                }

            }
            .addOnFailureListener{
                Log.d("Firebase", it.message.toString())
            }
    }
}