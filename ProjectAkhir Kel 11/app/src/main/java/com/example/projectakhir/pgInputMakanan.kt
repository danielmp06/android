package com.example.projectakhir

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class pgInputMakanan : AppCompatActivity() {
    var totalkalori = 0
    private lateinit var _lvData:RecyclerView
    var dataMakanan : ArrayList<makanan> = ArrayList<makanan>()
    private lateinit var totalkal:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pg_input_makanan)

        val db : FirebaseFirestore = FirebaseFirestore.getInstance()
        _lvData = findViewById(R.id.lvData)
        val addnewdata = findViewById<Button>(R.id.addData)
        totalkal = findViewById(R.id.totalkalori)
        var next = findViewById<Button>(R.id.nextBtn)
        var resetkalori = findViewById<Button>(R.id.resetKaloriBtn)
        totalkal.setText(totalkalori.toString())

        val _namaUser = intent.getStringExtra("namaUser")
        val _beratBadan = intent.getStringExtra("beratBadan")
        val _tinggiBadan = intent.getStringExtra("tinggiBadan")
        readData(db)

        addnewdata.setOnClickListener {
            val intent = Intent(this@pgInputMakanan, addMakanan::class.java)
            startActivity(intent)
        }

        resetkalori.setOnClickListener {
            totalkalori = 0
        }

        next.setOnClickListener {
            var temp = 1
            if(_beratBadan.toString() == "0"){
                temp = 0
            }
            if(_tinggiBadan.toString() == "0"){
                temp = 0
            }
            if(temp == 0){
                val intent = Intent(this@pgInputMakanan, pgBbTb::class.java)
                intent.putExtra("namaUser", _namaUser.toString())
                intent.putExtra("beratBadan", _beratBadan.toString())
                intent.putExtra("tinggiBadan", _tinggiBadan.toString())
                intent.putExtra("totalKalori", totalkalori.toString())
                startActivity(intent)
            }
            else{
                val intent = Intent(this@pgInputMakanan, pgOlahraga::class.java)
                intent.putExtra("namaUser", _namaUser.toString())
                intent.putExtra("beratBadan", _beratBadan.toString())
                intent.putExtra("tinggiBadan", _tinggiBadan.toString())
                intent.putExtra("totalKalori", totalkalori.toString())
                startActivity(intent)
            }
        }
    }



    fun readData (db:FirebaseFirestore){
        db.collection("tbMakanan").get()
            .addOnSuccessListener { result ->
                dataMakanan.clear() // DICLEAR SUPAYA NDA DOBEL
                for (document in result){
                    val namaBaru = makanan(document.data.get("nama").toString(), document.data.get("kalori").toString()) //AMBIL SEMUA DATA DI DATABASE SATU-SATU
                    dataMakanan.add(namaBaru) // DI ADD KE ARRAYLIST DATANAMA
                }
                _lvData.layoutManager= LinearLayoutManager(this)
                val Makanan_adapter = adapter_makanan(dataMakanan)
                _lvData.adapter = Makanan_adapter

                Makanan_adapter.setOnItemClickCallback(object : adapter_makanan.OnItemClickCallback{
                    override fun onItemClicked(data: makanan) {
                        val _namaUser = intent.getStringExtra("namaUser")
                        val _beratBadan = intent.getStringExtra("beratBadan")
                        val _tinggiBadan = intent.getStringExtra("tinggiBadan")
                        tambahKalori(db, data.nama)
                        val userdata = dataUser(
                            _namaUser.toString(),
                            _beratBadan.toString(),
                            _tinggiBadan.toString(),
                            totalkalori.toString()
                        )
                        db.collection("tbUser").document(userdata.nama)
                            .set(userdata) // NAMBAH DATA
                            .addOnSuccessListener {
                                readData(db) // REFRESH ARRAYLIST DATANAMA
                                Log.d("Firebase", "Simpan Data Berhasil")
                            }
                            .addOnFailureListener{
                                Log.d("Firebase", it.message.toString())
                            }
                    }
                })
            }
            .addOnFailureListener{
                Log.d("Firebase", it.message.toString())
            }
    }

//    fun HapusData(db: FirebaseFirestore, nama: String){
//        db.collection("tbMakanan").document(nama)
//            .delete() //DELETE DOCUMENT YANG DIPILIH
//            .addOnSuccessListener {
//                readData(db) // REFRESH ARRAYLIST DATANAMA
//            }
//            .addOnFailureListener{
//                Log.d("Firebase", it.message.toString())
//            }
//    }

    fun tambahKalori(db: FirebaseFirestore, nama: String){
        db.collection("tbMakanan").get()
            .addOnSuccessListener { result ->
                for (document in result){
                    val namaBaru = makanan(document.data.get("nama").toString(), document.data.get("kalori").toString())
                    if(namaBaru.nama == nama){
                        totalkalori = totalkalori + namaBaru.kalori.toInt()
                        //Toast.makeText(this@pgInputMakanan, totalkalori.toString(),Toast.LENGTH_SHORT).show()
                    }
                }
                totalkal.setText(totalkalori.toString())
            }
            .addOnFailureListener{
                Log.d("Firebase", it.message.toString())
            }
    }
}