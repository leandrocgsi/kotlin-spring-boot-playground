package br.com.erudio.unittests.mapper

import br.com.erudio.data.vo.v1.PersonVO
import br.com.erudio.mapper.DozerConverter
import br.com.erudio.model.Person
import br.com.erudio.unittests.mapper.mocks.MockPerson
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DozerConverterTest {

    var inputObject: MockPerson? = null

    @BeforeEach
    fun setUp() {
        inputObject = MockPerson()
    }

    @Test
    fun parseEntityToVOTest() {
        val output: PersonVO = DozerConverter.parseObject(inputObject!!.mockEntity(), PersonVO::class.java)
        assertEquals(0.toLong(), output.id)
        assertEquals("First Name Test0", output.firstName)
        assertEquals("Last Name Test0", output.lastName)
        assertEquals("Addres Test0", output.address)
        assertEquals("Male", output.gender)
    }

    @Test
    fun parseEntityListToVOListTest() {
        val outputList: ArrayList<PersonVO> =
            DozerConverter.parseListObjects(inputObject!!.mockEntityList(), PersonVO::class.java)

        val outputZero: PersonVO = outputList[0]

        assertEquals(0.toLong(), outputZero.id)
        assertEquals("First Name Test0", outputZero.firstName)
        assertEquals("Last Name Test0", outputZero.lastName)
        assertEquals("Addres Test0", outputZero.address)
        assertEquals("Male", outputZero.gender)

        val outputSeven: PersonVO = outputList[7]
        assertEquals(7.toLong(), outputSeven.id)
        assertEquals("First Name Test7", outputSeven.firstName)
        assertEquals("Last Name Test7", outputSeven.lastName)
        assertEquals("Addres Test7", outputSeven.address)
        assertEquals("Female", outputSeven.gender)

        val outputTwelve: PersonVO = outputList[12]
        assertEquals(12.toLong(), outputTwelve.id)
        assertEquals("First Name Test12", outputTwelve.firstName)
        assertEquals("Last Name Test12", outputTwelve.lastName)
        assertEquals("Addres Test12", outputTwelve.address)
        assertEquals("Male", outputTwelve.gender)
    }

    @Test
    fun parseVOToEntityTest() {

        val output: Person = DozerConverter.parseObject(inputObject!!.mockVO(), Person::class.java)

        assertEquals(0.toLong(), output.id)
        assertEquals("First Name Test0", output.firstName)
        assertEquals("Last Name Test0", output.lastName)
        assertEquals("Addres Test0", output.address)
        assertEquals("Male", output.gender)
    }

    @Test
    fun parserVOListToEntityListTest() {

        val outputList: ArrayList<Person> = DozerConverter.parseListObjects(inputObject!!.mockVOList(), Person::class.java)

        val outputZero: Person = outputList[0]
        assertEquals(0.toLong(), outputZero.id)
        assertEquals("First Name Test0", outputZero.firstName)
        assertEquals("Last Name Test0", outputZero.lastName)
        assertEquals("Addres Test0", outputZero.address)
        assertEquals("Male", outputZero.gender)

        val outputSeven: Person = outputList[7]
        assertEquals(7.toLong(), outputSeven.id)
        assertEquals("First Name Test7", outputSeven.firstName)
        assertEquals("Last Name Test7", outputSeven.lastName)
        assertEquals("Addres Test7", outputSeven.address)
        assertEquals("Female", outputSeven.gender)

        val outputTwelve: Person = outputList[12]
        assertEquals(12.toLong(), outputTwelve.id)
        assertEquals("First Name Test12", outputTwelve.firstName)
        assertEquals("Last Name Test12", outputTwelve.lastName)
        assertEquals("Addres Test12", outputTwelve.address)
        assertEquals("Male", outputTwelve.gender)
    }
}