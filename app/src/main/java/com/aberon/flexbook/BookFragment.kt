package com.aberon.flexbook

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.aberon.flexbook.databinding.FragmentBookBinding
import com.aberon.flexbook.model.BookInfo
import com.aberon.flexbook.store.SQLStore

const val FRAGMENT_BOOK_PARAM = "book"

class BookFragment : Fragment() {
    private lateinit var binding: FragmentBookBinding
    private lateinit var store: SQLStore

    private var bookInfo: BookInfo? = null
    private lateinit var bookName: TextView
    private lateinit var bookDescription: TextView
    private lateinit var bookCover: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        store = SQLStore.getInstance(requireContext())

        arguments?.let { bundle ->
            bundle.getString(FRAGMENT_BOOK_PARAM)?.let { id->
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
        bookDescription = binding.bookDescription
        bookCover = binding.bookCover
        val view = binding.root
        view.setOnClickListener {
            val intent = Intent(activity, ReaderActivity::class.java).apply {
                bookInfo?.let { info ->
                    putExtra(READER_BOOK_PARAM, info.book.bookId)
                }
            }
            startActivity(intent)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (bookInfo != null) {
            bookInfo?.let { info ->
                bookName.text = info.book.title
                bookDescription.text = info.book.description
                info.covers.lastOrNull()?.apply {
                    bookCover.setImageBitmap(BitmapFactory.decodeFile(coverPath))
                }
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @param param1 model Book.
         * @return A new instance of fragment BookFragment.
         */
        @JvmStatic
        fun newInstance(param1: Int) =
            BookFragment().apply {
                arguments = Bundle().apply {
                    putInt(FRAGMENT_BOOK_PARAM, param1)
                }
            }
    }
}