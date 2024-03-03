package dev.leonardo.requestcreditsystem.dto

import dev.leonardo.requestcreditsystem.entity.Address
import dev.leonardo.requestcreditsystem.entity.Customer
import java.math.BigDecimal

data class CustomerUpdateDto(
    val firstName: String,
    val lastName: String,
    val cpf: String,
    val income: BigDecimal,
    val email: String,
    val zipCode: String,
    val street: String,
    ) {

    fun toEntity(
        customer: Customer
    ):Customer {
        customer.firstName = this.firstName
        customer.lastName = this.lastName
        customer.cpf = this.cpf
        customer.income = this.income
        customer.email = this.email
        customer.address = Address(
            zipCode = this.zipCode,
            street = this.street
        )
        return customer
    }

}
