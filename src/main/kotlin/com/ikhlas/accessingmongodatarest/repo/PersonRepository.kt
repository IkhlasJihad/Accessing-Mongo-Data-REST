package com.ikhlas.accessingmongodatarest.repo

import com.ikhlas.accessingmongodatarest.model.PersonModel
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(collectionResourceRel = "people", path = "people")
interface PersonRepository: MongoRepository<PersonModel, String> {
    //  List<Person> findByLastName(@Param("name") String name);
    fun findByLastName(@Param("name") lastName: String): List<PersonModel>?
}