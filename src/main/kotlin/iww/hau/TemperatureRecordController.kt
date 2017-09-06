package iww.hau

import iww.hau.model.TemperatureRecord
import iww.hau.model.TemperatureRequest
import iww.hau.repository.ReactiveTemperatureRecordRepository
import mu.KLogging
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@RestController
@RequestMapping("/temperature")
class TemperatureRecordController(val repository: ReactiveTemperatureRecordRepository) {

    companion object : KLogging()

    @PostMapping
    fun createTempRecord(@ModelAttribute temperatureReq: TemperatureRequest) {
        val temperatureRecord = mapToRecord(temperatureReq)
        repository.save(temperatureRecord).subscribe()

        logger.debug { "Record saved for tempReq = $temperatureReq " }
    }


    @GetMapping()
    fun getAll(@RequestParam(name = "fd", required = false, defaultValue = "-1") fDay: Int,
               @RequestParam(name = "fm", required = false, defaultValue = "-1") fMonth: Int,
               @RequestParam(name = "fy", required = false, defaultValue = "-1") fYear: Int,
               @RequestParam(name = "td", required = false, defaultValue = "-1") tDay: Int,
               @RequestParam(name = "tm", required = false, defaultValue = "-1") tMonth: Int,
               @RequestParam(name = "ty", required = false, defaultValue = "-1") tYear: Int): Flux<TemperatureRecord> {

        val now = LocalDate.now()

        val fromLocalDate = LocalDate.of(
                if (fYear > 0) fYear else now.year,
                if (fMonth > 0) fMonth else now.monthValue,
                if (fDay > 0) fDay else now.dayOfMonth
        )
        val fromDate = Date.from(fromLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant())

        val toLocalDate = LocalDateTime.of(
                if (tYear > 0) tYear else now.year,
                if (tMonth > 0) tMonth else now.monthValue,
                if (tDay > 0) tDay else now.dayOfMonth,
                23,
                59,
                59
        )
        val toDate = Date.from(toLocalDate.atZone(ZoneId.systemDefault()).toInstant())

        return repository.findByCreationTimeBetween(fromDate, toDate)
    }

    private fun mapToRecord(temperatureReq: TemperatureRequest): TemperatureRecord {
        val temperatureRecord = TemperatureRecord()
        temperatureRecord.ipAddress = temperatureReq.ipAddress
        temperatureRecord.deviceName = temperatureReq.deviceName
        temperatureRecord.rssi = temperatureReq.rssi
        temperatureRecord.light = temperatureReq.light
        temperatureRecord.insideTemp = temperatureReq.insideTemp
        temperatureRecord.outsideTemp = temperatureReq.outsideTemp
        return temperatureRecord
    }
}