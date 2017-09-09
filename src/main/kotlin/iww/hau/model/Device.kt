package iww.hau.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
class Device {

    @Id
    lateinit var id: String

    var deviceName: String = ""

    var allowed: Boolean = true

}