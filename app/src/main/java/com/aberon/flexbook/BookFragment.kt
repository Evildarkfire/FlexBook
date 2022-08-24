package com.aberon.flexbook

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

const val ARG_PARAM1 = "bookCover"
const val ARG_PARAM2 = "bookName"
const val ARG_PARAM3 = "bookDescription"

/**
 * A simple [Fragment] subclass.
 * Use the [BookFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BookFragment : Fragment() {
    private var bookCover: String? = null
    private var bookName: String? = null
    private var bookDescription: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            bookCover = it.getString(ARG_PARAM1)
            bookName = it.getString(ARG_PARAM2)
            bookDescription = it.getString(ARG_PARAM3)
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
        view.findViewById<TextView>(R.id.bookName).text = bookName
        view.findViewById<TextView>(R.id.bookDescription).text = bookDescription
        //TODO view.findViewById<ImageView>(R.id.bookCover).setImageBitmap()
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @param param3 Parameter 3.
         * @return A new instance of fragment BookFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String, param3: String) =
            BookFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                    putString(ARG_PARAM3, param3)
                }
            }
    }
}