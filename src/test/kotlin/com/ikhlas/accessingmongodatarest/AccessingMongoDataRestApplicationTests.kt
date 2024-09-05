package com.ikhlas.accessingmongodatarest

import com.fasterxml.jackson.databind.ObjectMapper
import com.ikhlas.accessingmongodatarest.repo.PersonRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AccessingMongoDataRestApplicationTests @Autowired constructor(
    private val mvc: MockMvc,
    private val personRepo: PersonRepository
){

    @BeforeEach
    fun setUp() {
        personRepo.deleteAll() // Clear repository before each test
    }

    @Test
    fun contextLoads() {
        // ensures that the Spring application context loads correctly
    }

    @Test
    fun `should return Person Repository index`(){
        mvc.perform(get("/")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$._links.people").exists())
    }

    @Test
    fun `post people should create person document`(){
        val payload = mapOf("firstName" to "Ikhlas", "lastName" to "Aydi")
        val mvcResult = mvc.perform(
            post("/people")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(payload))
        ).andReturn()

        val locationHeader = mvcResult.response.getHeader("Location")

        mvc.perform(get(locationHeader!!))
        .andExpect(status().isOk)
        .andExpect(jsonPath("firstName").value(payload["firstName"]))
        .andExpect(jsonPath("lastName").value(payload["lastName"]))
    }

    @Test
    fun `get people should retrieve entity`(){
        val payload = mapOf("firstName" to "Fredo", "lastName" to "Bill")
        val mvcResult = mvc.perform(
            post("/people")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(payload))
        ).andExpect(status().isCreated)
            .andReturn()
        val location: String = mvcResult.response.getHeader(HttpHeaders.LOCATION).toString()
        mvc.perform(get(location))
            .andExpect(status().isOk)
            .andExpect(jsonPath("firstName").value(payload["firstName"]))
            .andExpect(jsonPath("lastName").value(payload["lastName"]))
    }

    @Test
    fun `should update entity`(){
        val payload = mutableMapOf("firstName" to "Fredo", "lastName" to "Bill")
        val mvcResult: MvcResult = mvc.perform(
            post("/people")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(payload))
        ).andExpect(
            status().isCreated()
        ).andReturn()

        val location = mvcResult.response.getHeader("Location")
        payload["firstName"] = "Ferid"

        mvc.perform(
            put(location!!)
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(payload))
        ).andExpect(
            status().isNoContent()
        )

        mvc.perform(get(location!!)).andExpect(status().isOk()).andExpect(
            jsonPath("firstName").value(payload["firstName"])
        ).andExpect(
            jsonPath("lastName").value(payload["lastName"])
        )
    }

}
