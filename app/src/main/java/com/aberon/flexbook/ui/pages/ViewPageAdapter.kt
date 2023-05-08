package com.aberon.flexbook.ui.pages

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aberon.flexbook.databinding.PageItemLayoutBinding
import com.aberon.flexbook.store.SQLStore


class ViewPageAdapter(context: Context, blanck: TextView, bookId: String) :
    RecyclerView.Adapter<ViewPageAdapter.PagerViewHandler>() {
    private val store: SQLStore = SQLStore.getInstance(context)
    private val bookInfo = store.getBookById(bookId)
    private val paginator = Paginator(
        bookInfo,
        blanck.measuredHeight,
        context.resources.displayMetrics.widthPixels,
        blanck.lineSpacingMultiplier,
        blanck.lineSpacingExtra,
        blanck.paint
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHandler {
        val binding = PageItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PagerViewHandler(binding.root)
    }

    override fun getItemCount(): Int = paginator.size

    override fun onBindViewHolder(holder: PagerViewHandler, position: Int) = holder.itemView.let {
        holder.pageContent.text = paginator[position]
        holder.pageNumber.text = (position + 1).toString()
    }

    class PagerViewHandler(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding: PageItemLayoutBinding = PageItemLayoutBinding.bind(itemView)
        val pageContent = binding.pageContent
        val pageNumber = binding.pageNumber
    }
}