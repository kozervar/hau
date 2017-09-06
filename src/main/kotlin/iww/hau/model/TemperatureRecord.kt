package iww.hau.model

import org.springframework.data.annotation.Id
import java.util.*

class TemperatureRecord {

    @Id
    lateinit var id: String

    var deviceName: String = ""

    var temp: Float = 0.0F

    var ipAddress: String = ""

    var sid: String = ""

    var rssi: Int = 0

    var creationTime: Date = Date()

    override fun toString(): String {
        return "TemperatureRecord(id='$id', deviceName='$deviceName', temp=$temp, ipAddress='$ipAddress', sid='$sid', rssi=$rssi, creationTime=$creationTime)"
    }

}