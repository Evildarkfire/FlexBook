package com.aberon.flexbook.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.aberon.flexbook.databinding.SettingsFragmentBinding
import com.aberon.flexbook.model.Preference
import com.aberon.flexbook.model.PreferenceKey
import com.aberon.flexbook.store.SQLStore


class SettingsFragment : Fragment() {

    companion object {
        const val readerMarginMin = 0
        const val readerMarginMax = 50
        const val readerMarginStep = 5
        const val textSizeMin = 12
        const val textSizeMax = 36
        const val textSizeStep = 2
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

    private lateinit var preferences: Map<PreferenceKey, Preference>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SettingsFragmentBinding.inflate(inflater, container, false)
        store = SQLStore.getInstance(activity!!.applicationContext)
        preferences = store.preference
        val view = binding.root

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
            progress = preferences[PreferenceKey.ReaderMarginSeekbar]?.preferenceValue ?: 1
            setOnSeekBarChangeListener(seekBarChangeListener)
        }
        readerMarginSeekbarText = binding.readerMarginSeekbarText.apply {
            text = "${getPreferenceValue(PreferenceKey.ReaderMarginSeekbar)}"
        }

        readerStartSeekbar = binding.readerStartSeekbar.apply {
            max = (readerMarginMax - readerMarginMin) / readerMarginStep
            progress = preferences[PreferenceKey.ReaderStartSeekbar]?.preferenceValue ?: 1
            setOnSeekBarChangeListener(seekBarChangeListener)
        }
        readerStartSeekbarText = binding.readerStartSeekbarText.apply {
            text = "${getPreferenceValue(PreferenceKey.ReaderStartSeekbar)}"
        }

        readerTextSizeSeekbar = binding.readerTextSizeSeekbar.apply {
            max = (textSizeMax - textSizeMin) / textSizeStep
            progress = preferences[PreferenceKey.ReaderTextSizeSeekbar]?.preferenceValue ?: 3
            setOnSeekBarChangeListener(seekBarChangeListener)
        }
        readerTextSizeSeekbarText = binding.readerTextSizeSeekbarText.apply {
            text = "${getPreferenceValue(PreferenceKey.ReaderTextSizeSeekbar)}"
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
            PreferenceKey.ReaderStartSeekbar ->
                preferences[PreferenceKey.ReaderStartSeekbar]
                    ?.let { preference -> readerMarginMin + (preference.preferenceValue * readerMarginStep) }
                    ?: readerMarginMin
            PreferenceKey.ReaderMarginSeekbar ->
                preferences[PreferenceKey.ReaderStartSeekbar]
                    ?.let { preference -> readerMarginMin + (preference.preferenceValue * readerMarginStep) }
                    ?: readerMarginMin
            PreferenceKey.ReaderTextSizeSeekbar ->
                preferences[PreferenceKey.ReaderStartSeekbar]
                    ?.let { preference -> textSizeMin + (preference.preferenceValue * textSizeStep) }
                    ?: textSizeMin
            PreferenceKey.NightMod ->
                preferences[PreferenceKey.NightMod]
                    ?.let { preference -> if (preference.preferenceValue == 1) 1 else 0 }
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
                    updatePreference(PreferenceKey.ReaderMarginSeekbar, progress)
                    readerMarginSeekbarText.text =
                        "${readerMarginMin + (progress * readerMarginStep)}"
                }
                readerStartSeekbar -> {
                    updatePreference(PreferenceKey.ReaderStartSeekbar, progress)
                    readerStartSeekbarText.text =
                        "${readerMarginMin + (progress * readerMarginStep)}"
                }
                readerTextSizeSeekbar -> {
                    updatePreference(PreferenceKey.ReaderTextSizeSeekbar, progress)
                    readerTextSizeSeekbarText.text =
                        "${textSizeMin + (progress * textSizeStep)}"
                }
            }
        }
    })
}