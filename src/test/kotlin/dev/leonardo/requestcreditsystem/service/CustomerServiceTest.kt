package dev.leonardo.requestcreditsystem.service

import dev.leonardo.requestcreditsystem.entity.Address
import dev.leonardo.requestcreditsystem.entity.Customer
import dev.leonardo.requestcreditsystem.exception.BusinessException
import dev.leonardo.requestcreditsystem.repository.CustomerRepository
import dev.leonardo.requestcreditsystem.service.impl.CustomerService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.util.*
import kotlin.random.Random

@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class CustomerServiceTest {
    @MockK
    lateinit var customerRepository: CustomerRepository

    @InjectMockKs
    lateinit var customerService: CustomerService

    private fun buildCustomer(
        firstName: String = "Lana",
        lastName: String = "Del Penha",
        cpf: String = "65093495077",
        income: BigDecimal = BigDecimal.valueOf(1000.00),
        email: String = "lana@dog.com.br",
        password: String = "1234",
        zipCode: String = "1111",
        street: String = "dog street",
        id: Long = 1L
    ) = Customer(
        firstName = firstName,
        lastName = lastName,
        cpf = cpf,
        income = income,
        email = email,
        password = password,
        address = Address(
            zipCode = zipCode,
            street = street
        ),
        id = id
    )

    @Test
    fun `should create a customer`() {
        // given
        val fakeCustomer = buildCustomer()
        every { customerRepository.save(any()) } returns fakeCustomer
        // when
        val actual: Customer = customerService.save(fakeCustomer)
        // then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(fakeCustomer)
        verify(exactly = 1) {
            customerRepository.save(fakeCustomer)
        }
    }

    @Test
    fun `should find a customer by id` (){
        // given
        val fakeId: Long = Random.nextLong()
        val fakeCustomer = buildCustomer(id = fakeId)
        every { customerRepository.findById(fakeId) } returns Optional.of(fakeCustomer)
        // when
       val actual: Customer = customerService.findById(fakeId)

        // then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(fakeCustomer)
        Assertions.assertThat(actual).isExactlyInstanceOf(Customer::class.java)
        verify(exactly = 1) {
            customerRepository.findById(fakeId)
        }
    }

    @Test
    fun `should not find a customer by invalid id and throw BusinessException`(){
        // given
        val fakeId: Long = Random.nextLong()
        every { customerRepository.findById(fakeId) } returns Optional.empty()
        // when
        // then
        Assertions.assertThatExceptionOfType(BusinessException::class.java)
            .isThrownBy {
            customerService.findById(fakeId)
        }.withMessage("Id $fakeId not found!")

        verify(exactly = 1) {
            customerRepository.findById(fakeId)
        }
    }
    @Test
    fun `should delete a customer by id` (){
        // given
        val fakeId: Long = Random.nextLong()
        val fakeCustomer = buildCustomer(id = fakeId)
        every { customerRepository.findById(fakeId) } returns Optional.of(fakeCustomer)
        every { customerRepository.delete(fakeCustomer) } just runs
        // when
        customerService.delete(fakeId)
        // then
        verify(exactly = 1) {
            customerRepository.findById(fakeId)
            customerRepository.delete(fakeCustomer)
        }
    }


}