package iww.hau.service

import iww.hau.model.Device
import iww.hau.repository.RectiveDeviceRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono


@Service
class DeviceService(val repositoryRective: RectiveDeviceRepository) {

    fun register(name: String): Mono<Boolean> {
        return Mono.create<Boolean> { sink ->
            var device = repositoryRective.findByDeviceName(name).block()
            if (device == null) {
                device = Device()
                device.deviceName = name
                repositoryRective.save(device).subscribe()
            }
            sink.success(device.allowed)
        }
    }

}