package com.aberon.flexbook.store

import android.os.Bundle
import android.support.v4.app.Fragment
import com.aberon.flexbook.FRAGMENT_BOOK_PARAM
import com.aberon.flexbook.BookFragment
import com.aberon.flexbook.model.Author
import com.aberon.flexbook.model.Book
import com.aberon.flexbook.model.BookType

class TestStore : Store {
    companion object {
        private var booksStore: Map<String, Pair<Book, Fragment>> = mutableMapOf() // TODO remove fragment from store
    }

    private var _i = 3

    init {
        (1..3).forEach { i ->
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
                emptyList(),
                emptyMap()
            )
            val fragment = BookFragment()
            fragment.arguments = Bundle().apply {
                putParcelable(FRAGMENT_BOOK_PARAM, book)
            }
            booksStore = booksStore + (i.toString() to (book to fragment))
        }
    }

    override val books: Map<String, Pair<Book, Fragment>>
        get() = booksStore

    override fun addBook(book: Book): Fragment {
        val bookFragment = BookFragment()
        bookFragment.arguments = Bundle().apply {
            putParcelable(FRAGMENT_BOOK_PARAM, book)
        }
        booksStore = booksStore + ((_i++).toString() to (book to bookFragment))
        return bookFragment
    }
}