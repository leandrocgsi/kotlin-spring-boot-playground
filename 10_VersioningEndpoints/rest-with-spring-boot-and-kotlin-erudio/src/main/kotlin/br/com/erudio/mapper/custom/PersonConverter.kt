package br.com.erudio.mapper.custom

import br.com.erudio.data.vo.v2.PersonVOV2
import br.com.erudio.model.Person
import org.springframework.stereotype.Service
import java.util.*

@Service
class PersonConverter {
    fun convertEntityToVO(person: Person): PersonVOV2 {
        val vo = PersonVOV2()
        vo.id = person.id
        vo.address = person.address
        vo.birthDay = Date()
        vo.firstName = person.firstName
        vo.lastName = person.lastName
        vo.gender = person.gender
        return vo
    }

    fun convertVOToEntity(person: PersonVOV2): Person {
        val entity = Person()
        entity.id = person.id
        entity.address = person.address
        entity.firstName = person.firstName
        entity.lastName = person.lastName
        entity.gender = person.gender
        return entity
    }
}