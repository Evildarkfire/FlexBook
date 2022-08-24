package com.aberon.flexbook.store

import android.os.Bundle
import android.support.v4.app.Fragment
import com.aberon.flexbook.ARG_PARAM2
import com.aberon.flexbook.ARG_PARAM3
import com.aberon.flexbook.BookFragment
import com.aberon.flexbook.model.Author
import com.aberon.flexbook.model.Book
import com.aberon.flexbook.model.BookType

class TestStore : Store {
    private var _books: Map<String, Pair<Book, Fragment>> = mutableMapOf()
    private var _i = 16

    init {
        (1..15).forEach { i ->
            val book = Book(
                "Test name $i",
                listOf(
                    Author(
                        "last",
                        "middle",
                        "first",
                        "fullName",
                        "nick",
                        emptyList()
                    )
                ),
                "Test desc $i",
                BookType.FB2,
                null,
                "null",
                emptyList()
            )
            val fragment = BookFragment()
            fragment.arguments = Bundle().apply {
                putString(ARG_PARAM2, book.title)
                putString(ARG_PARAM3, book.description)
            }
            _books = _books + (i.toString() to (book to fragment))
        }
    }

    override val books: Map<String, Pair<Book, Fragment>>
        get() = _books

    override fun addBook(book: Book): Fragment {
        val bookFragment = BookFragment()
        bookFragment.arguments = Bundle().apply {
            putString(ARG_PARAM2, book.title)
            putString(ARG_PARAM3, book.description)
        }
        _books = _books + ((_i++).toString() to (book to bookFragment))
        return bookFragment
    }
}