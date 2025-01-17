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

import android.accounts.AbstractAccountAuthenticator
import android.accounts.AccountManager
import com.leinardi.forlago.feature.account.AccountAuthenticatorConfig
import com.leinardi.forlago.feature.account.api.interactor.account.GetAccountInteractor
import com.leinardi.forlago.feature.account.api.interactor.token.InvalidateAccessTokenInteractor
import com.leinardi.forlago.feature.account.api.interactor.token.PeekAccessTokenInteractor
import com.leinardi.forlago.library.android.api.coroutine.CoroutineDispatchers
import com.leinardi.forlago.library.android.api.interactor.encryption.EncryptDeterministicallyInteractor
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

internal class InvalidateAccessTokenInteractorImpl @Inject constructor(
    private val accountManager: AccountManager,
    private val dispatchers: CoroutineDispatchers,
    private val encryptDeterministicallyInteractor: EncryptDeterministicallyInteractor,
    private val getAccountInteractor: GetAccountInteractor,
    private val peekAccessTokenInteractor: PeekAccessTokenInteractor,
) : InvalidateAccessTokenInteractor {
    override suspend operator fun invoke() {
        withContext(dispatchers.io) {
            getAccountInteractor()?.let { account ->
                Timber.d("Invalidate access token")
                val accessToken = peekAccessTokenInteractor()
                if (accessToken != null) {
                    accountManager.invalidateAuthToken(AccountAuthenticatorConfig.ACCOUNT_TYPE, encryptDeterministicallyInteractor(accessToken))
                }
                accountManager.setUserData(account, AbstractAccountAuthenticator.KEY_CUSTOM_TOKEN_EXPIRY, 0.toString())
            }
        }
    }
}
