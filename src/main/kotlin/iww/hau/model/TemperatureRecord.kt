package iww.hau.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
class TemperatureRecord {

    @Id
    lateinit var id: String

    var deviceName: String = ""

    var light: Float = 0.0F

    var insideTemp: Float = 0.0F

    var outsideTemp: Float = 0.0F

    var ipAddress: String = ""

    var sid: String = ""

    var rssi: Int = 0

    var creationTime: Date = Date()

    override fun toString(): String {
        return "TemperatureRecord(id='$id', deviceName='$deviceName', light=$light, insideTemp=$insideTemp, outsideTemp=$outsideTemp, ipAddress='$ipAddress', sid='$sid', rssi=$rssi, creationTime=$creationTime)"
    }

}