package com.example.projectakhir

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class dataOlahraga(
    var nama : String,
    var kalori : String,
    var durasi : String,
    var link : String
) : Parcelable
