package com.aberon.flexbook.ui.reader

import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.aberon.flexbook.databinding.ActivityReaderBinding
import com.aberon.flexbook.ui.pages.ViewPageAdapter

class ReaderActivity : AppCompatActivity() {
    companion object {
        const val READER_BOOK_PARAM = "book"
    }

    private lateinit var binding: ActivityReaderBinding
    private lateinit var readerContent: ViewPager2
    private lateinit var blanck: TextView
    private var bookId: String? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReaderBinding.inflate(layoutInflater)
        readerContent = binding.readerContent
        blanck = binding.pageBlanck
        blanck.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        setContentView(binding.root)

        intent.getStringExtra(READER_BOOK_PARAM)?.let {
            bookId = it
            val adapter = ViewPageAdapter(this, blanck, it)
            readerContent.adapter = adapter
        }
    }
}