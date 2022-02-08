package br.com.erudio.controller

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

//@RestController
class MathController1 {
    @RequestMapping(value = ["/sum/{numberOne}/{numberTwo}"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun sum(@PathVariable("numberOne") numberOne: String?, @PathVariable("numberTwo") numberTwo: String? ): Double {
        if (!isNumeric(numberOne) || !isNumeric(numberTwo)) {
            throw Exception()
        }
        return covertToDouble(numberOne) + covertToDouble(numberTwo)
    }

    companion object {
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
}