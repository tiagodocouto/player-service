package io.github.tiagodocouto.playerservice.app.player.client

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
    fun toEntity(dto: PlayerRecord): Player
}
