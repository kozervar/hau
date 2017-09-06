package iww.hau.model

import org.springframework.data.annotation.Id
import java.util.*


class Device {

    @Id
    lateinit var id: String

    var deviceName: String = ""

    var allowed: Boolean = true

}