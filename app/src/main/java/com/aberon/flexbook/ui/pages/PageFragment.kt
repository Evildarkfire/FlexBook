package com.aberon.flexbook.ui.pages

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.core.view.*
import androidx.fragment.app.Fragment
import com.aberon.flexbook.databinding.BottomSheetBinding
import com.aberon.flexbook.databinding.PageItemLayoutBinding
import com.aberon.flexbook.model.Language
import com.aberon.flexbook.model.Preference
import com.aberon.flexbook.model.PreferenceKey
import com.aberon.flexbook.store.SQLStore
import com.aberon.flexbook.tool.Translation
import com.aberon.flexbook.ui.settings.SettingsFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior


class PageFragment(bsBinding: BottomSheetBinding) : Fragment() {
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

    private val bottomSheet = bsBinding.bottomSheet
    private val bottomSheetText = bsBinding.bottomSheetText
    private val behavior = BottomSheetBehavior.from(bottomSheet)
    private lateinit var store: SQLStore
    private lateinit var preference: Map<PreferenceKey, Preference>
    private lateinit var binding: PageItemLayoutBinding
    private var bookLanguage: String? = null
    private lateinit var targetLanguage: String
    private lateinit var pageContent: TextView
    private lateinit var pageNumber: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        store = SQLStore.getInstance(requireContext())
        preference = store.preference
        targetLanguage = preference[PreferenceKey.TargetLanguage]?.let { tl ->
            Language[tl.preferenceValue]?.short
        } ?: SettingsFragment.defaulLanguage.short
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PageItemLayoutBinding.inflate(inflater, container, false)
        val view = binding.root
        val prefPadding =
            SettingsFragment.getMarginFromValue(preference[PreferenceKey.ReaderMargin]?.preferenceValue)
        val textSize =
            SettingsFragment.getTextSizeFromValue(preference[PreferenceKey.ReaderTextSize]?.preferenceValue)
        binding.pageContentContainer.setPadding(prefPadding)
        pageContent = binding.pageContent.apply {
            setTextSize(textSize)
            customSelectionActionModeCallback = object : ActionMode.Callback {
                override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                    menu?.children
                        ?.map { it.itemId }
                        ?.filter { !MenuItems.ids.contains(it) }
                        ?.forEach { menu.removeItem(it) }
                    menu?.add(
                        0,
                        MenuItems.OPEN_REVERSO.id,
                        0,
                        "Открыть в Reverso context"
                    )
                    return true
                }

                override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                    val translate = Translation()
                    translate.setOnTranslationCompleteListener(object :
                        Translation.OnTranslationCompleteListener {
                        override fun onStartTranslation() {}
                        override fun onCompleted(text: String?) {
                            text?.let { r ->
                                bottomSheetText.text = r
                                behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            }
                        }

                        override fun onError(e: Exception?) {}
                    })
                    translate.execute(
                        "${text.subSequence(selectionStart, selectionEnd)}",
                        bookLanguage,
                        targetLanguage
                    )
                    return true
                }

                override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                    val text = text.subSequence(selectionStart, selectionEnd)
                    when (item?.itemId) {
                        MenuItems.COPY.id -> {
                            val clipboard: ClipboardManager? = view
                                .context
                                .getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
                            val clip = ClipData.newPlainText(
                                "copy",
                                text
                            )
                            clipboard?.setPrimaryClip(clip)
                        }
                        MenuItems.OPEN_REVERSO.id -> {
                            val bl = Language.fromShort(bookLanguage!!)!!.full
                            val tl = Language.fromShort(targetLanguage)!!.full
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://context.reverso.net/перевод/$bl-$tl/$text")
                            )
                            startActivity(intent)
                        }
                    }
                    return true
                }

                override fun onDestroyActionMode(mode: ActionMode?) {
                    behavior.state = BottomSheetBehavior.STATE_HIDDEN
                }

            }
        }
        pageNumber = binding.pageNumber.apply {
            setTextSize(textSize)
        }
        binding.pageNumberContainer.setPadding(prefPadding)
        behavior.state = BottomSheetBehavior.STATE_HIDDEN

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.let {
            pageContent.text = (it.getString("text"))
            pageNumber.text = (it.getString("page"))
            bookLanguage = (it.getString("bookLang"))
        }
    }
}