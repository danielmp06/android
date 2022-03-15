package com.example.projectakhir

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class cheatdayData(
    var nama : String,
    var tahun : String,
    var bulan : String,
    var tanggal : String
) : Parcelable
