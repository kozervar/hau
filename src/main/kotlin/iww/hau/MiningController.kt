package iww.hau

import iww.hau.model.MiningStats
import iww.hau.repository.RectiveMiningStatsRepository
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


@RestController
@CrossOrigin(origins = arrayOf("*"))
class MiningController (@Autowired val miningStatsRepository: RectiveMiningStatsRepository) {

    private val log = KotlinLogging.logger {}

    @GetMapping("/mining/stats/{rigName}")
    fun getMiningStats(@PathVariable("rigName") rigName:String): Flux<MiningStats> {
//        miningStatsRepository.deleteAll().subscribe()
        return miningStatsRepository.findByRigName(rigName)
    }

    @GetMapping("/mining/watts/{rigName}")
    fun getMinerWatts(@PathVariable("rigName") rigName:String): Mono<Float> {
//        miningStatsRepository.deleteAll().subscribe()
        return miningStatsRepository.findByRigName(rigName).map { ms -> ms.power }.reduce { total, next -> total + next }
    }

    @PostMapping("/mining/stats/{rigName}")
    fun miningStats(@PathVariable("rigName") rigName:String, @RequestBody miningStatistics: String) {

        var lines = miningStatistics.split("\n")
       
        for (l in lines) {
            val values = l.split("|")
            if(values.size != 7)
                return

            val miningStats = MiningStats()
            miningStats.rigName = rigName
            miningStats.name = values[0]

            val stats = miningStatsRepository.findByRigNameAndName(miningStats.rigName, miningStats.name).block()
            if(stats != null) {
                miningStats.id = stats.id
            }

            miningStats.temp = values[1].toInt()
            miningStats.fan = values[2]
            miningStats.power = values[3].toFloat()
            miningStats.data = miningStatistics

            miningStatsRepository.save(miningStats).subscribe()
        }

        log.info("Mining stats: {}", miningStatistics)
    }
}