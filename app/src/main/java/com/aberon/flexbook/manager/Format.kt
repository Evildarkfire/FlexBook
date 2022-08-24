package com.aberon.flexbook.manager

import com.aberon.flexbook.model.Book
import java.io.InputStream
import java.util.stream.Stream

interface Format {
    fun serialize(inputStream: InputStream): Book?
    fun deserialize(book: Book): Stream<Byte>
}