package com.aberon.flexbook.ui.book

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.aberon.flexbook.R
import com.aberon.flexbook.databinding.FragmentBooksBinding
import com.aberon.flexbook.manager.FB2Format
import com.aberon.flexbook.model.BookInfo
import com.aberon.flexbook.store.SQLStore
import com.aberon.flexbook.tool.Permissions
import com.aberon.flexbook.tool.RequestCodes

class BooksFragment : Fragment() {

    private var _binding: FragmentBooksBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var store: SQLStore
    private lateinit var permissions: Permissions

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBooksBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.addBook.setOnClickListener { onClickAddBook() }

        permissions = Permissions(activity!!.applicationContext)
        store = SQLStore.getInstance(activity!!.applicationContext)
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        store.books.forEach { bookInfo ->
            fragmentTransaction.add(R.id.booksList, createBookFragment(bookInfo), bookInfo.book.bookId)
        }
        fragmentTransaction.commit()

        return root
    }

    private fun onClickAddBook() {
        if (permissions.checkPermissions()) {
            openBookFromFile()
        } else {
            requestPermissions()
        }
    }

    private fun openBookFromFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/*"
        }
        startActivityForResult(intent, RequestCodes.OPEN_FILE_REQUEST_CODE)
    }

    private fun addBookFragment(bookInfo: BookInfo) {
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        store.addBook(bookInfo)
        fragmentTransaction.add(R.id.booksList, createBookFragment(bookInfo), bookInfo.book.bookId)
        fragmentTransaction.commit()
    }

    private fun createBookFragment(bookInfo: BookInfo): BookFragment {
        val bookFragment = BookFragment()
        bookFragment.arguments = Bundle().apply {
            putString(FRAGMENT_BOOK_PARAM, bookInfo.book.bookId)
        }
        return bookFragment
    }

    private fun requestPermissions() {
        requestPermissions(Permissions.permissions, RequestCodes.PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResultsrequestCode: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResultsrequestCode)
        when (requestCode) {
            RequestCodes.PERMISSION_REQUEST_CODE -> if (
                grantResultsrequestCode.any { result -> result == PackageManager.PERMISSION_GRANTED }
            ) {
                openBookFromFile()
            } else {
                Toast.makeText(
                    activity!!.applicationContext,
                    "Нет разрешения на чтение",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            else ->
                return
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestCodes.OPEN_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data?.data != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    activity?.contentResolver?.openInputStream(data.data!!)
                        ?.let { inputStream -> FB2Format(activity!!.applicationContext).serialize(inputStream) }
                        ?.let { bookInfo -> addBookFragment(bookInfo) }
                } else {
                    //TODO API < 29
                }
            } else {
                Log.d("OPEN_FILE_REQUEST_CODE", "File uri not found {${resultCode}}")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}