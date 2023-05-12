package com.aberon.flexbook.ui.pages

import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import com.aberon.flexbook.model.BookInfo


class Paginator(
    private val bookInfo: BookInfo,
    private val height: Int,
    private val width: Int,
    private val lineSpacingMultiplier: Float,
    private val lineSpacingExtra: Float,
    private val paint: TextPaint,
    private val startLine: String
) {
    private val pages: MutableList<PageInfo>
    val size: Int
        get() = pages.size

    init {
        pages = ArrayList()
        sections()
    }

    fun getStartPage(pageNumber: Int): Pair<Int, Int> {
        val page = pages[pageNumber]
        return page.sectionIndex to page.startOffset
    }

    fun getPageIndexFromStart(sectionToStartOffset: Pair<Int, Int>): Int {
        val page = pages.firstOrNull { p ->
            p.sectionIndex == sectionToStartOffset.first
                    && p.startOffset == sectionToStartOffset.second
        }
        return pages.indexOf(page)
    }

    operator fun get(index: Int): CharSequence? {
        return if (index >= 0 && index < pages.size) pages[index].text else null
    }

    private fun sections() {
        bookInfo.book.sections?.forEach { section ->
            section.elements?.map { paragraph ->
                "$startLine${paragraph.text}"
            }?.requireNoNulls()?.joinToString("\n\n")?.let { sequens ->
                if (sequens.isNotBlank()) {
                    if (section.title != null && section.title.isNotBlank()) {
                        layout(section.id, "${section.title}\n\n${sequens}")
                    } else {
                        layout(section.id, sequens)
                    }
                }
            }
        }
    }

    private fun layout(section: Int, text: CharSequence) {
        val layout = StaticLayout.Builder
            .obtain(text, 0, text.length, paint, width)
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .setLineSpacing(lineSpacingExtra, lineSpacingMultiplier)
            .setIncludePad(false)
            .build()
        val lines = layout.lineCount
        var startOffset = 0
        var pageHeight = height
        for (i in 0 until lines) {
            if (pageHeight < layout.getLineBottom(i)) {
                addPage(
                    PageInfo(
                        section,
                        startOffset,
                        text.subSequence(startOffset, layout.getLineStart(i))
                    )
                )
                startOffset = layout.getLineStart(i)
                pageHeight = layout.getLineTop(i) + height
            }
            if (i == lines - 1) {
                addPage(
                    PageInfo(
                        section,
                        startOffset,
                        text.subSequence(startOffset, layout.getLineEnd(i))
                    )
                )
                return
            }
        }
    }

    private fun addPage(page: PageInfo) {
        pages.add(page)
    }

    data class PageInfo(
        val sectionIndex: Int,
        val startOffset: Int,
        val text: CharSequence
    )
}