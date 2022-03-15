package com.example.projectakhir

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class pgMakanan : AppCompatActivity() {
    private lateinit var _lvData:RecyclerView
    var datamakananrekomendasi : ArrayList<makanan> = ArrayList<makanan>()
    var dataallmakanan : ArrayList<makanan> = ArrayList<makanan>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pg_makanan)

        _lvData = findViewById(R.id.lvDataMakanan)
        val db : FirebaseFirestore = FirebaseFirestore.getInstance()
        var displayIMT = findViewById<TextView>(R.id.nilaiIMT)
        var displaykategori = findViewById<TextView>(R.id.kategori)
        var displaynamauser = findViewById<TextView>(R.id.namauser)
        var homeBtn = findViewById<ImageButton>(R.id.homeBtn)
        var submitBtn = findViewById<Button>(R.id.submitBtn)
        var toaddmakanansendiri = findViewById<Button>(R.id.toMakananSendiri)

        var _namaUser = intent.getStringExtra("namaUser")
        var bb = intent.getStringExtra("beratBadan")
        var tb = intent.getStringExtra("tinggiBadan")
        var totalkalories = intent.getStringExtra("totalKalori")

        displaynamauser.setText(_namaUser.toString())
        var nilaiIMT = 0.0
        var _bb = 0.0
        var _tb = 0.0
        _bb = bb!!.toDouble()
        _tb = tb!!.toDouble()
        if(_bb == 0.0 && _tb == 0.0){
            displayIMT.setText("-")
            var text = "-"
            displaykategori.setText(text)
        }
        else {
            nilaiIMT = _bb / (_tb * _tb / 10000)
            displayIMT.setText(nilaiIMT.toString())
            if (nilaiIMT < 18.4) {
                var text = "Berat Badan Kurang"
                displaykategori.setText(text)
            } else if (nilaiIMT < 24.9) {
                var text = "Berat Badan Ideal"
                displaykategori.setText(text)
            } else if (nilaiIMT < 29.9) {
                var text = "Berat Badan Lebih"
                displaykategori.setText(text)
            } else if (nilaiIMT < 39.9) {
                var text = "Gemuk"
                displaykategori.setText(text)
            } else {
                var text = "Sangat Gemuk"
                displaykategori.setText(text)
            }
        }

        resetdata(db)
        readData(db, nilaiIMT)

        homeBtn.setOnClickListener {
            val intent = Intent(this@pgMakanan, pgHome::class.java)
            intent.putExtra("namaUser", _namaUser.toString())
            intent.putExtra("beratBadan", bb.toString())
            intent.putExtra("tinggiBadan", tb.toString())
            intent.putExtra("totalKalori", totalkalories.toString())
            startActivity(intent)
        }


        submitBtn.setOnClickListener {
            for (i in datamakananrekomendasi) {
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
            Toast.makeText(this@pgMakanan, text, Toast.LENGTH_SHORT).show()
            val intent = Intent(this@pgMakanan, pgJamTidur::class.java)
            intent.putExtra("namaUser", _namaUser.toString())
            intent.putExtra("beratBadan", bb.toString())
            intent.putExtra("tinggiBadan", tb.toString())
            intent.putExtra("totalKalori", totalkalories.toString())
            startActivity(intent)
        }

        toaddmakanansendiri.setOnClickListener {
            val intent = Intent(this@pgMakanan, addMakananSendiri::class.java)
            intent.putExtra("namaUser", _namaUser.toString())
            intent.putExtra("beratBadan", bb.toString())
            intent.putExtra("tinggiBadan", tb.toString())
            intent.putExtra("totalKalori", totalkalories.toString())
            startActivity(intent)
        }
    }

    fun resetdata(db: FirebaseFirestore){
        db.collection("tbRekomendasiMakanan").get()
            .addOnSuccessListener { result ->
                dataallmakanan.clear()
                for (document in result){
                    val namaBaru = makanan(document.data.get("nama").toString(), document.data.get("kalori").toString())
                    dataallmakanan.add(namaBaru)
                }
                for(i in dataallmakanan) {
                    db.collection("tbRekomendasiMakanan").document(i.nama)
                        .delete()
                }
            }
            .addOnFailureListener{
                Log.d("Firebase", it.message.toString())
            }


    }

    fun readData (db: FirebaseFirestore, nilaiIMT: Double){
        var totalkalorisehat = 0.0
        if (nilaiIMT < 18.4) {
            totalkalorisehat = 2500.0
        } else if (nilaiIMT < 24.9) {
            totalkalorisehat = 2200.0
        } else if (nilaiIMT < 29.9) {
            totalkalorisehat = 2000.0
        } else if (nilaiIMT < 39.9) {
            totalkalorisehat = 1800.0
        } else {
            totalkalorisehat = 1600.0
        }
        db.collection("tbMakanan").get()
            .addOnSuccessListener { result ->
                datamakananrekomendasi.clear()
                var totalkaloriburned = 0.0
                for (document in result){
                    val namaBaru = makanan(document.data.get("nama").toString(), document.data.get("kalori").toString())
                    if(totalkaloriburned < totalkalorisehat){
                        totalkaloriburned += namaBaru.kalori.toString().toInt()
                        datamakananrekomendasi.add(namaBaru)
                    }
                }
                _lvData.layoutManager= LinearLayoutManager(this)
                val Makanan_adapter = adapter_makanan(datamakananrekomendasi)
                _lvData.adapter = Makanan_adapter
            }
            .addOnFailureListener{
                Log.d("Firebase", it.message.toString())
            }
    }
}