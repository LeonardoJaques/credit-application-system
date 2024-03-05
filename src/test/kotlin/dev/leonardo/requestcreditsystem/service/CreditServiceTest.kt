package dev.leonardo.requestcreditsystem.service

import dev.leonardo.requestcreditsystem.entity.Address
import dev.leonardo.requestcreditsystem.entity.Credit
import dev.leonardo.requestcreditsystem.entity.Customer
import dev.leonardo.requestcreditsystem.exception.BusinessException
import dev.leonardo.requestcreditsystem.repository.CreditRepository
import dev.leonardo.requestcreditsystem.service.impl.CreditService
import dev.leonardo.requestcreditsystem.service.impl.CustomerService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class CreditServiceTest {

    @MockK
    lateinit var creditRepository: CreditRepository

    @MockK
    lateinit var customerService: CustomerService

    @InjectMockKs
    lateinit var creditService: CreditService

    private fun buildCustomer(
        firstName: String = "Lana",
        lastName: String = "Del Penha",
        cpf: String = "65093495077",
        income: BigDecimal = BigDecimal.valueOf(1000.00),
        email: String = "lana@dog.com",
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

    private fun buildCredit(
        creditCode: UUID = UUID.randomUUID(),
        creditValue: BigDecimal = BigDecimal.valueOf(1000.00),
        dayFirstInstallment: LocalDate = LocalDate.now(),
        numberOfInstallments: Int = 10,
        customer: Customer = buildCustomer(),
        id: Long = 1L
    ) = Credit(
        creditCode = creditCode,
        creditValue = creditValue,
        dayFirstInstallment = dayFirstInstallment,
        numberOfInstallments = numberOfInstallments,
        customer = customer,
        id = id
    )



    @Test
    fun `should create a credit`() {
        // given
        val fakeCredit = buildCredit()
        every { creditRepository.save(any()) } returns fakeCredit
        every { customerService.findById(any()) } returns buildCustomer()
        // when
        val actual: Credit = creditService.save(fakeCredit)
        // then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(fakeCredit)
        Assertions.assertThat(actual.customer).isNotNull
        verify(exactly = 1) {
            creditRepository.save(fakeCredit)
        }
    }

    @Test
    fun `should find a credit by CreditCode` (){
        // given
        val fakeCustomer = buildCustomer()
        val fakeCredit = buildCredit(customer = fakeCustomer)
        every { creditRepository.findByCreditCode(fakeCredit.creditCode) } returns fakeCredit
        // when
        val actual: Credit = creditService.findByCreditCode(fakeCustomer.id!!, fakeCredit.creditCode)

        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(fakeCredit)
        Assertions.assertThat(actual).isExactlyInstanceOf(Credit::class.java)
        verify(exactly = 1) {
            creditRepository.findByCreditCode(fakeCredit.creditCode)
        }

    }

    @Test
    fun `should not find a credit by CreditCode` (){
        // given
        val fakeCustomer = buildCustomer()
        val fakeCredit = buildCredit(customer = fakeCustomer)
        every { creditRepository.findByCreditCode(fakeCredit.creditCode) } returns null
        // when
        // then
        Assertions.assertThatExceptionOfType(BusinessException::class.java)
            .isThrownBy {
                creditService.findByCreditCode(fakeCustomer.id!!, fakeCredit.creditCode)
            }.withMessage("CreditCode ${fakeCredit.creditCode} not found!")

    }

    @Test
    fun `should not find a credit by invalid customerId` (){
        // given
        val fakeCustomer = buildCustomer()
        val fakeCredit = buildCredit(customer = fakeCustomer)
        every { creditRepository.findByCreditCode(fakeCredit.creditCode) } returns fakeCredit
        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java)
            .isThrownBy {
                creditService.findByCreditCode(2L, fakeCredit.creditCode)
            }.withMessage("Contact admin!")

    }

    @Test
    fun `should not create a credit with invalid dayFirstInstallment` (){
        // given
        val fakeCredit = buildCredit(dayFirstInstallment = LocalDate.now().plusMonths(3).plusDays(1))
        // when
        // then
        Assertions.assertThatExceptionOfType(BusinessException::class.java)
            .isThrownBy {
                creditService.save(fakeCredit)
            }.withMessage("Invalid Date")

    }

    @Test
    fun `should find all customers by customerId` (){
        // given
        val fakeCustomer = buildCustomer()
        val fakeCredit = buildCredit(customer = fakeCustomer)
        every { creditRepository.findAllVByCustomerId(fakeCustomer.id!!) } returns listOf(fakeCredit)
        // when
        val actual: List<Credit> = creditService.findAllByCustomerId(fakeCustomer.id!!)

        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isNotEmpty
        Assertions.assertThat(actual).hasSize(1)
        Assertions.assertThat(actual[0]).isSameAs(fakeCredit)
        verify(exactly = 1) {
            creditRepository.findAllVByCustomerId(fakeCustomer.id!!)
        }

    }
}