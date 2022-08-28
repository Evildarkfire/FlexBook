package com.aberon.flexbook.tool

import android.widget.TextView
import com.aberon.flexbook.model.Book

class BookReader(
    private val book: Book,
    private val textView: TextView
) {
    var currentPage: Int
    var countPage: Int

    init {
        //TODO load from book parameter
        currentPage = 0
        countPage = 0
    }

    fun nextPage() {
        TODO("not implemented")
    }

    fun prevPage() {
        TODO("not implemented")
    }

    val page: String
        get() {
            val sections = book.sections?.last().let { section ->
                section?.elements?.map { paragraph ->
                    paragraph.text
                }
            }!!.requireNoNulls()
            return sections.lastOrNull() ?: ""
        }

    companion object {
        private const val COUNT_PAGES = "count_pages"
        private const val CURRENT_PAGE = "current_page"
        private const val SECTION_NUM = "section_num"
        private const val PARAGRAPH_NUM = "section_num"
    }
}