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

package io.github.tiagodocouto.playerservice.domain.player

import io.github.tiagodocouto.helper.fixture.PlayerFixture.arbitrary
import io.github.tiagodocouto.helper.infra.IntegrationTestContext
import io.github.tiagodocouto.helper.spec.IntegrationTestSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll

@IntegrationTestContext
class PlayerServiceTest(
    private val playerService: PlayerService,
) : IntegrationTestSpec() {
    @Test
    suspend fun `should save a Player`() {
        checkAll(Player.arbitrary) { player ->
            playerService.save(player) {
                it.id.shouldNotBeNull()
                it.createdAt.shouldNotBeNull()
                it.updatedAt.shouldNotBeNull()
                it.version.shouldNotBeNull()
                it.externalId.shouldNotBeNull()
                it.name.shouldNotBeNull()
            }
        }
    }

    @Test
    suspend fun `should find a Player byExternalId`() {
        checkAll(Player.arbitrary) { player ->
            val saved = playerService.save(player)
            playerService.byExternalId(
                externalId = saved.externalId,
            ) {
                it.id shouldBe saved.id
                it.createdAt.epochSecond shouldBe saved.createdAt.epochSecond
                it.updatedAt.epochSecond shouldBe saved.updatedAt.epochSecond
                it.version shouldBe saved.version
                it.externalId shouldBe saved.externalId
                it.name shouldBe saved.name
            }
        }
    }
}
