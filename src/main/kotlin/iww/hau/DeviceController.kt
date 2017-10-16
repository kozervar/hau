package iww.hau

import iww.hau.model.Device
import iww.hau.model.DeviceRequest
import iww.hau.repository.RectiveDeviceRepository
import iww.hau.service.DeviceService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/device")
@CrossOrigin(origins = arrayOf("*"))
class DeviceController (val deviceService: DeviceService, val repository: RectiveDeviceRepository){

    @PostMapping
    fun registerDevice(@ModelAttribute deviceRequest: DeviceRequest): Mono<Boolean> {
        return deviceService.register(deviceRequest.deviceName)
    }

    @GetMapping
    fun getAllDevices(): Flux<Device>? {
        return repository.findAll()
    }
}