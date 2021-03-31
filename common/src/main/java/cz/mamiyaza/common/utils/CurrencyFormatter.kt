package cz.mamiyaza.common.utils

import java.text.DecimalFormat

/**
 * Converter for currency.
 */
object CurrencyFormatter {
    fun format(number: Long): String {
        val decimalFormat = DecimalFormat("###,###,##0")
        return decimalFormat.format(number.toString().toDouble())
    }

}