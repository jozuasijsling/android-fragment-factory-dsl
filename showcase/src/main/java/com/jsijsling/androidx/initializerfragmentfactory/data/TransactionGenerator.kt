package com.jsijsling.androidx.initializerfragmentfactory.data

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import java.math.BigDecimal
import java.util.Currency
import kotlin.random.Random
import kotlin.random.nextLong

fun generateTransaction(number: Int): Transaction {
    val seededRng = Random(number)
    val accountNumber = (seededRng.nextLong(0..<100_000_000_000_000L)).toString()
        .padStart(length = 15, padChar = '0')
    val randomLoremIpsumWordIndex = seededRng.nextInt(until = 500)
    val currency = Currency.getInstance("EUR")
    val loremIpsumWords = LoremIpsum().values.first().splitToSequence("\\s".toRegex())
    return Transaction(
        id = number,
        value = MonetaryValue(
            amount = BigDecimal(number).apply {
                setScale(currency.defaultFractionDigits)
            },
            currency = currency,
        ),
        beneficiaryName = loremIpsumWords.elementAt(number).trimEnd { it in setOf(',', '.') },
        beneficiaryIban = "GB82WEST$accountNumber",
        description = loremIpsumWords.elementAt(randomLoremIpsumWordIndex).trimEnd { it in setOf(',', '.') },
    )
}
