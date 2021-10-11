package br.com.rafael.springboot.tutorial.thenewboston.datasource

import br.com.rafael.springboot.tutorial.thenewboston.model.Bank

interface BankDataSource {

    fun retrieveBanks(): Collection<Bank>

    fun retrieveBank(accountNumber: String): Bank

    fun createBank(bank: Bank): Bank

    fun updateBank(bank: Bank): Bank

    fun deleteBank(accountNumber: String)

}