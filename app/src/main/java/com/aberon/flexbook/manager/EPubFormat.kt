package com.aberon.flexbook.manager

import com.aberon.flexbook.model.Book
import java.io.File
import java.io.InputStream
import java.util.stream.Stream

class EPubFormat: Format {
    //private val folioReader = FolioReader.get()

    override fun serialize(inputStream: InputStream): Book? {
        val file = File.createTempFile("book", ".fb2")
        inputStream.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        //val book = folioReader.openBook(file.absolutePath)
        return null
    }

    override fun deserialize(book: Book): Stream<Byte> {
        TODO("Not yet implemented")
    }
}