package br.com.rafael.springboot.tutorial.thenewboston.controller

import br.com.rafael.springboot.tutorial.thenewboston.model.Bank
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.*

@SpringBootTest
@AutoConfigureMockMvc
internal class BankControllerTest @Autowired constructor (

    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper

) {

    val baseUrl = "/api/banks"

    @Nested
    @DisplayName("GET /api/banks")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class GetBanks {

        @Test
        fun `should return all banks`() {
            //when/then
            mockMvc.get(baseUrl)
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$[0].account_number") { value("1234") }
                }
        }
    }

    @Nested
    @DisplayName("GET /api/banks/{account_number}")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class GetBank {

        @Test
        fun `should return the bank with the given account number`() {

            val accountNumber = 1234

            mockMvc.get("$baseUrl/$accountNumber")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.trust") { value("3.14") }
                    jsonPath("$.default_transaction_fee") { value("17") }
                }

        }

        @Test
        fun `should return NotFound if the accountNumber doest exist`() {
            // GIVEN
            val accountNumber = "does_not_exist"

            // WHEN // THEN
            mockMvc.get("$baseUrl/$accountNumber")
                .andDo { print() }
                .andExpect { status { isNotFound() } }
        }

    }
    
    @Nested
    @DisplayName("POST /api/banks")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class PostNewBank {
    
        @Test
        fun `should add the new bank`() {
            // GIVEN
            val newBank = Bank("abc123", 31.415, 2)
            
            // WHEN
            val performPost = mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(newBank)
            }
            // THEN
            performPost
                .andDo { print() }
                .andExpect {
                    status { isCreated() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        json(objectMapper.writeValueAsString(newBank))
                    }
                }

            mockMvc.get("$baseUrl/${newBank.accountNumber}")
                .andExpect { content { json(objectMapper.writeValueAsString(newBank)) } }
        
        }

        @Test
        fun `should return BAD REQUEST if bank with given accountNumber already exists`() {
            // GIVEN
            val invalidBank = Bank("1234", 1.0, 1)

            // WHEN
            val performPost = mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(invalidBank)
            }

            // THEN
            performPost
                .andDo { print() }
                .andExpect {
                    status { isBadRequest() }
                }

        }
    
    }
    
    @Nested
    @DisplayName("PATCH /api/banks")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class PatchExistingBank {

        @Test
        fun `should update an existing bank`() {
            // GIVEN
            val updatedBank = Bank("1234", 1.0, 1)

            // WHEN
            val performPatchRequest = mockMvc.patch(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(updatedBank)
            }

            // THEN
            performPatchRequest
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        json(objectMapper.writeValueAsString(updatedBank))
                    }
                }
            mockMvc.get("$baseUrl/${updatedBank.accountNumber}")
                .andExpect { content { json(objectMapper.writeValueAsString(updatedBank)) } }
        }

        @Test
        fun `should return BAD REQUEST if no bank with given accountNumber exists`() {
            // GIVEN
            val invalidBank = Bank("does_not_exist", 1.0, 1)

            // WHEN
            val performPatchRequest = mockMvc.patch(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(invalidBank)
            }

            // THEN
            performPatchRequest
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                }
        }
    }

    @Nested
    @DisplayName("DELETE /api/banks/{accountNumber}")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class DeleteExistingBank {

        @Test
        @DirtiesContext
        fun `should delete the bank with the given accountNumber`() {
            // GIVEN
            val accountNumber = 1234

            // WHEN
            mockMvc.delete("$baseUrl/$accountNumber")

                // THEN
                .andDo { print() }
                .andExpect {
                    status { isNoContent() }
                }
            mockMvc.get("$baseUrl/$accountNumber")
                .andExpect { status { isNotFound() } }
        }

        @Test
        fun `should return NOT FOUND if no bank with given accountNumber exists`() {
            // GIVEN
            val invalidAccountNumber = "does_not_exist"

            // WHEN
            mockMvc.delete("$baseUrl/$invalidAccountNumber")

            // THEN
                .andDo { print() }
                .andExpect { status { isNotFound() } }

        }

    }

}