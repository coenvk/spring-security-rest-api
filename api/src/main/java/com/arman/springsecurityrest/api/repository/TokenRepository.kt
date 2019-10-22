package com.arman.springsecurityrest.api.repository

import com.arman.springsecurityrest.common.model.Token
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.*
import javax.transaction.Transactional

@Repository
interface TokenRepository : CrudRepository<Token, Long> {

    fun findByToken(token: String): Optional<Token>

    fun findByTokenAndTokenTypeAndExpiryDateGreaterThan(token: String, tokenType: String, date: LocalDate): Optional<Token>

    fun findAllByExpiryDateLessThanEqual(date: LocalDate): MutableIterable<Token>

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM tokens t WHERE t.expiry_date <= :date")
    fun deleteAllByExpiryDateLessThanEqual(@Param("date") date: LocalDate)

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "ALTER SEQUENCE tokens_id_seq RESTART WITH 1;")
    fun resetIdSequence()

}