package dev.leonardo.requestcreditsystem.controller

import dev.leonardo.requestcreditsystem.dto.CreditDto
import dev.leonardo.requestcreditsystem.dto.CreditView
import dev.leonardo.requestcreditsystem.dto.CreditViewList
import dev.leonardo.requestcreditsystem.entity.Credit
import dev.leonardo.requestcreditsystem.service.impl.CreditService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.stream.Collectors

@RestController
@RequestMapping("/api/credits")
class CreditResource(
    private val creditService: CreditService,
) {
    @PostMapping
    fun saveCredit(@RequestBody @Valid creditDto: CreditDto): ResponseEntity<String> {
        val credit: Credit = this.creditService.save(creditDto.toEntity())
        return ResponseEntity.status(HttpStatus.CREATED)
            .body("Credit ${credit.creditCode} - ${credit.customer?.email} saved!")
    }

    @GetMapping
    fun findAllByCustomerId(@RequestParam(value = "customerId") customerId: Long): ResponseEntity<List<CreditViewList>> {
        val creditViewList: List<CreditViewList> = this.creditService.findAllByCustomerId(customerId)
            .stream()
            .map { credit -> CreditViewList(credit) }
            .collect(Collectors.toList())
        return ResponseEntity.status(HttpStatus.OK)
            .body(creditViewList)
    }

    @GetMapping("/{creditCode}")
    fun findByCreditCode(
        @RequestParam(value = "customerId") customerId: Long,
        @PathVariable creditCode: UUID,
    ): ResponseEntity<CreditView> {
        val credit: Credit = this.creditService
            .findByCreditCode(customerId, creditCode)
        return ResponseEntity.status(HttpStatus.OK)
            .body(CreditView(credit))
    }

}