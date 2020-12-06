package com.imb.jobtop.utils

import androidx.core.graphics.toColorInt
import java.util.*

object Colors {
    fun getRandomColor(): Int {
        val list = listOf(
            "#E1F5EA",
            "#F2F5F7",
            "#EBDCF9",
        )
        return list[Random().nextInt(3)].toColorInt()
    }
}