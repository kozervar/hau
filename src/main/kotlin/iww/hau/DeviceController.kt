package iww.hau

import iww.hau.model.DeviceRequest
import iww.hau.service.DeviceService
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/device")
class DeviceController (val deviceService: DeviceService){

    @PostMapping
    fun registerDevice(@ModelAttribute deviceRequest: DeviceRequest): Mono<Boolean> {
        return deviceService.register(deviceRequest.deviceName)
    }
}