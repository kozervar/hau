package iww.hau.hau

import iww.hau.hau.model.TemperatureRecord
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface ReactiveTemperatureRecordRepository:ReactiveCrudRepository<TemperatureRecord, String>