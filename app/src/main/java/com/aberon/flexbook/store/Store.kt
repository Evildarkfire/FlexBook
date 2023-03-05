package com.aberon.flexbook.store

import androidx.fragment.app.Fragment
import com.aberon.flexbook.model.Book

interface Store {
    val books: Map<String, Pair<Book, Fragment>>

    fun addBook(book: Book): Fragment
}