package iww.hau.repository

import iww.hau.model.TemperatureRecord
import reactor.core.publisher.Flux
import java.util.*


interface TemperatureRecordCustom {

    fun getByDate(from: Date, to:Date) : Flux<TemperatureRecord>
}