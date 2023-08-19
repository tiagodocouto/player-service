package io.github.tiagodocouto.playerservice.app.player.client

/**
 * # [PlayerRecord]
 * @property externalId the player external id
 * @property name the player name
 */
data class PlayerRecord(
    val externalId: String,
    val name: String,
) {
    companion object
}
