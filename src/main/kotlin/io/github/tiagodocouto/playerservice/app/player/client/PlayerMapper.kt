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

package io.github.tiagodocouto.playerservice.app.player.client

import io.github.tiagodocouto.playerservice.app.mapper.annotation.IgnoreBaseFields
import io.github.tiagodocouto.playerservice.domain.player.document.Player
import org.mapstruct.InheritInverseConfiguration
import org.mapstruct.Mapper

/**
 * # [PlayerMapper]
 * maps a [Player] to a [PlayerRecord] and vice-versa.
 */
@Mapper(componentModel = "spring")
interface PlayerMapper {
    /**
     * [PlayerMapper.toRecord]
     * maps a [Player] to a [PlayerRecord].
     *
     * @param entity the [Player] to be mapped
     * @return the mapped [PlayerRecord]
     */
    fun toRecord(entity: Player): PlayerRecord

    /**
     * [PlayerMapper.toEntity]
     * maps a [PlayerRecord] to a [Player].
     *
     * @param dto the [PlayerRecord] to be mapped
     * @return the mapped [Player]
     */
    @InheritInverseConfiguration
    @IgnoreBaseFields
    fun toEntity(dto: PlayerRecord): Player
}
