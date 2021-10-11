package br.com.rafael.springboot.tutorial.thenewboston.datasource.network.dto

import br.com.rafael.springboot.tutorial.thenewboston.model.Bank

data class BankList(
    val results: Collection<Bank>
)

