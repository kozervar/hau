package iww.hau.hau

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class HauApplication

fun main(args: Array<String>) {
    SpringApplication.run(HauApplication::class.java, *args)
}
