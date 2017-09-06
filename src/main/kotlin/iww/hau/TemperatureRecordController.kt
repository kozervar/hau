package iww.hau

import iww.hau.model.TemperatureRecord
import iww.hau.model.TemperatureRequest
import mu.KLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@RestController
@RequestMapping("/temperature")
class TemperatureRecordController() {

    companion object: KLogging()

    @Autowired
    lateinit var repository: ReactiveTemperatureRecordRepository

    @PostMapping
    fun createTempRecord(@ModelAttribute temperatureReq: TemperatureRequest, @RequestHeader("User-Agent") userAgent:String) {
        val insideTemperatureRecord = mapToRecord(temperatureReq, userAgent)
        insideTemperatureRecord.temp = temperatureReq.insideTemp
        repository.save(insideTemperatureRecord).subscribe()

        val outsideTemperatureRecord = mapToRecord(temperatureReq, userAgent)
        outsideTemperatureRecord.temp = temperatureReq.outsideTemp

        val date = LocalDate.now().minusDays(14)
        outsideTemperatureRecord.creationTime = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant())

        repository.save(outsideTemperatureRecord).subscribe()
        logger.info { "Two records saved for tempReq = $temperatureReq " }
    }


    @GetMapping()
    fun getAll(@RequestParam(name = "fd", required = false, defaultValue = "-1") fDay:Int,
               @RequestParam(name = "fm", required = false, defaultValue = "-1") fMonth:Int,
               @RequestParam(name = "fy", required = false, defaultValue = "-1") fYear:Int,
               @RequestParam(name = "td", required = false, defaultValue = "-1") tDay:Int,
               @RequestParam(name = "tm", required = false, defaultValue = "-1") tMonth:Int,
               @RequestParam(name = "ty", required = false, defaultValue = "-1") tYear:Int): Flux<TemperatureRecord> {

        val now = LocalDate.now()

        val fromLocalDate = LocalDate.of(
                if(fYear > 0) fYear else now.year,
                if(fMonth > 0) fMonth else now.monthValue,
                if(fDay > 0) fDay else now.dayOfMonth
        )
        val fromDate = Date.from(fromLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant())

        val toLocalDate = LocalDateTime.of(
                if(tYear > 0) tYear else now.year,
                if(tMonth > 0) tMonth else now.monthValue,
                if(tDay > 0) tDay else now.dayOfMonth,
                23,
                59,
                59
        )
        val toDate = Date.from(toLocalDate.atZone(ZoneId.systemDefault()).toInstant())

        return repository.findByCreationTimeBetween(fromDate, toDate)
    }

    private fun mapToRecord(temperatureReq: TemperatureRequest, userAgent: String): TemperatureRecord {
        val temperatureRecord = TemperatureRecord()
        temperatureRecord.ipAddress = temperatureReq.ipAddress
        temperatureRecord.deviceName = userAgent
        temperatureRecord.rssi = temperatureReq.rssi
        return temperatureRecord
    }
}