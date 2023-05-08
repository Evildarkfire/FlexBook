package com.aberon.flexbook.ui.books

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
import com.aberon.flexbook.manager.Format
import com.aberon.flexbook.model.BookInfo
import com.aberon.flexbook.store.FilesStore
import com.aberon.flexbook.store.SQLStore
import com.aberon.flexbook.tool.Permissions

class BooksFragment : Fragment() {
    companion object {
        const val OPEN_FILE_REQUEST_CODE = 111
        const val PERMISSION_REQUEST_CODE = 112
    }

    private lateinit var binding: FragmentBooksBinding

    private lateinit var permissions: Permissions
    private lateinit var store: SQLStore
    private lateinit var filesStore: FilesStore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBooksBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.addBook.setOnClickListener { onClickAddBook() }

        permissions = Permissions(activity!!.applicationContext)
        store = SQLStore.getInstance(activity!!.applicationContext)
        filesStore = FilesStore(activity!!.applicationContext)
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        store.books.forEach { bookInfo ->
            fragmentTransaction.add(
                R.id.booksList,
                createBookFragment(bookInfo),
                bookInfo.book.bookId
            )
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
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
            putExtra(
                Intent.EXTRA_MIME_TYPES,
                arrayOf("application/octet-stream", "application/epub+zip")
            )
        }
        startActivityForResult(intent, OPEN_FILE_REQUEST_CODE)
    }

    private fun saveBook(bookInfo: BookInfo) {
        bookInfo.covers.forEach { cover ->
            cover.coverPath = filesStore.writeCover(cover.coverBytes!!).absolutePath
        }
        store.addBook(bookInfo)
    }

    private fun addBookFragment(bookInfo: BookInfo) {
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.booksList, createBookFragment(bookInfo), bookInfo.book.bookId)
        fragmentTransaction.commit()
    }

    private fun createBookFragment(bookInfo: BookInfo): BookFragment {
        val bookFragment = BookFragment()
        bookFragment.arguments = Bundle().apply {
            putString(BookFragment.FRAGMENT_BOOK_PARAM, bookInfo.book.bookId)
        }
        return bookFragment
    }

    private fun requestPermissions() {
        requestPermissions(Permissions.permissions, PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResultsrequestCode: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResultsrequestCode)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (
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
        if (requestCode == OPEN_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data?.data != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val contentResolver = activity!!.contentResolver!!
                    val type = contentResolver.getType(data.data!!)
                    val inputStream = contentResolver.openInputStream(data.data!!)
                    if (type != null && inputStream != null) {
                        val file = filesStore.writeBook(inputStream.readBytes())
                        val bookInfo = Format.serialize(type, file)
                        saveBook(bookInfo!!) //TODO exception
                        addBookFragment(bookInfo)
                    }
                } else {
                    //TODO API < 29
                }
            } else {
                Log.d("OPEN_FILE_REQUEST_CODE", "File uri not found {${resultCode}}")
            }
        }
    }
}