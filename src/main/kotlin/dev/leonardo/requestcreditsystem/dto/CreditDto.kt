package dev.leonardo.requestcreditsystem.dto

import dev.leonardo.requestcreditsystem.entity.Credit
import dev.leonardo.requestcreditsystem.entity.Customer
import jakarta.validation.constraints.Future
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Range
import java.math.BigDecimal
import java.time.LocalDate

data class CreditDto(
    @field:NotNull(message = "Invalid input") val creditValue: BigDecimal,
    @field:Future( message = "Invalid input") val dayFirstOfInstallment: LocalDate,
    @field:Range(min=1, max = 48, message = "Invalid value") val numberOfInstallments: Int,
    @field:NotNull(message = "Invalid input") val customerId: Long,

    ) {
    fun toEntity(): Credit = Credit(
        creditValue = this.creditValue,
        dayFirstInstallment = this.dayFirstOfInstallment,
        numberOfInstallments = this.numberOfInstallments,
        customer = Customer(id = this.customerId)
    )
}
