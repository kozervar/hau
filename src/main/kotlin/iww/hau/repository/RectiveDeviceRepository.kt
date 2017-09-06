package iww.hau.repository

import iww.hau.model.Device
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface RectiveDeviceRepository : ReactiveCrudRepository<Device, String> {

    fun findByDeviceName(deviceName: String): Mono<Device>
}