package com.aberon.flexbook.manager

import com.aberon.flexbook.model.BookInfo
import java.io.InputStream
import java.util.stream.Stream

interface Format {
    fun serialize(inputStream: InputStream): BookInfo?
    fun deserialize(book: BookInfo): Stream<Byte>
}