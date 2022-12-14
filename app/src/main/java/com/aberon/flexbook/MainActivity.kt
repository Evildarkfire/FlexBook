package com.aberon.flexbook

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.aberon.flexbook.manager.FB2Format
import com.aberon.flexbook.model.Book
import com.aberon.flexbook.store.TestStore
import com.aberon.flexbook.tool.Permissions
import com.aberon.flexbook.tool.RequestCodes


class MainActivity : AppCompatActivity() {
    private val bocksStore = TestStore()
    private val permissions = Permissions(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        bocksStore.books.forEach {
            fragmentTransaction.add(R.id.booksList, it.value.second, it.key)
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

    private fun addBookFragment(book: Book) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        val bookFragment = bocksStore.addBook(book)
        fragmentTransaction.add(R.id.booksList, bookFragment, book.toString())

        fragmentTransaction.commit()
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
                    "?????? ???????????????????? ???? ????????????",
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
                        ?.let { inputStream -> FB2Format().serialize(inputStream) }
                        ?.let { book -> addBookFragment(book) }
                } else {
                    //TODO API < 29
                }
            } else {
                Log.d("OPEN_FILE_REQUEST_CODE", "File uri not found {}")
            }
        }
    }
}