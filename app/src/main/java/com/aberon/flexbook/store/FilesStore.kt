package com.aberon.flexbook.store

import android.content.Context
import java.io.File
import java.util.UUID

class FilesStore(context: Context) {
    private val COVERS_DIR = File("${context.applicationInfo.dataDir}/covers").apply {
        if (!exists()) {
            mkdir()
        }
    }

    private val BOOK_DIR = File("${context.applicationInfo.dataDir}/book").apply {
        if (!exists()) {
            mkdir()
        }
    }

    fun writeBook(byteArray: ByteArray): File {
        return writeBytes("${BOOK_DIR}/${UUID.randomUUID()}", byteArray)
    }

    fun writeCover(byteArray: ByteArray): File {
        return writeBytes("${COVERS_DIR}/${UUID.randomUUID()}", byteArray)
    }

    private fun writeBytes(path: String, byteArray: ByteArray): File {
        return File(path).apply {
            createNewFile()
            writeBytes(byteArray)
        }
    }
}