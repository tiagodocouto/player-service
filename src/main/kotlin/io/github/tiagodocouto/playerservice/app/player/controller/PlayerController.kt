/*
 * Copyright (c) 2023 Tiago do Couto.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
