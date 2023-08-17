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

package io.github.tiagodocouto.playerservice.domain.player.service

import io.github.tiagodocouto.playerservice.domain.player.document.Player
import io.github.tiagodocouto.playerservice.domain.player.exception.PlayerNotFoundException
import io.github.tiagodocouto.playerservice.infra.player.PlayerRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * # [PlayerService]
 * the [Player] service.
 * @property playerRepository the [PlayerRepository] instance
 *
 * ```kotlin
 * playerService.save( Player() ) { ... }
 * ```
 */
@Service
@Transactional
class PlayerService(
    private val playerRepository: PlayerRepository,
) {
    /**
     * [PlayerService.save]
     * Saves the [Player] and executes [block] with the saved [Player]
     *
     * @param player the [Player] to be saved
     * @param block the function to be executed with the saved [Player]
     * @return the saved [Player]
     */
    fun save(
        player: Player,
        block: ((Player) -> Unit)? = null,
    ): Player = playerRepository.save(player)
        .also { saved -> block?.invoke(saved) }

    /**
     * [PlayerService.byExternalId]
     * Finds a [Player] by its [externalId] and executes [block] with the found [Player]
     * or throws a [PlayerNotFoundException] if the [Player] was not found
     *
     * @param externalId the [Player] external id
     * @param block the function to be executed with the found [Player]
     * @return the found [Player]
     * @throws PlayerNotFoundException if the [Player] was not found
     */
    @Throws(PlayerNotFoundException::class)
    fun byExternalId(
        externalId: String,
        block: ((Player) -> Unit)? = null,
    ): Player = playerRepository.findByExternalId(externalId)
        ?.also { found -> block?.invoke(found) }
        ?: throw PlayerNotFoundException(externalId)
}
