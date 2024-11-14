package com.example.zeeshanassignment

import java.io.InputStreamReader

class MockJsonResponseFileReader(path: String) {

    val strContent: String

    init {
        val reader = InputStreamReader(this.javaClass.classLoader?.getResourceAsStream(path))
        strContent = reader.readText()
        reader.close()
    }
}