package com.example.projectakhir

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class pgCheatday : AppCompatActivity() {
    var temp = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pg_cheatday)

        val db : FirebaseFirestore = FirebaseFirestore.getInstance()
        var cheatdaybtn = findViewById<Button>(R.id.setCheatDay)
        var tanggal = findViewById<EditText>(R.id.tanggal)
        var bulan = findViewById<EditText>(R.id.bulan)
        var tahun = findViewById<EditText>(R.id.tahun)
        var toHomeBtn = findViewById<ImageButton>(R.id.toHome)
        var toOlahragaBtn = findViewById<Button>(R.id.toOlahraga)
        var toMakananBtn = findViewById<Button>(R.id.toInputMakanan)
        val _namaUser = intent.getStringExtra("namaUser")
        var bb = intent.getStringExtra("beratBadan")
        var tb = intent.getStringExtra("tinggiBadan")
        var totalkalori = intent.getStringExtra("totalKalori")

        toHomeBtn.setOnClickListener {
            val intent = Intent(this@pgCheatday, pgHome::class.java)
            intent.putExtra("namaUser", _namaUser)
            intent.putExtra("beratBadan", bb)
            intent.putExtra("tinggiBadan", tb)
            intent.putExtra("totalKalori", totalkalori)
            startActivity(intent)
        }

        toOlahragaBtn.setOnClickListener {
            if(bb.toString() == "0" && tb.toString()=="0"){
                var text = "BB dan TB belum diisi"
                Toast.makeText(this@pgCheatday, text ,Toast.LENGTH_SHORT).show()
                val intent = Intent(this@pgCheatday, pgBbTb::class.java)
                intent.putExtra("namaUser", _namaUser)
                intent.putExtra("beratBadan", bb)
                intent.putExtra("tinggiBadan", tb)
                intent.putExtra("totalKalori", totalkalori)
                startActivity(intent)
            }
            else if(totalkalori.toString()=="0"){
                var text = "Total Kalori belum diisi"
                Toast.makeText(this@pgCheatday, text ,Toast.LENGTH_SHORT).show()
                val intent = Intent(this@pgCheatday, pgInputMakanan::class.java)
                intent.putExtra("namaUser", _namaUser)
                intent.putExtra("beratBadan", bb)
                intent.putExtra("tinggiBadan", tb)
                intent.putExtra("totalKalori", totalkalori)
                startActivity(intent)
            }
            else{
                val intent = Intent(this@pgCheatday, pgOlahraga::class.java)
                intent.putExtra("namaUser", _namaUser)
                intent.putExtra("beratBadan", bb)
                intent.putExtra("tinggiBadan", tb)
                intent.putExtra("totalKalori", totalkalori)
                startActivity(intent)
            }
        }

        toMakananBtn.setOnClickListener {
            val intent = Intent(this@pgCheatday, pgInputMakanan::class.java)
            intent.putExtra("namaUser", _namaUser)
            intent.putExtra("beratBadan", bb)
            intent.putExtra("tinggiBadan", tb)
            intent.putExtra("totalKalori", totalkalori)
            startActivity(intent)
        }

        cheatdaybtn.setOnClickListener {
            var _tanggal = tanggal.text.toString().toInt()
            var _bulan = bulan.text.toString().toInt()
            var _tahun = tahun.text.toString().toInt()
            val _waktuawal : Long = Calendar.getInstance().run {
                set(_tahun ,_bulan-1, _tanggal, 0, 1)
                timeInMillis
            }
            val _waktuakhir : Long = Calendar.getInstance().run {
                set(_tahun ,_bulan-1 ,_tanggal, 23, 59)
                timeInMillis
            }
            val _eventIntent = Intent(Intent.ACTION_INSERT).apply {
                data = CalendarContract.Events.CONTENT_URI
                putExtra(CalendarContract.Events.TITLE, "CHEAT DAY")
                putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, _waktuawal)
                putExtra(CalendarContract.EXTRA_EVENT_END_TIME, _waktuakhir)
            }

            val cheatday = cheatdayData(_namaUser.toString(), _tahun.toString(), _bulan.toString(), _tanggal.toString())
            db.collection("tbCheatday").document(_namaUser.toString())
                .set(cheatday) // NAMBAH DATA
                .addOnSuccessListener {
                    Log.d("Firebase", "Simpan Data Berhasil")
                }
                .addOnFailureListener{
                    Log.d("Firebase", it.message.toString())
                }
            if(intent.resolveActivity(packageManager) != null){
                startActivity(_eventIntent)
            }
        }
    }
}