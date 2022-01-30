package br.com.erudio.converter

import com.github.dozermapper.core.DozerBeanMapperBuilder
import java.util.*

class DozerConverter {
    private val mapper = DozerBeanMapperBuilder.buildDefault()

    fun <O, D> parseObject(origin: O, destination: Class<D>?): D {
        return mapper.map(origin, destination)
    }

    fun <O, D> parseListObjects(origin: List<O>, destination: Class<D>?): List<D> {
        val destinationObjects: MutableList<D> = ArrayList()
        for (o in origin) {
            destinationObjects.add(mapper.map(o, destination))
        }
        return destinationObjects
    }
}