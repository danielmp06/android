package com.example.projectakhir

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.firebase.firestore.FirebaseFirestore

class pgBbTb : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pg_bb_tb)

        val db : FirebaseFirestore = FirebaseFirestore.getInstance()
        var _bb = findViewById<EditText>(R.id.beratBadan)
        var _tb = findViewById<EditText>(R.id.tinggiBadan)
        var nextBtn = findViewById<Button>(R.id.nextBtn)

        val _namaUser = intent.getStringExtra("namaUser")
        val _totalKalori = intent.getStringExtra("totalKalori")

        nextBtn.setOnClickListener {
            val userdata = dataUser(
                _namaUser.toString(),
                _bb.text.toString(),
                _tb.text.toString(),
                _totalKalori.toString()
            )
            db.collection("tbUser").document(userdata.nama)
                .set(userdata) // NAMBAH DATA
                .addOnSuccessListener {
                    Log.d("Firebase", "Simpan Data Berhasil")
                }
                .addOnFailureListener{
                    Log.d("Firebase", it.message.toString())
                }

            var temp = 1
            if(_totalKalori.toString() == "0"){
                temp = 0
            }
            if(temp == 0){
                val intent = Intent(this@pgBbTb, pgInputMakanan::class.java)
                intent.putExtra("namaUser", _namaUser.toString())
                intent.putExtra("beratBadan", _bb.text.toString())
                intent.putExtra("tinggiBadan", _tb.text.toString())
                intent.putExtra("totalKalori", _totalKalori.toString())
                startActivity(intent)
            }
            else{
                val intent = Intent(this@pgBbTb, pgOlahraga::class.java)
                intent.putExtra("namaUser", _namaUser.toString())
                intent.putExtra("beratBadan", _bb.text.toString())
                intent.putExtra("tinggiBadan", _tb.text.toString())
                intent.putExtra("totalKalori", _totalKalori.toString())
                startActivity(intent)
            }
        }
    }
}