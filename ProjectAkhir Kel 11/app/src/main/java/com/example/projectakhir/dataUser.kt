package com.example.projectakhir

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class dataUser(
    var nama : String,
    var bb : String,
    var tb : String,
    var totalkalori : String
) : Parcelable
