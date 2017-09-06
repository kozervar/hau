package iww.hau.hau

import iww.hau.hau.model.TemperatureRecord
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import javax.annotation.PostConstruct

@RestController
class TemperatureRecordController() {

    @Autowired
    lateinit var repository: ReactiveTemperatureRecordRepository

    @PostConstruct
    fun init() {

    }

    @GetMapping("/temp")
    fun getAll(): Flux<TemperatureRecord> {
        repository.findAll().map { e -> println(e) }
        return repository.findAll()
    }

    @GetMapping("/new")
    fun saveNew()  {
        val temperatureRecord = TemperatureRecord()
        temperatureRecord.name = "test"
        repository.save(temperatureRecord).subscribe()
    }
}