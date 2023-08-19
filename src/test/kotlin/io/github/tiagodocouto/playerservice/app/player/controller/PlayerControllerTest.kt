package io.github.tiagodocouto.playerservice.app.player.controller

import io.github.tiagodocouto.helper.infra.IntegrationTestContext
import io.github.tiagodocouto.helper.spec.IntegrationTestSpec
import io.github.tiagodocouto.playerservice.app.player.client.PlayerRecord
import io.kotest.matchers.shouldBe

@IntegrationTestContext
class PlayerControllerTest(
    private val playerController: PlayerController,
) : IntegrationTestSpec() {
    @Test
    fun `should save a Player`() {
        val record = PlayerRecord(
            externalId = "1",
            name = "Tiago",
        )
        val result = playerController.new(record)

        result.externalId shouldBe record.externalId
        result.name shouldBe record.name
    }
}
