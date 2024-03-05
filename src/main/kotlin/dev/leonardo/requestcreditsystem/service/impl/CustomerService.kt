package dev.leonardo.requestcreditsystem.service.impl

import dev.leonardo.requestcreditsystem.entity.Customer
import dev.leonardo.requestcreditsystem.exception.BusinessException
import dev.leonardo.requestcreditsystem.repository.CustomerRepository
import dev.leonardo.requestcreditsystem.service.ICustomerService
import org.springframework.stereotype.Service

@Service
class CustomerService(
    private val customerRepository: CustomerRepository,
) : ICustomerService {
    override fun save(customer: Customer): Customer = this.customerRepository.save(customer)

    override fun findById(id: Long): Customer = this.customerRepository.findById(id).orElseThrow {
        throw BusinessException("Id $id not found!")
    }

    override fun delete(id: Long) {
        val customer: Customer = this.findById(id)
        this.customerRepository.delete(customer)
    }
    

}