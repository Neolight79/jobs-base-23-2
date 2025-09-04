package ru.practicum.android.diploma.util.format

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

enum class CurrencyStyle { CODE, SYMBOL, SYMBOL_OR_CODE }

object SalaryFormatter {
    private val normalizedCode = mapOf(
        "RUR" to "RUB"
    )

    fun toDisplay(
        minValue: Long?,
        maxValue: Long?,
        currencyCodeRaw: String?,
        locale: Locale = Locale.getDefault(),
        emptyText: String = "Зарплата не указана",
        keepNbsp: Boolean = true,
        currencyStyle: CurrencyStyle = CurrencyStyle.CODE
    ): String {
        if (minValue == null && maxValue == null) return emptyText

        val codeUpper = currencyCodeRaw?.uppercase(Locale.ROOT).orEmpty()
        val code = (normalizedCode[codeUpper] ?: codeUpper).ifEmpty { "RUB" }

        val nf = NumberFormat.getNumberInstance(locale).apply {
            maximumFractionDigits = 0
            isGroupingUsed = true
        }
        fun fmt(v: Long?) = v?.let { nf.format(it) }
            ?.let { if (keepNbsp) it.replace(' ', '\u00A0') else it }

        val min = fmt(minValue)
        val max = fmt(maxValue)

        val label =
            if (currencyStyle == CurrencyStyle.CODE) code
            else runCatching { Currency.getInstance(code).getSymbol(locale) }.getOrNull() ?: code

        return when {
            min != null && max != null -> "от $min до $max $label"
            min != null -> "от $min $label"
            else -> "до $max $label"
        }
    }
}
