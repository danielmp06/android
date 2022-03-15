package com.example.projectakhir

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class pgFinal : AppCompatActivity() {
    var dataolahragarekomendasi : ArrayList<dataOlahraga> = ArrayList<dataOlahraga>()
    var datamakananrekomendasi : ArrayList<makanan> = ArrayList<makanan>()
    var datacheatday : ArrayList<cheatdayData> = ArrayList<cheatdayData>()
    lateinit var _lvDataOlahraga : RecyclerView
    lateinit var _lvDataDiet : RecyclerView
    lateinit var _lvCheatDay: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pg_final)
        val db : FirebaseFirestore = FirebaseFirestore.getInstance()
        _lvDataOlahraga = findViewById(R.id.lvDataOlahraga)
        _lvDataDiet = findViewById(R.id.lvDataDiet)
        _lvCheatDay = findViewById(R.id.lvCheatDay)
        var usernama = findViewById<TextView>(R.id.namauser)
        var nilaiImt = findViewById<TextView>(R.id.nilaiIMT)
        var jamtidur = findViewById<TextView>(R.id.jamTidur)
        var submitBtn = findViewById<Button>(R.id.submitBtn)
        var homeBtn = findViewById<Button>(R.id.homeBtn)

        var _namaUser = intent.getStringExtra("namaUser")
        var bb = intent.getStringExtra("beratBadan")
        var tb = intent.getStringExtra("tinggiBadan")
        var totalkalories = intent.getStringExtra("totalKalori")
        var totaljamtidur = intent.getStringExtra("jamtidur")
        var nilaiIMT = 0.0
        var _bb = 0.0
        var _tb = 0.0
        _bb = bb!!.toDouble()
        _tb = tb!!.toDouble()
        nilaiIMT = _bb / (_tb * _tb / 10000)

        nilaiImt.setText(nilaiIMT.toString())
        usernama.setText(_namaUser.toString())
        jamtidur.setText(totaljamtidur.toString())

        readData(db, "OR", _namaUser.toString())

        readData(db, "DIET", _namaUser.toString())

        readData(db, "CD", _namaUser.toString())

        homeBtn.setOnClickListener {
            val intent = Intent(this@pgFinal, pgHome::class.java)
            intent.putExtra("namaUser", _namaUser.toString())
            intent.putExtra("beratBadan", bb.toString())
            intent.putExtra("tinggiBadan", tb.toString())
            intent.putExtra("totalKalori", totalkalories.toString())
            startActivity(intent)
        }

        submitBtn.setOnClickListener {
            var seluruhText = "Rekomendasi Olahraga: \n"
            for(i in dataolahragarekomendasi){
                seluruhText += "Nama: "
                seluruhText += i.nama
                seluruhText += "\nDurasi: "
                seluruhText += i.durasi
                seluruhText += "\nLink: "
                seluruhText += i.link
                seluruhText += "\n\n"
            }
            seluruhText += "\n"
            seluruhText += "Rekomendasi Diet: \n"
            for(i in datamakananrekomendasi){
                seluruhText += "Nama: "
                seluruhText += i.nama
                seluruhText += "\n"
            }
            seluruhText += "\n"
            seluruhText += "Rekomendasi Jam Tidur: "
            seluruhText += totaljamtidur.toString()
            seluruhText += " Jam"
            var text = "Semua rekomendasi telah disimpan"
            Toast.makeText(this@pgFinal, text, Toast.LENGTH_SHORT).show()
            //NYIMPEN
            var temp = 0
            val sdf = SimpleDateFormat("dd/M/yyyy")
            val currentDate = sdf.format(Date())

            for (i in datacheatday){
                if(currentDate.subSequence(3,5).toString().toInt() == i.bulan.toInt()){
                    if(currentDate.subSequence(0,2).toString().toInt() == i.tanggal.toInt()){
                        temp = 32 // start besoknya
                    }
                    else if(currentDate.subSequence(0,2).toString().toInt() + 7 > i.tanggal.toInt()){
                        temp = i.tanggal.toInt() - 1 //berhenti h-1 sblm cheatday
                    }
                }
            }
            if(temp == 0) {
                var _waktuawal: Long = Calendar.getInstance().run {
                    set(
                        currentDate.subSequence(6, 10).toString().toInt(),
                        currentDate.subSequence(3, 5).toString().toInt() - 1,
                        currentDate.subSequence(0, 2).toString().toInt(),
                        0,
                        1
                    )
                    timeInMillis
                }
                var _waktuakhir: Long = Calendar.getInstance().run {
                    set(
                        currentDate.subSequence(6, 10).toString().toInt(),
                        currentDate.subSequence(3, 5).toString().toInt() - 1,
                        currentDate.subSequence(0, 2).toString().toInt() + 7,
                        23,
                        59
                    )
                    timeInMillis
                }

                val _eventIntent = Intent(Intent.ACTION_INSERT).apply {
                    data = CalendarContract.Events.CONTENT_URI
                    putExtra(CalendarContract.Events.TITLE, "Workout and Diet Plan")
                    putExtra(CalendarContract.Events.DESCRIPTION, seluruhText)
                    putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, _waktuawal)
                    putExtra(CalendarContract.EXTRA_EVENT_END_TIME, _waktuakhir)
                }
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(_eventIntent)
                }
            }
            else if(temp == 32){
                var _waktuawal: Long = Calendar.getInstance().run {
                    set(
                        currentDate.subSequence(6, 10).toString().toInt(),
                        currentDate.subSequence(3, 5).toString().toInt() - 1,
                        currentDate.subSequence(0, 2).toString().toInt() + 1,
                        0,
                        1
                    )
                    timeInMillis
                }
                var _waktuakhir: Long = Calendar.getInstance().run {
                    set(
                        currentDate.subSequence(6, 10).toString().toInt(),
                        currentDate.subSequence(3, 5).toString().toInt() - 1,
                        currentDate.subSequence(0, 2).toString().toInt() + 8,
                        23,
                        59
                    )
                    timeInMillis
                }

                val _eventIntent = Intent(Intent.ACTION_INSERT).apply {
                    data = CalendarContract.Events.CONTENT_URI
                    putExtra(CalendarContract.Events.TITLE, "Workout and Diet Plan")
                    putExtra(CalendarContract.Events.DESCRIPTION, seluruhText)
                    putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, _waktuawal)
                    putExtra(CalendarContract.EXTRA_EVENT_END_TIME, _waktuakhir)
                }
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(_eventIntent)
                }
            }
            else{
                var _waktuawal: Long = Calendar.getInstance().run {
                    set(
                        currentDate.subSequence(6, 10).toString().toInt(),
                        currentDate.subSequence(3, 5).toString().toInt() - 1,
                        currentDate.subSequence(0, 2).toString().toInt(),
                        0,
                        1
                    )
                    timeInMillis
                }
                var _waktuakhir: Long = Calendar.getInstance().run {
                    set(
                        currentDate.subSequence(6, 10).toString().toInt(),
                        currentDate.subSequence(3, 5).toString().toInt() - 1,
                        temp,
                        23,
                        59
                    )
                    timeInMillis
                }

                val _eventIntent = Intent(Intent.ACTION_INSERT).apply {
                    data = CalendarContract.Events.CONTENT_URI
                    putExtra(CalendarContract.Events.TITLE, "Workout and Diet Plan")
                    putExtra(CalendarContract.Events.DESCRIPTION, seluruhText)
                    putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, _waktuawal)
                    putExtra(CalendarContract.EXTRA_EVENT_END_TIME, _waktuakhir)
                }
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(_eventIntent)
                }
            }
//            val intent = Intent(this@pgFinal, MainActivity::class.java)
//            startActivity(intent)
        }
    }

    fun readData (db:FirebaseFirestore, jenis: String, nama: String){
        if(jenis == "OR") {
            db.collection("tbRekomendasiOlahraga").get()
                .addOnSuccessListener { result ->
                    dataolahragarekomendasi.clear() // DICLEAR SUPAYA NDA DOBEL
                    for (document in result) {
                        val namaBaru = dataOlahraga(
                            document.data.get("nama").toString(),
                            document.data.get("kalori").toString(),
                            document.data.get("durasi").toString(),
                            document.data.get("link").toString()
                        )
                        dataolahragarekomendasi.add(namaBaru)
                    }
                    _lvDataOlahraga.layoutManager= LinearLayoutManager(this)
                    val Olahraga_adapter = adapter_olahraga(dataolahragarekomendasi)
                    _lvDataOlahraga.adapter = Olahraga_adapter
                }
                .addOnFailureListener {
                    Log.d("Firebase", it.message.toString())
                }
        }
        else if(jenis == "DIET"){
            db.collection("tbRekomendasiMakanan").get()
                .addOnSuccessListener { result ->
                    datamakananrekomendasi.clear() // DICLEAR SUPAYA NDA DOBEL
                    for (document in result) {
                        val namaBaru = makanan(
                            document.data.get("nama").toString(),
                            document.data.get("kalori").toString()
                        )
                        datamakananrekomendasi.add(namaBaru)
                    }
                    _lvDataDiet.layoutManager= LinearLayoutManager(this)
                    val Makanan_adapter = adapter_makanan(datamakananrekomendasi)
                    _lvDataDiet.adapter = Makanan_adapter
                }
                .addOnFailureListener {
                    Log.d("Firebase", it.message.toString())
                }
        }
        else{
            db.collection("tbCheatday").get()
                .addOnSuccessListener { result ->
                    datacheatday.clear() // DICLEAR SUPAYA NDA DOBEL
                    for (document in result) {
                        val namaBaru = cheatdayData(
                            document.data.get("nama").toString(),
                            document.data.get("tahun").toString(),
                            document.data.get("bulan").toString(),
                            document.data.get("tanggal").toString()
                        )
                        if(namaBaru.nama == nama){
                            datacheatday.add(namaBaru)
                        }
                    }
                    _lvCheatDay.layoutManager= LinearLayoutManager(this)
                    val cheatday_adapter = adapter_cheatday(datacheatday)
                    _lvCheatDay.adapter = cheatday_adapter
                }
                .addOnFailureListener {
                    Log.d("Firebase", it.message.toString())
                }
        }
    }

}