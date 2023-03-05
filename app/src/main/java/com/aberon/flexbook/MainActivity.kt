package com.aberon.flexbook

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.aberon.flexbook.manager.FB2Format
import com.aberon.flexbook.model.BookInfo
import com.aberon.flexbook.store.SQLStore
import com.aberon.flexbook.tool.Permissions
import com.aberon.flexbook.tool.RequestCodes


class MainActivity : AppCompatActivity() {
    private lateinit var store: SQLStore
    private lateinit var permissions: Permissions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        permissions = Permissions(this)
        store = SQLStore.getInstance(this)
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        store.books.forEach { bookInfo ->
            fragmentTransaction.add(R.id.booksList, createBookFragment(bookInfo), bookInfo.book.bookId)
        }
        fragmentTransaction.commit()
    }

    fun onClickAddBook(view: View) {
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
        val fragmentManager = supportFragmentManager
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
                    applicationContext,
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
                    contentResolver.openInputStream(data.data!!)
                        ?.let { inputStream -> FB2Format(this).serialize(inputStream) }
                        ?.let { bookInfo -> addBookFragment(bookInfo) }
                } else {
                    //TODO API < 29
                }
            } else {
                Log.d("OPEN_FILE_REQUEST_CODE", "File uri not found {${resultCode}}")
            }
        }
    }
}