package dev.leonardo.requestcreditsystem.dto

import dev.leonardo.requestcreditsystem.entity.Credit
import java.math.BigDecimal
import java.util.*

data class CreditViewList(
    val creditCode: UUID,
    val creditValue: BigDecimal,
    val numberInstallments: Int,
) {
    constructor(credit: Credit) : this(
        creditCode = credit.creditCode,
        creditValue = credit.creditValue,
        numberInstallments = credit.numberOfInstallments,

    )

}
