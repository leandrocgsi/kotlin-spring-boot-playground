package br.com.erudio.controller

import br.com.erudio.exception.UnsuportedMathOperationException
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class MathController2 {
    @RequestMapping(value = ["/sum/{numberOne}/{numberTwo}"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun sum(@PathVariable("numberOne") numberOne: String?, @PathVariable("numberTwo") numberTwo: String?): Double {
        if (!isNumeric(numberOne) || !isNumeric(numberTwo)) {
            throw UnsuportedMathOperationException("Please set a numeric value!")
        }
        return covertToDouble(numberOne) + covertToDouble(numberTwo)
    }

    @RequestMapping(value = ["/subtraction/{numberOne}/{numberTwo}"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun subtraction(@PathVariable("numberOne") numberOne: String?, @PathVariable("numberTwo") numberTwo: String?): Double {
        if (!isNumeric(numberOne) || !isNumeric(numberTwo)) {
            throw UnsuportedMathOperationException("Please set a numeric value!")
        }
        return covertToDouble(numberOne) - covertToDouble(numberTwo)
    }

    @RequestMapping(value = ["/multiplication/{numberOne}/{numberTwo}"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun multiplication(@PathVariable("numberOne") numberOne: String?, @PathVariable("numberTwo") numberTwo: String?): Double {
        if (!isNumeric(numberOne) || !isNumeric(numberTwo)) {
            throw UnsuportedMathOperationException("Please set a numeric value!")
        }
        return covertToDouble(numberOne) * covertToDouble(numberTwo)
    }

    @RequestMapping(value = ["/division/{numberOne}/{numberTwo}"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun division(@PathVariable("numberOne") numberOne: String?, @PathVariable("numberTwo") numberTwo: String?): Double {
        if (!isNumeric(numberOne) || !isNumeric(numberTwo)) {
            throw UnsuportedMathOperationException("Please set a numeric value!")
        }
        return covertToDouble(numberOne) / covertToDouble(numberTwo)
    }

    @RequestMapping(value = ["/mean/{numberOne}/{numberTwo}"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun mean(@PathVariable("numberOne") numberOne: String?, @PathVariable("numberTwo") numberTwo: String?): Double {
        if (!isNumeric(numberOne) || !isNumeric(numberTwo)) {
            throw UnsuportedMathOperationException("Please set a numeric value!")
        }
        return (covertToDouble(numberOne) + covertToDouble(numberTwo)) / 2
    }

    @RequestMapping(value = ["/squareRoot/{number}"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun squareRoot(@PathVariable("number") number: String?): Double {
        if (!isNumeric(number)) {
            throw UnsuportedMathOperationException("Please set a numeric value!")
        }
        return Math.sqrt(covertToDouble(number))
    }

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