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

package com.leinardi.forlago.feature.account.interactor.account

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.leinardi.forlago.feature.account.api.interactor.account.SignInInteractor
import com.leinardi.forlago.feature.account.api.interactor.account.SignInInteractor.OkResult
import com.leinardi.forlago.feature.account.api.model.AuthErrResult
import com.leinardi.forlago.library.feature.interactor.GetFeaturesInteractor
import kotlinx.coroutines.delay
import java.net.HttpURLConnection
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class SignInInteractorImpl @Inject constructor(
    private val getFeaturesInteractor: GetFeaturesInteractor,
) : SignInInteractor {
    @Suppress("TooGenericExceptionCaught", "MagicNumber")
    override suspend operator fun invoke(username: String, password: String, success: Boolean): Result<OkResult, AuthErrResult> {
        // This simulates fetching a new refresh token. The access token won't be valid 20% of the time.
        delay(TimeUnit.SECONDS.toMillis(2))
        return if (success) {
            getFeaturesInteractor().forEach { it.featureLifecycle.onSignIn() }
            Ok(
                OkResult(
                    accessToken = UUID.randomUUID().toString(),
                    refreshToken = UUID.randomUUID().toString(),
                    username = username,
                ),
            )
        } else {
            Err(AuthErrResult.BadAuthentication(HttpURLConnection.HTTP_FORBIDDEN))
        }
    }
}
