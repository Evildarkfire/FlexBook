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
    private val paint: TextPaint
) {
    private val pages: MutableList<CharSequence>
    val size: Int
        get() = pages.size

    init {
        pages = ArrayList()
        sections()
    }

    operator fun get(index: Int): CharSequence? {
        return if (index >= 0 && index < pages.size) pages[index] else null
    }

    private fun sections() {
        bookInfo.book.sections?.forEach { section ->
            section.elements?.map { paragraph ->
                "${paragraph.text}"
            }?.requireNoNulls()?.joinToString("\n\n")?.let { sequens ->
                if (sequens.isNotBlank()) {
                    if (section.title != null && section.title.isNotBlank()) {
                        layout("${section.title}\n\n${sequens}")
                    } else {
                        layout(sequens)
                    }
                }
            }
        }
    }

    private fun layout(text: CharSequence) {
        val layout = StaticLayout.Builder.obtain(text, 0, text.length, paint, width)
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .setLineSpacing(lineSpacingExtra, lineSpacingMultiplier)
            .setIncludePad(false)
            .build()
        val lines = layout.lineCount
        var startOffset = 0
        var pageHeight = height
        for (i in 0 until lines) {
            if (pageHeight < layout.getLineBottom(i)) {
                addPage(text.subSequence(startOffset, layout.getLineStart(i)))
                startOffset = layout.getLineStart(i)
                pageHeight = layout.getLineTop(i) + height
            }
            if (i == lines - 1) {
                addPage(text.subSequence(startOffset, layout.getLineEnd(i)))
                return
            }
        }
    }

    private fun addPage(text: CharSequence) {
        pages.add(text)
    }
}