/*
 * Copyright 2023 Roberto Leinardi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.leinardi.forlago.feature.account.interactor.token

import com.leinardi.forlago.feature.account.api.interactor.token.GetJwtExpiresAtInMillisInteractor
import com.leinardi.forlago.feature.account.api.interactor.token.IsJwtExpiredInteractor
import java.time.Clock
import javax.inject.Inject

internal class IsJwtExpiredInteractorImpl @Inject constructor(
    private val clock: Clock,
    private val getJwtExpiresAtInMillisInteractor: GetJwtExpiresAtInMillisInteractor,
) : IsJwtExpiredInteractor {
    override operator fun invoke(jwt: String): Boolean = clock.millis() - getJwtExpiresAtInMillisInteractor(jwt) >= 0
}
