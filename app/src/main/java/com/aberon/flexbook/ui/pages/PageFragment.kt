package com.aberon.flexbook.ui.pages

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.aberon.flexbook.databinding.PageItemLayoutBinding
import com.aberon.flexbook.tool.Translation

class PageFragment : Fragment() {
    companion object {
        enum class MenuItems(val id: Int) {
            COPY(android.R.id.copy),
            OPEN_REVERSO(111);

            companion object {
                val ids
                    get() = values().map { it.id }

                fun fromInt(value: Int) = values().first { it.id == value }
            }
        }
    }

    private lateinit var binding: PageItemLayoutBinding
    private lateinit var pageContent: TextView
    private lateinit var pageNumber: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PageItemLayoutBinding.inflate(inflater, container, false)
        val view = binding.root

        pageContent = binding.pageContent.apply {
            customSelectionActionModeCallback = object : ActionMode.Callback {
                override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                    menu?.add(
                        0,
                        MenuItems.OPEN_REVERSO.id,
                        0,
                        "Открыть в Reverso context"
                    )
                    val translate = Translation()
                    translate.setOnTranslationCompleteListener(object :
                        Translation.OnTranslationCompleteListener {
                        override fun onStartTranslation() {
                            // here you can perform initial work before translated the text like displaying progress bar
                        }

                        override fun onCompleted(text: String?) {
                            text?.let { r ->
                                menu?.add(r)
                            }
                        }

                        override fun onError(e: Exception?) {}
                    })
                    translate.execute(
                        "${text.subSequence(selectionStart, selectionEnd)}",
                        "ru",
                        "en"
                    )
                    return true
                }

                override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                    menu?.children
                        ?.map { it.itemId }
                        ?.filter { !MenuItems.ids.contains(it) }
                        ?.forEach { menu.removeItem(it) }
                    return true
                }

                override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                    when (item?.itemId) {
                        MenuItems.COPY.id -> {
//                            val clipboard: ClipboardManager? = itemView
//                                .context
//                                .getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
//                            val clip = ClipData.newPlainText(
//                                "copy",
//                                text.subSequence(selectionStart, selectionEnd)
//                            )
//                            clipboard?.setPrimaryClip(clip)//TODO fix copy
                        }

                    }
                    return true
                }

                override fun onDestroyActionMode(mode: ActionMode?) {}

            }
        }
        pageNumber = binding.pageNumber

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.let {
            pageContent.text = (it.getString("text"))
            pageNumber.text = (it.getString("page"))
        }
    }
}