package iww.hau.repository

import iww.hau.model.TemperatureRecord
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import java.util.*

interface ReactiveTemperatureRecordRepository:ReactiveCrudRepository<TemperatureRecord, String> {

    fun findByCreationTimeAfter(creationTime: Date): Flux<TemperatureRecord>

    fun findByCreationTimeBetween(fromCreationTime: Date, toCreationTime: Date): Flux<TemperatureRecord>
}