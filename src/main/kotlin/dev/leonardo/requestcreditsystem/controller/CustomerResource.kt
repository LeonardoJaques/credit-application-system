package dev.leonardo.requestcreditsystem.controller

import dev.leonardo.requestcreditsystem.dto.CustomerDto
import dev.leonardo.requestcreditsystem.dto.CustomerUpdateDto
import dev.leonardo.requestcreditsystem.dto.CustomerView
import dev.leonardo.requestcreditsystem.entity.Customer
import dev.leonardo.requestcreditsystem.service.impl.CustomerService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/customers")
class CustomerResource(
    private val customerService: CustomerService,
) {
    @PostMapping
    fun customerSave(@RequestBody @Valid customerDto: CustomerDto): ResponseEntity<String> {
        val savedCustomer = this.customerService.save(customerDto.toEntity())
        return ResponseEntity.status(HttpStatus.CREATED)
            .body("Customer ${savedCustomer.email} saved!")
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<CustomerView> {
        val customer: Customer = this.customerService.findById(id)
        return ResponseEntity.status(HttpStatus.OK)
            .body(CustomerView(customer))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCustomer(@PathVariable id: Long): ResponseEntity<String> {
        this.customerService.delete(id)
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .body("Customer $id deleted!")
    }

    @PatchMapping
    fun updateCustomer(
        @RequestParam(value = "customerId") id: Long,
        @RequestBody
        @Valid customerUpdateDto: CustomerUpdateDto,
    ): ResponseEntity<CustomerView> {
        val customer: Customer = this.customerService.findById(id)
        val customerToUpdate: Customer = customerUpdateDto.toEntity(customer)
        val customerUpdated: Customer = this.customerService.save(customerToUpdate)
        return ResponseEntity.status(HttpStatus.OK)
            .body(CustomerView(customerUpdated))
    }
}