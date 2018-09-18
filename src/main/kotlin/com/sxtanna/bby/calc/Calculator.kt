package com.sxtanna.bby.calc

import com.sxtannna.calc.CalcApp

object Calculator { // this exists as a buffer in case I want to change the backend of how this works

    fun exec(input: String): String {
        return CalcApp.exec(input)
    }

}