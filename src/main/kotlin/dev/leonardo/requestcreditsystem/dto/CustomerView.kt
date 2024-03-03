package dev.leonardo.requestcreditsystem.dto

import dev.leonardo.requestcreditsystem.entity.Customer

data class CustomerView(
    val firstName: String,
    val lastName: String,
    val cpf: String,
    val income: String,
    val email: String,
    val zipCode: String,
    val street: String,
){
    constructor(customer: Customer): this(
       firstName = customer.firstName,
       lastName = customer.lastName,
       email = customer.email,
       cpf = customer.cpf,
       income = customer.income.toString(),
       zipCode = customer.address.zipCode,
       street = customer.address.street,
    )
}
