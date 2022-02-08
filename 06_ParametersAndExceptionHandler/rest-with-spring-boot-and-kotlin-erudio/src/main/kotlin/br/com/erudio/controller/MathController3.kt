package br.com.erudio.controller

import br.com.erudio.exception.UnsuportedMathOperationException
import br.com.erudio.math.SimpleMath
import br.com.erudio.request.converters.NumberConverter
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

//@RestController
class MathController3 {

    private val math: SimpleMath = SimpleMath()

    @RequestMapping(value = ["/sum/{numberOne}/{numberTwo}"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun sum(@PathVariable("numberOne") numberOne: String?, @PathVariable("numberTwo") numberTwo: String?): Double {
        if (!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo)) {
            throw UnsuportedMathOperationException("Please set a numeric value!")
        }
        return math.sum(NumberConverter.covertToDouble(numberOne), NumberConverter.covertToDouble(numberTwo))
    }

    @RequestMapping(value = ["/subtraction/{numberOne}/{numberTwo}"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun subtraction(@PathVariable("numberOne") numberOne: String?, @PathVariable("numberTwo") numberTwo: String?): Double {
        if (!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo)) {
            throw UnsuportedMathOperationException("Please set a numeric value!")
        }
        return math.subtraction(NumberConverter.covertToDouble(numberOne), NumberConverter.covertToDouble(numberTwo))
    }

    @RequestMapping(value = ["/multiplication/{numberOne}/{numberTwo}"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun multiplication(@PathVariable("numberOne") numberOne: String?, @PathVariable("numberTwo") numberTwo: String?): Double {
        if (!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo)) {
            throw UnsuportedMathOperationException("Please set a numeric value!")
        }
        return math.multiplication(NumberConverter.covertToDouble(numberOne), NumberConverter.covertToDouble(numberTwo))
    }

    @RequestMapping(value = ["/division/{numberOne}/{numberTwo}"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun division(@PathVariable("numberOne") numberOne: String?, @PathVariable("numberTwo") numberTwo: String?): Double {
        if (!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo)) {
            throw UnsuportedMathOperationException("Please set a numeric value!")
        }
        return math.division(NumberConverter.covertToDouble(numberOne), NumberConverter.covertToDouble(numberTwo))
    }

    @RequestMapping(value = ["/mean/{numberOne}/{numberTwo}"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun mean(@PathVariable("numberOne") numberOne: String?, @PathVariable("numberTwo") numberTwo: String?): Double {
        if (!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo)) {
            throw UnsuportedMathOperationException("Please set a numeric value!")
        }
        return math.mean(NumberConverter.covertToDouble(numberOne), NumberConverter.covertToDouble(numberTwo))
    }

    @RequestMapping(value = ["/squareRoot/{number}"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun squareRoot(@PathVariable("number") number: String?): Double {
        if (!NumberConverter.isNumeric(number)) {
            throw UnsuportedMathOperationException("Please set a numeric value!")
        }
        return math.squareRoot(NumberConverter.covertToDouble(number))
    }
}