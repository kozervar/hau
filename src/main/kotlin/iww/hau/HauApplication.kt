package iww.hau

import iww.hau.model.TemperatureRecord
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration

@SpringBootApplication
@EnableAutoConfiguration(exclude = arrayOf(MongoAutoConfiguration::class, MongoDataAutoConfiguration::class))
class HauApplication: CommandLineRunner {

    @Autowired
    lateinit var repo: ReactiveTemperatureRecordRepository;

    override fun run(vararg args: String?) {
//        save("test1")
//        save("test2")
//        save("test3")
//        save("test4")
    }

    fun save(name:String) {
        val temperatureRecord1 = TemperatureRecord()
        temperatureRecord1.deviceName = name
        repo.save(temperatureRecord1).subscribe()
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(HauApplication::class.java, *args)
}