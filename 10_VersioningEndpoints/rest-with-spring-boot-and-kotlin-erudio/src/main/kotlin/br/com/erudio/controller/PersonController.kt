package br.com.erudio.controller

import br.com.erudio.data.vo.v2.PersonVOV2
import br.com.erudio.model.Person
import br.com.erudio.services.PersonServices
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/person")
class PersonController {
    @Autowired
    private lateinit  var service: PersonServices

    @GetMapping
    fun findAll(): List<Person> {
        return service.findAll()
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable("id") id: Long): Person {
        return service.findById(id)
    }

    @PostMapping
    fun create(@RequestBody person: Person?): Person {
        return service.create(person!!)
    }

    @PostMapping("/v2")
    fun createV2(@RequestBody person: PersonVOV2?): PersonVOV2? {
        return service.createV2(person)
    }

    @PutMapping
    fun update(@RequestBody person: Person?): Person {
        return service.update(person!!)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: Long): ResponseEntity<*> {
        service.delete(id)
        return ResponseEntity.ok().build<Any>()
    }
}