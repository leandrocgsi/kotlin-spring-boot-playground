package br.com.erudio.request.converters

object NumberConverter {
    fun covertToDouble(strNumber: String?): Double {
        if (strNumber == null) return 0.0
        val number = strNumber.replace(",".toRegex(), ".")
        return if (isNumeric(number)) number.toDouble() else 0.0
    }

    fun isNumeric(strNumber: String?): Boolean {
        if (strNumber == null) return false
        val number = strNumber.replace(",".toRegex(), ".")
        return number.matches("""[-+]?[0-9]*\.?[0-9]+""".toRegex())
    }
}