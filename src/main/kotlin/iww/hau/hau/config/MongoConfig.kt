package iww.hau.hau.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import iww.hau.hau.ReactiveTemperatureRecordRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

@Configuration
@EnableReactiveMongoRepositories(basePackageClasses = arrayOf(ReactiveTemperatureRecordRepository::class))
class MongoConfig : AbstractReactiveMongoConfiguration() {

    @Bean
    fun objectMapper(): ObjectMapper {
        return Jackson2ObjectMapperBuilder.json().build()
    }

    override fun mongoClient(): MongoClient {
        return MongoClients.create()
    }

    override fun getDatabaseName(): String? {
        return "reactive"
    }
}