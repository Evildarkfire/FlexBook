package com.aberon.flexbook.ui.pages

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.aberon.flexbook.databinding.BottomSheetBinding
import com.aberon.flexbook.model.BookInfo


class PagesFragmentAdapter(
    private val bsBinding: BottomSheetBinding,
    private val bookInfo: BookInfo,
    private val paginator: Paginator,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = paginator.size

    override fun createFragment(position: Int): Fragment = PageFragment(bsBinding).apply {
        arguments = bundleOf(
            "text" to paginator[position],
            "page" to "${position + 1}",
            "bookLang" to bookInfo.book.language
        )
    }
}