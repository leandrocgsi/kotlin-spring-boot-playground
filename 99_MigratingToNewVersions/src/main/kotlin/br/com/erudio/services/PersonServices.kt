package br.com.erudio.services

import br.com.erudio.converter.DozerConverter
import br.com.erudio.data.model.Person
import br.com.erudio.data.vo.v1.PersonVO
import br.com.erudio.exception.ResourceNotFoundException
import br.com.erudio.repository.PersonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.function.Supplier

@Service
class PersonServices {
	@Autowired
	var repository: PersonRepository? = null
	fun create(person: PersonVO?): PersonVO {
		val entity: Person =
			DozerConverter.parseObject<PersonVO, Person>(person, Person::class.java)
		return DozerConverter.parseObject<Person, PersonVO>(
			repository!!.save(entity),
			PersonVO::class.java
		)
	}

	fun findPersonByName(firstName: String?, pageable: Pageable?): Page<PersonVO> {
		val page = repository!!.findPersonByName(firstName, pageable)
		return page!!.map { entity: Person? ->
			convertToPersonVO(
				entity
			)
		}
	}

	fun findAll(pageable: Pageable?): Page<PersonVO> {
		val page = repository!!.findAll(pageable!!)
		return page.map { entity: Person? ->
			convertToPersonVO(
				entity
			)
		}
	}

	private fun convertToPersonVO(entity: Person?): PersonVO {
		return DozerConverter.parseObject<Person, PersonVO>(entity, PersonVO::class.java)
	}

	fun findById(id: Long): PersonVO {
		val entity = repository!!.findById(id)
			.orElseThrow {
				ResourceNotFoundException(
					"No records found for this ID"
				)
			}!!
		return DozerConverter.parseObject<Person, PersonVO>(entity, PersonVO::class.java)
	}

	fun update(person: PersonVO): PersonVO {
		val entity: Person = repository.findById(person.key)
			.orElseThrow(Supplier {
				ResourceNotFoundException(
					"No records found for this ID"
				)
			})
		entity.firstName = person.firstName
		entity.lastName = person.lastName
		entity.address = person.address
		entity.gender = person.gender
		return DozerConverter.parseObject<Person, PersonVO>(
			repository.save(entity),
			PersonVO::class.java
		)
	}

	@Transactional
	fun disablePerson(id: Long): PersonVO {
		repository.disablePersons(id)
		val entity: Person = repository.findById(id)
			.orElseThrow(Supplier {
				ResourceNotFoundException(
					"No records found for this ID"
				)
			})
		return DozerConverter.parseObject<Person, PersonVO>(entity, PersonVO::class.java)
	}

	fun delete(id: Long) {
		val entity: Person = repository.findById(id)
			.orElseThrow(Supplier {
				ResourceNotFoundException(
					"No records found for this ID"
				)
			})
		repository.delete(entity)
	}
}