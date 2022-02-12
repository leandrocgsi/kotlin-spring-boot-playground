package br.com.erudio.integrationtests.controller.withyaml.mapper

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import com.fasterxml.jackson.databind.ObjectMapper as JacksonObjectMapper;
import io.restassured.mapper.ObjectMapper;
import io.restassured.mapper.ObjectMapperDeserializationContext;
import io.restassured.mapper.ObjectMapperSerializationContext;

class YMLMapper : ObjectMapper {

    private val objectMapper: JacksonObjectMapper = JacksonObjectMapper(YAMLFactory())
    private var typeFactory: TypeFactory = TypeFactory.defaultInstance()

    init {
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        //typeFactory = TypeFactory.defaultInstance()
    }

    override fun deserialize(context: ObjectMapperDeserializationContext): Any? {
        try {
            val dataToSerialize = context.dataToDeserialize.asString()
            val type = context.type as Class<*>
            return objectMapper.readValue(dataToSerialize, typeFactory.constructType(type))
        } catch (e: JsonMappingException) {
            e.printStackTrace()
        } catch (e: JsonProcessingException) {
            e.printStackTrace()
        }
        return null
    }

    override fun serialize(context: ObjectMapperSerializationContext): Any? {
        try {
            return objectMapper.writeValueAsString(context.objectToSerialize)
        } catch (e: JsonProcessingException) {
            e.printStackTrace()
        }
        return null
    }
}