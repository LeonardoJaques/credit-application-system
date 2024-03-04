package dev.leonardo.requestcreditsystem.dto

import dev.leonardo.requestcreditsystem.entity.Address
import dev.leonardo.requestcreditsystem.entity.Customer
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class CustomerUpdateDto(
    @field:NotEmpty(message = "Invalid input")
    val firstName: String,
    @field:NotEmpty(message = "Invalid input")
    val lastName: String,
    @field:NotNull(message = "Invalid input")
    val income: BigDecimal,
    @field:NotEmpty(message = "Invalid input")
    val email: String,
    @field:NotEmpty(message = "Invalid input")
    val zipCode: String,
    @field:NotEmpty(message = "Invalid input")
    val street: String,
) {

    fun toEntity(
        customer: Customer,
    ): Customer {
        customer.firstName = this.firstName
        customer.lastName = this.lastName
        customer.income = this.income
        customer.email = this.email
        customer.address = Address(
            zipCode = this.zipCode,
            street = this.street
        )
        return customer
    }

}
