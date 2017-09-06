package iww.hau.hau.model

import org.springframework.data.annotation.Id

class TemperatureRecord {

    @Id
    lateinit var id:String

    lateinit var name:String
}