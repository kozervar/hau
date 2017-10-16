package iww.hau.repository

import iww.hau.model.Device
import iww.hau.model.MiningStats
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface RectiveMiningStatsRepository : ReactiveCrudRepository<MiningStats, String> {

    fun findByRigName(rigName:String): Flux<MiningStats>

    fun findByRigNameAndName(rigName:String, name: String): Mono<MiningStats>
}