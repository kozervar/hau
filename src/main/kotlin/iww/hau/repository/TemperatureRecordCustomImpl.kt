package iww.hau.repository

import iww.hau.model.TemperatureRecord
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import reactor.core.publisher.Flux
import java.util.*


class TemperatureRecordCustomImpl : TemperatureRecordCustom {

    @Autowired
    lateinit var reactiveMongoTemplate:ReactiveMongoTemplate

    override fun getByDate(from: Date, to: Date):Flux<TemperatureRecord> {

        var query = Query()

        query.addCriteria(Criteria.where("creationTime").gte(from).lt(to))

        return reactiveMongoTemplate.find(query, TemperatureRecord::class.java)
    }
}