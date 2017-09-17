package iww.hau

import iww.hau.model.TemperatureRecord
import iww.hau.model.TemperatureRequest
import iww.hau.repository.ReactiveTemperatureRecordRepository
import mu.KLogging
import org.apache.commons.math3.stat.descriptive.moment.Mean
import org.apache.commons.math3.stat.descriptive.rank.Median
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoField
import java.util.*
import java.util.function.Function
import kotlin.streams.toList

data class RecordDTTuple(var dateTime: LocalDateTime, var value: Float) {
    override fun toString(): String {
        return "RecordDTTuple(dateTime=$dateTime, value=$value)"
    }
}

enum class Interval {
    YEAR,
    MONTH,
    DAY,
    HOUR,
    MINUTE
}

@RestController
@CrossOrigin(origins = arrayOf("*"))
class StatisticsController(val repository: ReactiveTemperatureRecordRepository) {

    companion object : KLogging()

    @PostMapping("/temperature")
    fun createTempRecord(@ModelAttribute temperatureReq: TemperatureRequest) {
        val temperatureRecord = mapToRecord(temperatureReq)
        repository.save(temperatureRecord).subscribe()

        logger.debug { "Record saved for tempReq = $temperatureReq " }
    }


    @GetMapping("/temperature")
    fun getAll(@RequestParam(name = "fd", required = false, defaultValue = "-1") fDay: Int,
               @RequestParam(name = "fm", required = false, defaultValue = "-1") fMonth: Int,
               @RequestParam(name = "fy", required = false, defaultValue = "-1") fYear: Int,
               @RequestParam(name = "fh", required = false, defaultValue = "-1") fHour: Int,
               @RequestParam(name = "fmin", required = false, defaultValue = "-1") fMinute: Int,
               @RequestParam(name = "td", required = false, defaultValue = "-1") tDay: Int,
               @RequestParam(name = "tm", required = false, defaultValue = "-1") tMonth: Int,
               @RequestParam(name = "ty", required = false, defaultValue = "-1") tYear: Int,
               @RequestParam(name = "th", required = false, defaultValue = "-1") tHour: Int,
               @RequestParam(name = "tmin", required = false, defaultValue = "-1") tMinute: Int
    ): Flux<Any>? {

        return getTemperature(fDay, fMonth, fYear, fHour, fMinute, tDay, tMonth, tYear, tHour, tMinute,
                Function { tr -> tr })
    }

    @GetMapping("/temperature/inside")
    fun getInsideTemperatureAll(@RequestParam(name = "fd", required = false, defaultValue = "-1") fDay: Int,
                                @RequestParam(name = "fm", required = false, defaultValue = "-1") fMonth: Int,
                                @RequestParam(name = "fy", required = false, defaultValue = "-1") fYear: Int,
                                @RequestParam(name = "fh", required = false, defaultValue = "-1") fHour: Int,
                                @RequestParam(name = "fmin", required = false, defaultValue = "-1") fMinute: Int,
                                @RequestParam(name = "td", required = false, defaultValue = "-1") tDay: Int,
                                @RequestParam(name = "tm", required = false, defaultValue = "-1") tMonth: Int,
                                @RequestParam(name = "ty", required = false, defaultValue = "-1") tYear: Int,
                                @RequestParam(name = "th", required = false, defaultValue = "-1") tHour: Int,
                                @RequestParam(name = "tmin", required = false, defaultValue = "-1") tMinute: Int,
                                @RequestParam(name = "interval", required = false, defaultValue = "MINUTE") interval: Interval
    ): List<RecordDTTuple> {
        return getTemperatureInterval(fDay, fMonth, fYear, fHour, fMinute, tDay, tMonth, tYear, tHour, tMinute,
                Function { tr -> tr.insideTemp }, interval)
    }

    @GetMapping("/temperature/outside")
    fun getOutsideTemperatureAll(@RequestParam(name = "fd", required = false, defaultValue = "-1") fDay: Int,
                                 @RequestParam(name = "fm", required = false, defaultValue = "-1") fMonth: Int,
                                 @RequestParam(name = "fy", required = false, defaultValue = "-1") fYear: Int,
                                 @RequestParam(name = "fh", required = false, defaultValue = "-1") fHour: Int,
                                 @RequestParam(name = "fmin", required = false, defaultValue = "-1") fMinute: Int,
                                 @RequestParam(name = "td", required = false, defaultValue = "-1") tDay: Int,
                                 @RequestParam(name = "tm", required = false, defaultValue = "-1") tMonth: Int,
                                 @RequestParam(name = "ty", required = false, defaultValue = "-1") tYear: Int,
                                 @RequestParam(name = "th", required = false, defaultValue = "-1") tHour: Int,
                                 @RequestParam(name = "tmin", required = false, defaultValue = "-1") tMinute: Int,
                                 @RequestParam(name = "interval", required = false, defaultValue = "MINUTE") interval: Interval
    ): List<RecordDTTuple> {
        return getTemperatureInterval(fDay, fMonth, fYear, fHour, fMinute, tDay, tMonth, tYear, tHour, tMinute,
                Function { tr -> tr.outsideTemp }, interval)
    }

    fun getTemperature(fDay: Int,
                       fMonth: Int,
                       fYear: Int,
                       fHour: Int,
                       fMinute: Int,
                       tDay: Int,
                       tMonth: Int,
                       tYear: Int,
                       tHour: Int,
                       tMinute: Int,
                       mapFunction: Function<in TemperatureRecord, out Any>): Flux<Any>? {
        val now = LocalDateTime.now()

        val fromLocalDateTime = LocalDateTime.of(
                if (fYear > 0) fYear else now.year,
                if (fMonth > 0) fMonth else now.monthValue,
                if (fDay > 0) fDay else now.dayOfMonth,
                if (fHour > -1) fHour else now.hour,
                if (fMinute > -1) fMinute else now.minute,
                0
        )
        val fromDate = Date.from(fromLocalDateTime.atZone(ZoneId.systemDefault()).toInstant())

        val toLocalDate = LocalDateTime.of(
                if (tYear > 0) tYear else now.year,
                if (tMonth > 0) tMonth else now.monthValue,
                if (tDay > 0) tDay else now.dayOfMonth,
                if (tHour > -1) tHour else 23,
                if (tMinute > -1) tMinute else 59,
                59
        )
        val toDate = Date.from(toLocalDate.atZone(ZoneId.systemDefault()).toInstant())

        return repository.findByCreationTimeBetweenOrderByCreationTimeAsc(fromDate, toDate).map(mapFunction)
    }

    fun getTemperatureInterval(fDay: Int,
                               fMonth: Int,
                               fYear: Int,
                               fHour: Int,
                               fMinute: Int,
                               tDay: Int,
                               tMonth: Int,
                               tYear: Int,
                               tHour: Int,
                               tMinute: Int,
                               mapFunction: Function<in TemperatureRecord, out Float>,
                               interval: Interval): List<RecordDTTuple> {
        val now = LocalDateTime.now()

        val fromLocalDateTime = LocalDateTime.of(
                if (fYear > 0) fYear else now.year,
                if (fMonth > 0) fMonth else now.monthValue,
                if (fDay > 0) fDay else now.dayOfMonth,
                if (fHour > -1) fHour else now.hour,
                if (fMinute > -1) fMinute else now.minute,
                0
        )
        val fromDate = Date.from(fromLocalDateTime.atZone(ZoneId.systemDefault()).toInstant())

        val toLocalDate = LocalDateTime.of(
                if (tYear > 0) tYear else now.year,
                if (tMonth > 0) tMonth else now.monthValue,
                if (tDay > 0) tDay else now.dayOfMonth,
                if (tHour > -1) tHour else 23,
                if (tMinute > -1) tMinute else 59,
                59
        )
        val toDate = Date.from(toLocalDate.atZone(ZoneId.systemDefault()).toInstant())

        val listOfDTTouples = repository
                .findByCreationTimeBetweenOrderByCreationTimeAsc(fromDate, toDate)
                .map { tr -> RecordDTTuple(toLocalDateTime(tr.creationTime), mapFunction.apply(tr)) }
                .toStream().toList()

        return listOfDTTouples
                .groupBy { recordDTTuple: RecordDTTuple -> groupBy(recordDTTuple, interval) }
                .map { entry: Map.Entry<String, List<RecordDTTuple>> -> mapToRecordTuple(entry, interval) }
    }

    private fun groupBy(recordDTTuple: RecordDTTuple, interval: Interval): String {
        val dt = recordDTTuple.dateTime
        var result = ""
        when (interval) {
            Interval.YEAR -> result = result + dt.year + ":" + dt.get(ChronoField.YEAR)
            Interval.MONTH -> result = result + dt.monthValue + "." + dt.year + ":" + dt.get(ChronoField.MONTH_OF_YEAR)
            Interval.DAY -> result = result + dt.dayOfMonth + "." + dt.monthValue + "." + dt.year + ":" + dt.get(ChronoField.DAY_OF_MONTH)
            Interval.HOUR -> result = result + dt.dayOfMonth + "." + dt.monthValue + "." + dt.year + ":" + dt.get(ChronoField.HOUR_OF_DAY)
            Interval.MINUTE -> result = result + dt.dayOfMonth + "." + dt.monthValue + "." + dt.year + ":" + dt.get(ChronoField.MINUTE_OF_DAY)
        }
        return result
    }

    private fun mapToRecordTuple(entry: Map.Entry<String, List<RecordDTTuple>>, interval: Interval): RecordDTTuple {

        var dateTime = entry.value.first().dateTime

        when (interval) {
            Interval.YEAR -> dateTime = dateTime.withMonth(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0)
            Interval.MONTH -> dateTime = dateTime.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0)
            Interval.DAY -> dateTime = dateTime.withHour(0).withMinute(0).withSecond(0).withNano(0)
            Interval.HOUR -> dateTime = dateTime.withMinute(0).withSecond(0).withNano(0)
            Interval.MINUTE -> dateTime = dateTime.withSecond(0).withNano(0)
        }

        val median = getAvarageFromDoubleArray(entry.value.map { e -> e.value.toDouble() }.toDoubleArray())
        return RecordDTTuple(dateTime, median.toFloat())
    }

    @GetMapping("/temperature/current/inside")
    fun getCurrentInsideTemperature(): Double {

        var from = LocalDateTime.now()
        from = from.minusMinutes(1)

        val to = LocalDateTime.now()

        return getMedianFromTo(from, to, { temperatureRecord -> temperatureRecord.insideTemp })
    }

    @GetMapping("/temperature/current/outside")
    fun getCurrentOutsideTemperature(): Double {

        var from = LocalDateTime.now()
        from = from.minusMinutes(1)

        val to = LocalDateTime.now()

        return getMedianFromTo(from, to, { temperatureRecord -> temperatureRecord.outsideTemp })
    }

    @GetMapping("/light/current")
    fun getCurrentLight(): Double {

        var from = LocalDateTime.now()
        from = from.minusMinutes(1)

        val to = LocalDateTime.now()

        return getMedianFromTo(from, to, { temperatureRecord -> temperatureRecord.light })
    }

    @GetMapping("/pressure/current")
    fun getCurrentPressure(): Double {

        var from = LocalDateTime.now()
        from = from.minusMinutes(1)

        val to = LocalDateTime.now()

        return getMedianFromTo(from, to, { temperatureRecord -> temperatureRecord.pressure })
    }

    @GetMapping("/humidity/current")
    fun getCurrentHumidity(): Double {

        var from = LocalDateTime.now()
        from = from.minusMinutes(1)

        val to = LocalDateTime.now()

        return getMedianFromTo(from, to, { temperatureRecord -> temperatureRecord.humidity })
    }

    private fun mapToRecord(temperatureReq: TemperatureRequest): TemperatureRecord {
        val temperatureRecord = TemperatureRecord()
        temperatureRecord.ipAddress = temperatureReq.ipAddress
        temperatureRecord.deviceName = temperatureReq.deviceName
        temperatureRecord.rssi = temperatureReq.rssi
        temperatureRecord.light = temperatureReq.light
        temperatureRecord.insideTemp = temperatureReq.insideTemp
        temperatureRecord.outsideTemp = temperatureReq.outsideTemp
        temperatureRecord.pressure = temperatureReq.pressure
        temperatureRecord.humidity = temperatureReq.humidity
        return temperatureRecord
    }

    private fun toDate(localDateTime: LocalDateTime): Date {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())
    }

    private fun toLocalDateTime(date: Date): LocalDateTime {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())
    }

    private fun getMedianFromTo(from: LocalDateTime, to: LocalDateTime, mapFunction: (TemperatureRecord) -> Float): Double {
        val insideTemps = repository
                .findByCreationTimeBetweenOrderByCreationTimeAsc(toDate(from), toDate(to))
                .map(mapFunction)
                .collectList()
                .block()
                ?.map { fl: Float -> fl.toDouble() }
                ?.toDoubleArray()

        return getMedianFromDoubleArray(insideTemps)
    }

    private fun getMedianFromDoubleArray(arr: DoubleArray?): Double {
        val median = Median()
        Mean()
        val value = median.evaluate(arr)
        return value
    }

    private fun getAvarageFromDoubleArray(arr: DoubleArray?): Double {
        val mean = Mean()
        val value = mean.evaluate(arr)
        return value
    }
}