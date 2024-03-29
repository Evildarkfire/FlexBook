package com.aberon.flexbook.ui.books

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.ContextMenu
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.aberon.flexbook.R
import com.aberon.flexbook.ui.reader.ReaderActivity
import com.aberon.flexbook.databinding.FragmentBookBinding
import com.aberon.flexbook.model.BookInfo
import com.aberon.flexbook.store.SQLStore

class BookFragment : Fragment() {
    companion object {
        const val FRAGMENT_BOOK_PARAM = "book"
        const val MENU_DELETE = 11111
    }

    private lateinit var binding: FragmentBookBinding
    private lateinit var store: SQLStore

    private var bookInfo: BookInfo? = null
    private lateinit var bookName: TextView
    private lateinit var bookAuthor: TextView
    private lateinit var bookCover: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        store = SQLStore.getInstance(requireContext())

        arguments?.let { bundle ->
            bundle.getString(FRAGMENT_BOOK_PARAM)?.let { id ->
                bookInfo = store.getBookById(id)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookBinding.inflate(inflater, container, false)
        bookName = binding.bookName
        bookAuthor = binding.bookAuthor
        bookCover = binding.bookCover
        val view = binding.root
        view.setOnClickListener {
            val intent = Intent(activity, ReaderActivity::class.java).apply {
                bookInfo?.let { info ->
                    putExtra(ReaderActivity.READER_BOOK_PARAM, info.book.bookId)
                }
            }
            startActivity(intent)
        }
        registerForContextMenu(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (bookInfo != null) {
            bookInfo?.let { info ->
                bookName.text = info.book.title
                info.authors.firstOrNull()?.let { author ->
                    bookAuthor.text = author.author.fullName
                }
                info.covers.lastOrNull()?.apply {
                    bookCover.setImageBitmap(BitmapFactory.decodeFile(coverPath))
                }
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menu.add(0, MENU_DELETE, 0, R.string.delete).setOnMenuItemClickListener {
            bookInfo?.book?.let { it1 -> store.deleteBook(it1) }
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.remove(this)
            fragmentTransaction.commit()
            true
        }
    }
}