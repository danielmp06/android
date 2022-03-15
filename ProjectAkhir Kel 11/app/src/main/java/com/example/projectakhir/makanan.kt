package com.example.projectakhir

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class makanan(
    var nama : String,
    var kalori : String
) : Parcelable
