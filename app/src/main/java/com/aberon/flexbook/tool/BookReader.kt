package com.aberon.flexbook.tool

import android.widget.TextView
import com.aberon.flexbook.model.BookInfo


class BookReader(
    private val book: BookInfo,
    private val textView: TextView
) {
    companion object {
        private const val COUNT_PAGES = "count_pages"
        private const val CURRENT_SECTION = "current_section"
        private const val CURRENT_PAGE = "current_page"
        private const val SECTION_NUM = "section_num"
    }

    var currentPage = book.book.parameters?.get(CURRENT_PAGE) ?: 0
    var countPages = book.book.parameters?.get(COUNT_PAGES) ?: 0
    var currentSection = book.book.parameters?.get(CURRENT_SECTION) ?: 0
    var sectionNum = book.book.parameters?.get(SECTION_NUM) ?: 0

    private val pages: Map<Int, String> = mutableMapOf()

    init {
        val linesCount = textView.height / textView.lineHeight

        textView.text = page

        textView.width

        /*textView.viewTreeObserver.addOnGlobalLayoutListener {
            val maxLines = textView.height / textView.lineHeight
            book.sections?.forEach { section ->
                val lines = mutableListOf<String>()
                lines.add(getTitle(section.title)) // TODO title style
                section?.elements?.forEach { paragraph ->
                    paragraph.text?.let {
                        it
                        TODO("а сколько влезет")
                    }
                }
            }
        }*/

        /*val linesCount = book.sections?.sumOf { s ->
            (s.title?.let { 2 } ?: 0) +
                    (s.elements?.sumOf { el ->
                        el.text?.lines()?.size ?: 0
                    } ?: 0)
        } ?: 0*/
    }

    fun nextPage() {
        TODO("not implemented")
    }

    fun prevPage() {
        TODO("not implemented")
    }

    private fun getTitle(string: String?): String {
        return string?.let { "$string\n\n" } ?: ""
    }

    private val page: String
        get() {

            val sections = book.book.sections?.map { section ->
                section.elements?.map { paragraph ->
                    "${paragraph.text}\n"
                }?.requireNoNulls()?.joinToString("\n")
            }!!.requireNoNulls().joinToString("\n\n")
            return sections
        }
}