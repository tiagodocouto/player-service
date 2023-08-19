package io.github.tiagodocouto.playerservice.app.player.client

import io.github.tiagodocouto.helper.fixture.PlayerFixture.arbitrary
import io.github.tiagodocouto.helper.spec.TestSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.property.checkAll

class PlayerRecordTest : TestSpec() {
    @Test
    suspend fun `should create a PlayerRecord`() {
        checkAll(PlayerRecord.arbitrary) { player ->
            player.externalId.shouldNotBeNull()
            player.name.shouldNotBeNull()
        }
    }
}
