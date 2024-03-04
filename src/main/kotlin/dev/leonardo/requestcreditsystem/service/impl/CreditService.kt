package dev.leonardo.requestcreditsystem.service.impl

import dev.leonardo.requestcreditsystem.entity.Credit
import dev.leonardo.requestcreditsystem.exception.BusinessException
import dev.leonardo.requestcreditsystem.repository.CreditRepository
import dev.leonardo.requestcreditsystem.service.ICreditService
import org.springframework.stereotype.Service
import java.util.*

@Service
class CreditService(
    private val creditRepository: CreditRepository,
    private val customerService: CustomerService,
) : ICreditService {
    override fun save(credit: Credit): Credit {
        credit.apply {
            customer = customerService.findById(credit.customer?.id!!)
        }
        return this.creditRepository.save(credit)
    }

    override fun findAllByCustomerId(customerId: Long): List<Credit> =
        creditRepository.finAllVByCustomerId(customerId)

    override fun findByCreditCode(customerId: Long, creditCode: UUID): Credit {
        val credit = (creditRepository.findByCreditCode(creditCode)
            ?: throw BusinessException("CreditCode $creditCode not found!"))
        return if (credit.customer?.id == customerId) credit
        else throw IllegalArgumentException("Contact admin!")
    }
}