package com.example.projectakhir

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.TestLooperManager
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class pgOlahraga : AppCompatActivity() {
    private lateinit var _lvData : RecyclerView

    var dataolahragarekomendasi : ArrayList<dataOlahraga> = ArrayList<dataOlahraga>()
    var dataallolahraga : ArrayList<dataOlahraga> = ArrayList<dataOlahraga>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pg_olahraga)
        val db : FirebaseFirestore = FirebaseFirestore.getInstance()
        var displayIMT = findViewById<TextView>(R.id.nilaiIMT)
        var displaykategori = findViewById<TextView>(R.id.kategori)
        var displaynamauser = findViewById<TextView>(R.id.namauser)
        var homeBtn = findViewById<ImageButton>(R.id.homeBtn)
        var submitBtn = findViewById<Button>(R.id.submitBtn)
        var toaddolahragasendiri = findViewById<Button>(R.id.toOlahragaSendiri)

        _lvData = findViewById(R.id.lvDataOlahraga)

        var _namaUser = intent.getStringExtra("namaUser")
        var bb = intent.getStringExtra("beratBadan")
        var tb = intent.getStringExtra("tinggiBadan")
        var totalkalories = intent.getStringExtra("totalKalori")

        resetdata(db)
        readData(db, totalkalories.toString())

        homeBtn.setOnClickListener {
            val intent = Intent(this@pgOlahraga, pgHome::class.java)
            intent.putExtra("namaUser", _namaUser.toString())
            intent.putExtra("beratBadan", bb.toString())
            intent.putExtra("tinggiBadan", tb.toString())
            intent.putExtra("totalKalori", totalkalories.toString())
            startActivity(intent)
        }

        displaynamauser.setText(_namaUser.toString())
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
            var nilaiIMT = _bb / (_tb * _tb / 10000)
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

        submitBtn.setOnClickListener {



            for (i in dataolahragarekomendasi) {
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
            Toast.makeText(this@pgOlahraga, text,Toast.LENGTH_SHORT).show()
            val intent = Intent(this@pgOlahraga, pgMakanan::class.java)
            intent.putExtra("namaUser", _namaUser.toString())
            intent.putExtra("beratBadan", bb.toString())
            intent.putExtra("tinggiBadan", tb.toString())
            intent.putExtra("totalKalori", totalkalories.toString())
            startActivity(intent)
        }

        toaddolahragasendiri.setOnClickListener {
            val intent = Intent(this@pgOlahraga, addOlahragaSendiri::class.java)
            intent.putExtra("namaUser", _namaUser.toString())
            intent.putExtra("beratBadan", bb.toString())
            intent.putExtra("tinggiBadan", tb.toString())
            intent.putExtra("totalKalori", totalkalories.toString())
            startActivity(intent)
        }
    }

    fun resetdata(db: FirebaseFirestore){
        db.collection("tbRekomendasiOlahraga").get()
            .addOnSuccessListener { result ->
                dataallolahraga.clear()
                for (document in result){
                    val namaBaru = dataOlahraga(document.data.get("nama").toString(), document.data.get("kalori").toString(), document.data.get("durasi").toString(), document.data.get("link").toString())
                    dataallolahraga.add(namaBaru)
                }
                for(i in dataallolahraga) {
                    db.collection("tbRekomendasiOlahraga").document(i.nama)
                        .delete()
                }
            }
            .addOnFailureListener{
                Log.d("Firebase", it.message.toString())
            }


    }

    fun readData (db:FirebaseFirestore, totalkalories: String){
        db.collection("tbOlahraga").get()
            .addOnSuccessListener { result ->
                dataolahragarekomendasi.clear()
                var totalkaloriburned = 0
                for (document in result){
                    val namaBaru = dataOlahraga(document.data.get("nama").toString(), document.data.get("kalori").toString(), document.data.get("durasi").toString(), document.data.get("link").toString())
                    if(totalkaloriburned < totalkalories.toInt()){
                        totalkaloriburned += namaBaru.kalori.toString().toInt()
                        dataolahragarekomendasi.add(namaBaru)
                    }
                    _lvData.layoutManager= LinearLayoutManager(this)
                    val Olahraga_adapter = adapter_olahraga(dataolahragarekomendasi)
                    _lvData.adapter = Olahraga_adapter
                }
            }
            .addOnFailureListener{
                Log.d("Firebase", it.message.toString())
            }
    }
}