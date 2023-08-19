package io.github.tiagodocouto.playerservice.app.player.controller

import io.github.tiagodocouto.playerservice.app.player.client.PlayerMapper
import io.github.tiagodocouto.playerservice.app.player.client.PlayerRecord
import io.github.tiagodocouto.playerservice.domain.player.document.Player
import io.github.tiagodocouto.playerservice.domain.player.service.PlayerService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

/**
 * # [PlayerController]
 * the [Player] controller.
 * @property playerService the [PlayerService] instance
 */
@RestController("/api/players")
class PlayerController(
    private val playerService: PlayerService,
    private val playerMapper: PlayerMapper,
) {
    /**
     * [PlayerController.new]
     * Saves a new [Player] and returns it
     *
     * @param record the `Player` request data to be saved
     * @return the saved `Player` data as record
     */
    @PostMapping
    fun new(record: PlayerRecord): PlayerRecord =
        playerService.save(playerMapper.toEntity(record))
            .let { playerMapper.toRecord(it) }
}
