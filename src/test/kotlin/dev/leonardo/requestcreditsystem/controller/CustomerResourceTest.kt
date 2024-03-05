package dev.leonardo.requestcreditsystem.controller

import com.fasterxml.jackson.databind.ObjectMapper
import dev.leonardo.requestcreditsystem.dto.CustomerDto
import dev.leonardo.requestcreditsystem.dto.CustomerUpdateDto
import dev.leonardo.requestcreditsystem.entity.Customer
import dev.leonardo.requestcreditsystem.repository.CustomerRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal
import kotlin.random.Random

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
class CustomerResourceTest {
    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    companion object {
        const val URL: String = "/api/customers"
    }

    @BeforeEach
    fun setup() = customerRepository.deleteAll()

    @AfterEach
    fun tearDown() = customerRepository.deleteAll()

    @Test
    fun `should create a customer and return 201 status`() {
        //given
        val customerDto: CustomerDto = buildCustomerDto()
        val valueAsString: String = objectMapper.writeValueAsString(customerDto)
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders
                .post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(customerDto.firstName))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(customerDto.lastName))
            .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value(customerDto.cpf))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(customerDto.email))
            .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value(customerDto.zipCode))
            .andExpect(MockMvcResultMatchers.jsonPath("$.street").value(customerDto.street))
            .andExpect(MockMvcResultMatchers.jsonPath("$.income").value(customerDto.income.toDouble()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not create a customer with the same cpf and return 409 status`() {
        //given
        customerRepository.save(buildCustomerDto().toEntity())
        val customerDto: CustomerDto = buildCustomerDto()
        val valueAsString: String = objectMapper.writeValueAsString(customerDto)
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders
                .post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isConflict)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Conflict!, consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(409))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class org.springframework.dao.DataIntegrityViolationException")
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.detail").isNotEmpty
            )
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not save a customer with firstname empty and return 400 status`() {
        //given
        val customerDto: CustomerDto = buildCustomerDto(firstName = "")
        val valueAsString: String = objectMapper.writeValueAsString(customerDto)
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders
                .post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request!, consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class MethodArgumentNotValidException")
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.detail").isNotEmpty
            )
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should find customer by id return status 200`() {
        //given
        val customer: Customer = customerRepository.save(buildCustomerDto().toEntity())
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("$URL/${customer.id}")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(customer.firstName))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(customer.lastName))
            .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value(customer.cpf))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(customer.email))
            .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value(customer.address.zipCode))
            .andExpect(MockMvcResultMatchers.jsonPath("$.street").value(customer.address.street))
            .andExpect(MockMvcResultMatchers.jsonPath("$.income").value(customer.income.toDouble()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(customer.id))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not find customer when with invalid id and return 400`() {
        //given
        val invalidId = 1L
        //when //then
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("$URL/$invalidId")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request!, consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class BusinessException")
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.detail").isNotEmpty
            )
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should delete customer by id and return status 204`() {
        //given
        val customer: Customer = customerRepository.save(buildCustomerDto().toEntity())
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders
                .delete("$URL/${customer.id}")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isNoContent)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not delete customer with invalid id and return 400`() {
        //given
        val invalidId: Long = Random(1).nextLong(1, 1000)
        //when //then
        mockMvc.perform(
            MockMvcRequestBuilders
                .delete("$URL/$invalidId")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request!, consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class BusinessException")
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.detail").isNotEmpty
            )
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should update customer and return 200`() {
        //given
        val customer: Customer = customerRepository.save(buildCustomerDto().toEntity())
        val customerUpdateDto: CustomerUpdateDto = buildCustomerUpdateDto()
        val valueAsString: String = objectMapper.writeValueAsString(customerUpdateDto)
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders
                .patch("$URL?customerId=${customer.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(customerUpdateDto.firstName))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(customerUpdateDto.lastName))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(customerUpdateDto.email))
            .andExpect(MockMvcResultMatchers.jsonPath("$.zipCode").value(customerUpdateDto.zipCode))
            .andExpect(MockMvcResultMatchers.jsonPath("$.street").value(customerUpdateDto.street))
            .andExpect(MockMvcResultMatchers.jsonPath("$.income").value(customerUpdateDto.income.toDouble()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(customer.id))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should not update customer with invalid id and return 400 status`() {
        //given
        val invalidId: Long = Random(1).nextLong(1, 1000)
        val customerUpdateDto: CustomerUpdateDto = buildCustomerUpdateDto()
        val valueAsString: String = objectMapper.writeValueAsString(customerUpdateDto)
        //when //then
        mockMvc.perform(
            MockMvcRequestBuilders
                .patch("$URL?customerId=$invalidId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad Request!, consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.exception")
                    .value("class BusinessException")
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.detail").isNotEmpty
            )
            .andDo(MockMvcResultHandlers.print())
    }

    private fun buildCustomerDto(
        firstName: String = "Lana",
        lastName: String = "Del Penha",
        cpf: String = "28475934625",
        email: String = "lana@gmail.com",
        password: String = "1234",
        zipCode: String = "1234",
        street: String = "Rua da lana",
        income: BigDecimal = BigDecimal.valueOf(1000.0),
    ) = CustomerDto(
        firstName = firstName,
        lastName = lastName,
        cpf = cpf,
        email = email,
        password = password,
        zipCode = zipCode,
        street = street,
        income = income,
    )

    private fun buildCustomerUpdateDto(
        firstName: String = "Lana",
        lastName: String = "Del Penha",
        email: String = "lana@email.com",
        zipCode: String = "1234",
        street: String = "Rua da lana",
        income: BigDecimal = BigDecimal.valueOf(1000.0),
    ) = CustomerUpdateDto(
        firstName = firstName,
        lastName = lastName,
        email = email,
        zipCode = zipCode,
        street = street,
        income = income,
    )
}