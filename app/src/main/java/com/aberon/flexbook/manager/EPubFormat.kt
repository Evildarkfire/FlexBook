package com.aberon.flexbook.manager

import com.aberon.flexbook.model.BookInfo
import java.io.File
import java.io.InputStream
import java.util.stream.Stream

class EPubFormat: Format {

    override fun serialize(inputStream: InputStream): BookInfo? {
        val file = File.createTempFile("book", ".fb2")
        inputStream.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return null
    }

    override fun deserialize(book: BookInfo): Stream<Byte> {
        TODO("Not yet implemented")
    }
}