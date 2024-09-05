package com.ikhlas.accessingmongodatarest.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "people")
class PersonModel (
    @Id private val id: String?,
    private var firstName: String,
    private var lastName: String,
    ){

    fun getFirstName(): String { return firstName }

    fun setFirstName(firstName: String) { this.firstName = firstName }

    fun getLastName(): String { return lastName }

    fun setLastName(lastName: String) { this.lastName = lastName}
}