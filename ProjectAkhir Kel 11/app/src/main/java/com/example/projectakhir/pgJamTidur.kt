package com.example.projectakhir

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class pgJamTidur : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pg_jam_tidur)

        var nilaiImt = findViewById<TextView>(R.id.nilaiIMT)
        var jamtidur = findViewById<TextView>(R.id.jamTidur)
        var nextbtn = findViewById<Button>(R.id.nextBtn)
        var homeBtn = findViewById<Button>(R.id.homeBtn)

        var _namaUser = intent.getStringExtra("namaUser")
        var bb = intent.getStringExtra("beratBadan")
        var tb = intent.getStringExtra("tinggiBadan")
        var totalkalories = intent.getStringExtra("totalKalori")
        var totaljamtidur = 0
        var nilaiIMT = 0.0
        var _bb = 0.0
        var _tb = 0.0
        _bb = bb!!.toDouble()
        _tb = tb!!.toDouble()
        nilaiIMT = _bb / (_tb * _tb / 10000)

        nilaiImt.setText(nilaiIMT.toString())
        if(nilaiIMT<24.0){
            totaljamtidur = 9
        }
        else if(nilaiIMT < 25.0){
            totaljamtidur = 8
        }
        else if(nilaiIMT < 26.0){
            totaljamtidur = 7
        }
        else if(nilaiIMT < 27.0){
            totaljamtidur = 6
        }
        else if(nilaiIMT < 28.0){
            totaljamtidur = 5
        }
        else{
            totaljamtidur = 4
        }
        jamtidur.setText(totaljamtidur.toString())

        homeBtn.setOnClickListener {
            val intent = Intent(this@pgJamTidur, pgHome::class.java)
            intent.putExtra("namaUser", _namaUser.toString())
            intent.putExtra("beratBadan", bb.toString())
            intent.putExtra("tinggiBadan", tb.toString())
            intent.putExtra("totalKalori", totalkalories.toString())
            startActivity(intent)
        }

        nextbtn.setOnClickListener {
            var text = "Rekomendasi Jam Tidur telah disimpan"
            Toast.makeText(this@pgJamTidur, text, Toast.LENGTH_SHORT).show()
            val intent = Intent(this@pgJamTidur, pgFinal::class.java)
            intent.putExtra("namaUser", _namaUser.toString())
            intent.putExtra("beratBadan", bb.toString())
            intent.putExtra("tinggiBadan", tb.toString())
            intent.putExtra("totalKalori", totalkalories.toString())
            intent.putExtra("jamtidur", totaljamtidur.toString())
            startActivity(intent)
        }
    }

}