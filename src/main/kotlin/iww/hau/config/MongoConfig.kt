package iww.hau.config

import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import iww.hau.repository.ReactiveTemperatureRecordRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories

@Configuration
@EnableReactiveMongoRepositories(basePackageClasses = arrayOf(ReactiveTemperatureRecordRepository::class))
@AutoConfigureAfter(EmbeddedMongoAutoConfiguration::class)
class MongoConfig : AbstractReactiveMongoConfiguration() {

    @Value("\${spring.data.mongodb.host}")
    private val mongoDbHost: String? = null


    @Value("\${spring.data.mongodb.port}")
    private val mongoDbPort: Int? = null

    @Bean
    override fun mongoClient(): MongoClient {
        return MongoClients.create("mongodb://$mongoDbHost:$mongoDbPort")
    }

    override fun getDatabaseName(): String? {
        return "reactive"
    }
}