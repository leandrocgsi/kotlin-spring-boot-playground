package br.com.erudio.controller

import br.com.erudio.data.vo.v1.PersonVO
import br.com.erudio.services.PersonServices
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/person/v1")
class PersonController {

    @Autowired
    private lateinit  var service: PersonServices

    @GetMapping
    fun findAll(): List<PersonVO>? {
        return service.findAll()
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable("id") id: Long): PersonVO? {
        return service.findById(id)
    }

    @PostMapping
    fun create(@RequestBody person: PersonVO?): PersonVO? {
        return service.create(person)
    }

    @PutMapping
    fun update(@RequestBody person: PersonVO?): PersonVO? {
        return service.update(person)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: Long): ResponseEntity<*> {
        service.delete(id)
        return ResponseEntity.ok().build<Any>()
    }
}