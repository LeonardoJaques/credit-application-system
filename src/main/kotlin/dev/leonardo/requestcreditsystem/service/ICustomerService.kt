package dev.leonardo.requestcreditsystem.service

import dev.leonardo.requestcreditsystem.entity.Customer

interface ICustomerService {
    fun save(customer: Customer): Customer
    fun findById(id: Long): Customer

    fun delete(id: Long)
}