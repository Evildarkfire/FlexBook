package com.aberon.flexbook.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.aberon.flexbook.R
import com.aberon.flexbook.databinding.SettingsFragmentBinding
import com.aberon.flexbook.model.Language
import com.aberon.flexbook.model.Preference
import com.aberon.flexbook.model.PreferenceKey
import com.aberon.flexbook.store.SQLStore


class SettingsFragment : Fragment() {

    companion object {
        const val readerMarginDefault = 4
        const val readerMarginMin = 0
        const val readerMarginMax = 50
        const val readerMarginStep = 2
        const val startDefault = 0
        const val startMin = 0
        const val startMax = 5
        const val startStep = 1
        const val textSizeDefault = 18
        const val textSizeMin = 12
        const val textSizeMax = 36
        const val textSizeStep = 2
        val defaulLanguage = Language[0]!!
        private const val DP = 10

        fun getTextSizeFromValue(int: Int?): Float {
            if (int != null) {
                return (textSizeMin + (int * textSizeStep)).toFloat()
            }
            return textSizeDefault.toFloat()
        }

        fun getMarginFromValue(int: Int?): Int {
            if (int != null) {
                return (readerMarginMin + (int * readerMarginStep)) * DP
            }
            return readerMarginDefault * DP
        }
    }

    private lateinit var store: SQLStore
    private lateinit var binding: SettingsFragmentBinding
    private lateinit var preferencesNight: SwitchCompat
    private lateinit var readerMarginSeekbar: SeekBar
    private lateinit var readerMarginSeekbarText: TextView
    private lateinit var readerStartSeekbar: SeekBar
    private lateinit var readerStartSeekbarText: TextView
    private lateinit var readerTextSizeSeekbar: SeekBar
    private lateinit var readerTextSizeSeekbarText: TextView
    private lateinit var preferencesLang: Spinner

    private lateinit var preferences: Map<PreferenceKey, Preference>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SettingsFragmentBinding.inflate(inflater, container, false)
        store = SQLStore.getInstance(activity!!.applicationContext)
        preferences = store.preference
        val view = binding.root

        val languageAdapter = ArrayAdapter(
            view.context,
            R.layout.spinner_item,
            Language.all()
        )
        preferencesLang = binding.preferencesLanguage.apply {
            adapter = languageAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>?,
                    selectedItemView: View?,
                    position: Int,
                    id: Long
                ) {
                    updatePreference(PreferenceKey.TargetLanguage, id.toInt())
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {}
            }
            setSelection(getPreferenceValue(PreferenceKey.TargetLanguage))
        }

        preferencesNight = binding.preferencesNight.apply {
            isChecked = getPreferenceValue(PreferenceKey.NightMod) == 1
            setOnCheckedChangeListener { _, state ->
                val st = if (state) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    1
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    0
                }
                updatePreference(PreferenceKey.NightMod, st)
            }
        }

        readerMarginSeekbar = binding.readerMarginSeekbar.apply {
            max = (readerMarginMax - readerMarginMin) / readerMarginStep
            progress = preferences[PreferenceKey.ReaderMargin]?.preferenceValue ?: 1
            setOnSeekBarChangeListener(seekBarChangeListener)
        }
        readerMarginSeekbarText = binding.readerMarginSeekbarText.apply {
            text = "${getPreferenceValue(PreferenceKey.ReaderMargin)}"
        }

        readerStartSeekbar = binding.readerStartSeekbar.apply {
            max = (startMax - startMin) / startStep
            progress = preferences[PreferenceKey.ReaderStart]?.preferenceValue ?: 1
            setOnSeekBarChangeListener(seekBarChangeListener)
        }
        readerStartSeekbarText = binding.readerStartSeekbarText.apply {
            text = "${getPreferenceValue(PreferenceKey.ReaderStart)}"
        }

        readerTextSizeSeekbar = binding.readerTextSizeSeekbar.apply {
            max = (textSizeMax - textSizeMin) / textSizeStep
            progress = preferences[PreferenceKey.ReaderTextSize]?.preferenceValue ?: 3
            setOnSeekBarChangeListener(seekBarChangeListener)
        }
        readerTextSizeSeekbarText = binding.readerTextSizeSeekbarText.apply {
            text = "${getPreferenceValue(PreferenceKey.ReaderTextSize)}"
        }

        return view
    }

    override fun onDestroy() {
        store.updatePreferences(preferences)
        super.onDestroy()
    }

    private fun updatePreference(preferenceKey: PreferenceKey, value: Int) {
        val pref = preferences[preferenceKey]
        if (pref == null) {
            preferences = preferences.plus(preferenceKey to Preference(preferenceKey, value))
        } else {
            pref.preferenceValue = value
        }
    }

    private fun getPreferenceValue(preferenceKey: PreferenceKey): Int {
        return when (preferenceKey) {
            PreferenceKey.ReaderMargin ->
                preferences[PreferenceKey.ReaderMargin]
                    ?.let { preference -> readerMarginMin + (preference.preferenceValue * readerMarginStep) }
                    ?: readerMarginDefault
            PreferenceKey.ReaderStart ->
                preferences[PreferenceKey.ReaderStart]
                    ?.let { preference -> startMin + (preference.preferenceValue * startStep) }
                    ?: startDefault
            PreferenceKey.ReaderTextSize ->
                preferences[PreferenceKey.ReaderTextSize]
                    ?.let { preference -> textSizeMin + (preference.preferenceValue * textSizeStep) }
                    ?: textSizeDefault
            PreferenceKey.NightMod ->
                preferences[PreferenceKey.NightMod]
                    ?.let { preference -> if (preference.preferenceValue == 1) 1 else 0 }
                    ?: 0
            PreferenceKey.TargetLanguage ->
                preferences[PreferenceKey.TargetLanguage]
                    ?.let { preference -> Language[preference.preferenceValue]?.id }
                    ?: 0
        }
    }

    private val seekBarChangeListener = (object : OnSeekBarChangeListener {
        override fun onStopTrackingTouch(seekBar: SeekBar) {}
        override fun onStartTrackingTouch(seekBar: SeekBar) {}
        override fun onProgressChanged(
            seekBar: SeekBar,
            progress: Int,
            fromUser: Boolean
        ) {
            when (seekBar) {
                readerMarginSeekbar -> {
                    updatePreference(PreferenceKey.ReaderMargin, progress)
                    readerMarginSeekbarText.text =
                        "${readerMarginMin + (progress * readerMarginStep)}"
                }
                readerStartSeekbar -> {
                    updatePreference(PreferenceKey.ReaderStart, progress)
                    readerStartSeekbarText.text =
                        "${startMin + (progress * startStep)}"
                }
                readerTextSizeSeekbar -> {
                    updatePreference(PreferenceKey.ReaderTextSize, progress)
                    readerTextSizeSeekbarText.text =
                        "${textSizeMin + (progress * textSizeStep)}"
                }
            }
        }
    })
}