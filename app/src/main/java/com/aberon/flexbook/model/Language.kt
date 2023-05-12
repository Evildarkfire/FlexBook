package com.aberon.flexbook.model

enum class Language(
    val id: Int,
    val full: String,
    val short: String
) {
    RUSSIAN(0, "русский", "ru"),
    ENGLISH(1, "английский", "en");

    companion object {
        operator fun get(index: Int): Language? {
            return if (index >= 0 && index < values().size) values()[index] else null
        }

        fun all(): List<String> {
            return values().map { it.full }
        }

        fun fromShort(short: String): Language? {
            return values().firstOrNull { it.short == short }
        }
    }
}