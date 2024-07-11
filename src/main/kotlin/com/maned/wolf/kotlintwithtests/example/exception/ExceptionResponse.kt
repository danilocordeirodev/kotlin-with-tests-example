package com.maned.wolf.kotlintwithtests.example.exception

import java.io.Serializable
import java.util.Date

class ExceptionResponse(val timestamp: Date, val message: String?, val details: String) : Serializable {

    companion object {
        private const val serialVersionUID = 1L
    }
}