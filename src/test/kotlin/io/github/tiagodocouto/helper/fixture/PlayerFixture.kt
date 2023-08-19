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

package io.github.tiagodocouto.helper.fixture

import io.github.tiagodocouto.helper.extension.FakerExtensions.faker
import io.github.tiagodocouto.helper.extension.FakerExtensions.name
import io.github.tiagodocouto.helper.extension.FakerExtensions.uuid
import io.github.tiagodocouto.playerservice.app.player.client.PlayerRecord
import io.github.tiagodocouto.playerservice.domain.player.document.Player
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary

object PlayerFixture {
    val Player.Companion.arbitrary: Arb<Player>
        get() = arbitrary {
            Player(
                externalId = faker.uuid(),
                name = faker.name(),
            )
        }
    val PlayerRecord.Companion.arbitrary: Arb<PlayerRecord>
        get() = arbitrary {
            PlayerRecord(
                externalId = faker.uuid(),
                name = faker.name(),
            )
        }
}
