package iww.hau.model

class TemperatureRequest {

    var deviceName:String = ""

    var ipAddress:String = ""

    var sid:String = ""

    var rssi:Int = 0

    var insideTemp:Float = 0.0F

    var outsideTemp:Float = 0.0F

    override fun toString(): String {
        return "TemperatureRequest(deviceName='$deviceName', ipAddress='$ipAddress', sid='$sid', rssi=$rssi, insideTemp='$insideTemp', outsideTemp='$outsideTemp')"
    }

}
