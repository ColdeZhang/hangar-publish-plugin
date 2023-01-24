/*
 * Hangar Publish Plugin Gradle Plugin
 * Copyright (c) 2023 HangarMC Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.papermc.hangarpublishplugin.internal

import java.util.concurrent.TimeUnit

data class HangarAuthorizationToken(val jwt: String, val expiresAt: Long) {
    companion object {
        fun create(jwt: String, expiresIn: Long): HangarAuthorizationToken =
            HangarAuthorizationToken(jwt, System.currentTimeMillis() + expiresIn)
    }

    fun shouldRenew(): Boolean {
        // Request a new one a little before the active one expires
        return System.currentTimeMillis() < expiresAt + TimeUnit.SECONDS.toMillis(3)
    }
}
