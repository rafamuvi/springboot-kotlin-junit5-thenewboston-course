package br.com.rafael.springboot.tutorial.thenewboston.datasource.network

import br.com.rafael.springboot.tutorial.thenewboston.datasource.BankDataSource
import br.com.rafael.springboot.tutorial.thenewboston.datasource.network.dto.BankList
import br.com.rafael.springboot.tutorial.thenewboston.model.Bank
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Repository
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity
import java.io.IOException

@Repository("network")
class NetworkDataSource(
    @Autowired private val restTemplate: RestTemplate
) : BankDataSource {

    private val baseUrl: String = "http://54.193.31.159/banks"

    override fun retrieveBanks(): Collection<Bank> {
        val response: ResponseEntity<BankList> =
            restTemplate.getForEntity(baseUrl)

        return response.body?.results
            ?: throw IOException("Could not fetch banks from the network")
    }

    override fun retrieveBank(accountNumber: String): Bank {
        TODO("Not yet implemented")
    }

    override fun createBank(bank: Bank): Bank {
        TODO("Not yet implemented")
    }

    override fun updateBank(bank: Bank): Bank {
        TODO("Not yet implemented")
    }

    override fun deleteBank(accountNumber: String) {
        TODO("Not yet implemented")
    }
}