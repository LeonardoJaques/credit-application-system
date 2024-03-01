package dev.leonardo.requestcreditsystem.service.impl

import dev.leonardo.requestcreditsystem.entity.Customer
import dev.leonardo.requestcreditsystem.repository.CustomerRepository
import dev.leonardo.requestcreditsystem.service.ICustomerService
import org.springframework.stereotype.Service

@Service
class CustomerService(
    private val customerRepository: CustomerRepository,
) : ICustomerService {
    override fun save(customer: Customer): Customer = customerRepository.save(customer)

    override fun findById(id: Long): Customer = customerRepository.findById(id).orElseThrow {
        throw RuntimeException("$id not found!")
    }

    override fun delete(id: Long) = customerRepository.deleteById(id)

}