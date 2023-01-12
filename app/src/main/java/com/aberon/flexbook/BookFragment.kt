package com.aberon.flexbook

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.aberon.flexbook.databinding.FragmentBookBinding
import com.aberon.flexbook.model.Book

const val FRAGMENT_BOOK_PARAM = "book"

class BookFragment : Fragment() {
    private var book: Book? = null
    private lateinit var binding: FragmentBookBinding
    private lateinit var bookName: TextView
    private lateinit var bookDescription: TextView
    private lateinit var bookCover: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            book = it.getParcelable(FRAGMENT_BOOK_PARAM)
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
                putExtra(READER_BOOK_PARAM, book)
            }
            startActivity(intent)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (book != null) {
            bookName.text = book!!.title
            bookDescription.text = book!!.description
            book!!.covers?.lastOrNull()?.apply {
                bookCover.setImageBitmap(BitmapFactory.decodeFile(this))
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
        fun newInstance(param1: Book) =
            BookFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(FRAGMENT_BOOK_PARAM, param1)
                }
            }
    }
}