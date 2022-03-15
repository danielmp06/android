package com.example.projectakhir

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore

class pgRegister : AppCompatActivity() {
    var dataUsers : ArrayList<dataUser> = ArrayList<dataUser>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pg_register)
        val db : FirebaseFirestore = FirebaseFirestore.getInstance()

        var back = findViewById<ImageButton>(R.id.backBtn)
        var nama = findViewById<EditText>(R.id.nama)
        var submit = findViewById<Button>(R.id.submit)

        back.setOnClickListener {
            val intent = Intent(this@pgRegister, MainActivity::class.java)
            startActivity(intent)
        }

        submit.setOnClickListener {
            AlertDialog.Builder(this@pgRegister)
                .setTitle("Create Account!")
                .setMessage("Akun "+ nama.text.toString() +" akan dibuat?")
                .setPositiveButton(
                    "Confirm",
                    DialogInterface.OnClickListener{ dialog, which ->
                        TambahData(db, nama.text.toString())
                        readData(db)
                        val intent = Intent(this@pgRegister, MainActivity::class.java)
                        startActivity(intent)
                    })

                .setNegativeButton(
                    "Decline",
                    DialogInterface.OnClickListener{ dialog, which ->
                        Toast.makeText(
                            this@pgRegister,
                            "Akun "+ nama.text.toString() + "tidak jadi dibuat.",
                            Toast.LENGTH_SHORT).show()
                    })
                .show()
        }


    }

    fun TambahData(db: FirebaseFirestore, nama: String){
        val namaBaru = dataUser(nama, "0", "0", "0")
        db.collection("tbUser").document(nama)
            .set(namaBaru) // NAMBAH DATA
            .addOnSuccessListener {
                readData(db) // REFRESH ARRAYLIST DATANAMA
                Log.d("Firebase", "Simpan Data Berhasil")
            }
            .addOnFailureListener{
                Log.d("Firebase", it.message.toString())
            }
        val isicheatday = cheatdayData(nama, "0","0","0")
        db.collection("tbCheatday").document(nama)
            .set(isicheatday) // NAMBAH DATA
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