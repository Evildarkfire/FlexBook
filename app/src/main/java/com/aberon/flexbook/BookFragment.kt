package com.aberon.flexbook

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.aberon.flexbook.model.Book

const val ARG_PARAM1 = "book"

class BookFragment : Fragment() {
    private var book: Book? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            book = it.getParcelable<Book>(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_book, container, false).apply {
            setOnClickListener {
                //TODO open book
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (book != null) {
            view.findViewById<TextView>(R.id.bookName).text = book!!.title
            view.findViewById<TextView>(R.id.bookDescription).text = book!!.description
            book!!.image?.let { image ->
                view.findViewById<ImageView>(R.id.bookCover).apply {
                    setImageBitmap(image)
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
        fun newInstance(param1: Book) =
            BookFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, param1)
                }
            }
    }
}