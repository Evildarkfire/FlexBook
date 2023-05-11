package com.aberon.flexbook.ui.reader

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.aberon.flexbook.databinding.ActivityReaderBinding
import com.aberon.flexbook.model.BookInfo
import com.aberon.flexbook.model.BookParametr
import com.aberon.flexbook.model.BookParametrKey
import com.aberon.flexbook.store.SQLStore
import com.aberon.flexbook.ui.pages.PagesFragmentAdapter
import com.aberon.flexbook.ui.pages.Paginator

class ReaderActivity : AppCompatActivity() {
    companion object {
        const val READER_BOOK_PARAM = "book"
    }

    private lateinit var binding: ActivityReaderBinding
    private lateinit var readerContent: ViewPager2
    private lateinit var blanck: TextView
    private lateinit var store: SQLStore
    private lateinit var bookInfo: BookInfo
    private lateinit var paginator: Paginator

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReaderBinding.inflate(layoutInflater)
        readerContent = binding.readerContent
        blanck = binding.pageBlanck
        store = SQLStore.getInstance(this)
        setContentView(binding.root)

        intent.getStringExtra(READER_BOOK_PARAM)?.let { id ->
            blanck.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            bookInfo = store.getBookById(id)
            paginator = Paginator(
                bookInfo,
                blanck.measuredHeight,
                resources.displayMetrics.widthPixels,
                blanck.lineSpacingMultiplier,
                blanck.lineSpacingExtra,
                blanck.paint
            )
            readerContent.adapter = PagesFragmentAdapter(
                paginator,
                supportFragmentManager,
                lifecycle
            )
            val parametrs = bookInfo.parametrs.associateBy { p -> p.parametrId }
            val savedSection = parametrs[BookParametrKey.SAVED_SECTION]
            val savedOffset = parametrs[BookParametrKey.SAVED_OFFSET]
            if (savedSection != null && savedOffset != null) {
                readerContent.currentItem =
                    paginator.getPageIndexFromStart(savedSection.parametrValue to savedOffset.parametrValue)
            }
        }
    }

    override fun onPause() {
        val startPage = paginator.getStartPage(readerContent.currentItem)
        updateParametr(BookParametrKey.SAVED_SECTION, startPage.first)
        updateParametr(BookParametrKey.SAVED_OFFSET, startPage.second)
        super.onPause()
    }

    private fun updateParametr(
        key: BookParametrKey,
        parametrValue: Int
    ) {
        val bookParametr = bookInfo.parametrs.lastOrNull { p -> p.parametrId == key }
        if (bookParametr != null) {
            bookParametr.parametrValue = parametrValue
            store.updateBookParametr(bookParametr)
        } else {
            store.addBookParametr(
                BookParametr(
                    key,
                    bookInfo.book.bookId,
                    parametrValue
                )
            )
        }
    }
}