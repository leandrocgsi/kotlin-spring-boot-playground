package br.com.erudio.mapper

import com.github.dozermapper.core.DozerBeanMapperBuilder
import com.github.dozermapper.core.Mapper
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl

object DozerConverter {

    private val mapper: Mapper = DozerBeanMapperBuilder.buildDefault()

    fun <O, D> parseObject(origin: O, destination: Class<D>?): D {
        return mapper.map(origin, destination)
    }

    fun <O, D> parseListObjects(origin: List<O>, destination: Class<D>?): ArrayList<D> {
        val destinationObjects: ArrayList<D> = ArrayList()
        for (o in origin) {
            destinationObjects.add(mapper.map(o, destination))
        }
        return destinationObjects
    }

    fun <O, D> parsePageOfObjects (origin: Page<O>?, destination: Class<D>?): Page<D> {
        val list: List<D> = parseListObjects(origin!!.content, destination)
        return PageImpl(list)
    }
}