package com.aberon.flexbook.tool

import android.text.Layout
import android.view.ViewTreeObserver
import android.widget.TextView
import com.aberon.flexbook.model.Book


class BookReader(
    private val book: Book,
    private val textView: TextView
) {
    var currentPage = book.parameters?.get(CURRENT_PAGE) ?: 0
    var countPages = book.parameters?.get(COUNT_PAGES) ?: 0
    var currentSection = book.parameters?.get(CURRENT_SECTION) ?: 0
    var sectionNum = book.parameters?.get(SECTION_NUM) ?: 0

    init {
        textView.text = page
    }

    fun nextPage() {
        TODO("not implemented")
    }

    fun prevPage() {
        TODO("not implemented")
    }

    private val page: String
        get() {
            textView.viewTreeObserver.addOnGlobalLayoutListener {
                val observer: ViewTreeObserver = textView.viewTreeObserver

                val height: Int = textView.height
                val scrollY: Int = textView.scrollY
                val layout: Layout = textView.layout
                val firstVisibleLineNumber: Int = layout.getLineForVertical(scrollY) // start id
                val lastVisibleLineNumber: Int = layout.getLineForVertical(height + scrollY) // end id
            } // TODO release pagination

            val sections = book.sections?.map { section ->
                section.elements?.map { paragraph ->
                    "${paragraph.text}\n"
                }?.requireNoNulls()?.joinToString("\n")
            }!!.requireNoNulls().joinToString("\n\n")
            return sections
        }

    companion object {
        private const val COUNT_PAGES = "count_pages"
        private const val CURRENT_SECTION = "current_section"
        private const val CURRENT_PAGE = "current_page"
        private const val SECTION_NUM = "section_num"
    }
}