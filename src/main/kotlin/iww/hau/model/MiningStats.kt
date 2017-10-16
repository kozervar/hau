package iww.hau.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*


@Document
class MiningStats {

    @Id
    lateinit var id: String

    val updateDate:Date = Date()

    var rigName:String = ""

    var name:String = ""

    var temp:Int = 0

    var power:Float = 0.0F

    var fan:String = "0%"

    var data:String = ""
}