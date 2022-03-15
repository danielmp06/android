package com.example.projectakhir

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
var tempreset = 0
class pgHome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pg_home)

        val db : FirebaseFirestore = FirebaseFirestore.getInstance()
        var pgbbtb = findViewById<Button>(R.id.pgBbTb)
        var cheatday = findViewById<Button>(R.id.pgCheatday)
        var inputmakanan = findViewById<Button>(R.id.pgMakanan)
        var olahraga = findViewById<Button>(R.id.pgOlahraga)
        var backtologin = findViewById<Button>(R.id.backtoLogin)
        var resetbutton = findViewById<ImageButton>(R.id.resetBtn)

        var namauseraktif = findViewById<TextView>(R.id.namauser)
        var beratbadanuser = findViewById<TextView>(R.id.beratbadanuser)
        var tinggibadanuser = findViewById<TextView>(R.id.tinggibadanuser)
        var totalkaloriuser = findViewById<TextView>(R.id.totalkaloriuser)

        var namauser = intent.getStringExtra("namaUser")
        var bb = intent.getStringExtra("beratBadan")
        var tb = intent.getStringExtra("tinggiBadan")
        var totalkalori = intent.getStringExtra("totalKalori")

//        db.collection("tbUser").get()
//            .addOnSuccessListener { result ->
//                for (document in result){
//                    val namaBaru = dataUser(document.data.get("nama").toString(), document.data.get("bb").toString(), document.data.get("tb").toString(), document.data.get("totalkalori").toString())
//                    if(namaBaru.nama == namauser.toString()){
//                        bb = namaBaru.bb
//                        tb = namaBaru.tb
//                        totalkalori = namaBaru.totalkalori
//                    }
//                }
//            }
//            .addOnFailureListener{
//                Log.d("Firebase", it.message.toString())
//            }

        //Toast.makeText(this@pgHome, bb.toString(), Toast.LENGTH_SHORT).show()
        namauseraktif.setText(namauser.toString())
        beratbadanuser.setText(bb.toString())
        tinggibadanuser.setText(tb.toString())
        totalkaloriuser.setText(totalkalori.toString())

        backtologin.setOnClickListener {
            AlertDialog.Builder(this@pgHome)
                .setTitle("Logout!")
                .setMessage("Anda akan Logout dari "+ namauser.toString() +"?")
                .setPositiveButton(
                    "Confirm",
                    DialogInterface.OnClickListener{ dialog, which ->
                        val intent = Intent(this@pgHome, MainActivity::class.java)
                        startActivity(intent)
                    })

                .setNegativeButton(
                    "Decline",
                    DialogInterface.OnClickListener{ dialog, which ->
                    })
                .show()

        }

        pgbbtb.setOnClickListener{
            var temp = 1
            if(bb.toString() == "0" && tb.toString() == "0"){
                temp = 0
            }
            if(temp == 0) {
                val intent = Intent(this@pgHome, pgBbTb::class.java)
                intent.putExtra("namaUser", namauser)
                intent.putExtra("beratBadan", bb)
                intent.putExtra("tinggiBadan", tb)
                intent.putExtra("totalKalori", totalkalori)
                startActivity(intent)
            }
            else{
                Toast.makeText(this@pgHome, "BB dan TB sudah ada nilai", Toast.LENGTH_SHORT).show()
            }
        }

        cheatday.setOnClickListener{
            val intent = Intent(this@pgHome, pgCheatday::class.java)
            intent.putExtra("namaUser", namauser)
            intent.putExtra("beratBadan", bb)
            intent.putExtra("tinggiBadan", tb)
            intent.putExtra("totalKalori", totalkalori)
            startActivity(intent)
        }

        inputmakanan.setOnClickListener{
            var temp = 1
            if(totalkalori.toString() == "0"){
                temp = 0
            }
            if(temp == 0) {
                val intent = Intent(this@pgHome, pgInputMakanan::class.java)
                intent.putExtra("namaUser", namauser)
                intent.putExtra("beratBadan", bb)
                intent.putExtra("tinggiBadan", tb)
                intent.putExtra("totalKalori", totalkalori)
                startActivity(intent)
            }
            else{
                Toast.makeText(this@pgHome, "Total Kalori sudah ada nilai", Toast.LENGTH_SHORT).show()
            }
        }

        resetbutton.setOnClickListener {
            AlertDialog.Builder(this@pgHome)
                .setTitle("Reset Data")
                .setMessage("Apakah benar data "+ namauser.toString() +" akan direset?")
                .setPositiveButton(
                    "Confirm",
                    DialogInterface.OnClickListener{ dialog, which ->
                        var resetdata = dataUser(
                            namauser.toString(),
                            "0",
                            "0",
                            "0"
                        )
                        db.collection("tbUser").document(namauser.toString())
                            .set(resetdata) // NAMBAH DATA
                            .addOnSuccessListener {
                                Log.d("Firebase", "Simpan Data Berhasil")
                                Toast.makeText(this@pgHome, "Reset Data Berhasil", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener{
                                Log.d("Firebase", it.message.toString())
                            }
                        val intent = Intent(this@pgHome, MainActivity::class.java)
                        startActivity(intent)
                    })

                .setNegativeButton(
                    "Decline",
                    DialogInterface.OnClickListener{ dialog, which ->
                        Toast.makeText(
                            this@pgHome,
                            "Data "+ namauser.toString() + " tidak jadi dihapus.",
                            Toast.LENGTH_SHORT).show()
                    })
                .show()
        }

        olahraga.setOnClickListener{
            val intent = Intent(this@pgHome, pgOlahraga::class.java)
            intent.putExtra("namaUser", namauser)
            intent.putExtra("beratBadan", bb)
            intent.putExtra("tinggiBadan", tb)
            intent.putExtra("totalKalori", totalkalori)
            startActivity(intent)
        }
    }
}