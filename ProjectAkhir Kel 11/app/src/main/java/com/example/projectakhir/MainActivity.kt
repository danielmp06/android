package com.example.projectakhir

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity() {

    private lateinit var _lvData : RecyclerView

    var dataUsers : ArrayList<dataUser> = ArrayList<dataUser>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db : FirebaseFirestore = FirebaseFirestore.getInstance()
        _lvData = findViewById(R.id.lvDatauser)
        var registerBtn = findViewById<Button>(R.id.register)

        readData(db)

        registerBtn.setOnClickListener {
            val intent = Intent(this@MainActivity, pgRegister::class.java)
            startActivity(intent)
        }

    }

    fun readData (db:FirebaseFirestore){
        db.collection("tbUser").get()
            .addOnSuccessListener { result ->
                dataUsers.clear() // DICLEAR SUPAYA NDA DOBEL
                for (document in result){
                    val namaBaru = dataUser(document.data.get("nama").toString(), document.data.get("bb").toString(), document.data.get("tb").toString(), document.data.get("totalkalori").toString()) //AMBIL SEMUA DATA DI DATABASE SATU-SATU
                    dataUsers.add(namaBaru) // DI ADD KE ARRAYLIST DATANAMA
                }
                _lvData.layoutManager= LinearLayoutManager(this)
                val Login_adapter = adapter_login(dataUsers)
                _lvData.adapter = Login_adapter

                Login_adapter.setOnItemClickCallback(object : adapter_login.OnItemClickCallback{
                    override fun onItemClicked(data:dataUser){
                        val intent = Intent(this@MainActivity, pgHome::class.java)
                        intent.putExtra("namaUser",data.nama)
                        intent.putExtra("beratBadan",data.bb)
                        intent.putExtra("tinggiBadan",data.tb)
                        intent.putExtra("totalKalori",data.totalkalori)
                        startActivity(intent)
                    }

                    override fun deleteAkun(data: dataUser) {
                        db.collection("tbUser").document(data.nama)
                            .delete()
                        readData(db)
                    }

                })
            }
            .addOnFailureListener{
                Log.d("Firebase", it.message.toString())
            }
    }

    fun getUser (db:FirebaseFirestore, nama: String){
        db.collection("tbUser").get()
            .addOnSuccessListener { result ->
                dataUsers.clear() // DICLEAR SUPAYA NDA DOBEL
                for (document in result){
                    val namaBaru = dataUser(document.data.get("nama").toString(), document.data.get("bb").toString(), document.data.get("tb").toString(), document.data.get("totalkalori").toString()) //AMBIL SEMUA DATA DI DATABASE SATU-SATU
                    dataUsers.add(namaBaru) // DI ADD KE ARRAYLIST DATANAMA
                }
            }
            .addOnFailureListener{
                Log.d("Firebase", it.message.toString())
            }
    }
}