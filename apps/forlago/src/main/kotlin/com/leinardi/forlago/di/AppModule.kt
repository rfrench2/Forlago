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

package com.leinardi.forlago.di

import android.app.Application
import com.leinardi.forlago.feature.account.AccountFeature
import com.leinardi.forlago.feature.account.api.interactor.account.RemoveAccountsInteractor
import com.leinardi.forlago.feature.account.api.interactor.token.InvalidateAccessTokenInteractor
import com.leinardi.forlago.feature.account.api.interactor.token.InvalidateRefreshTokenInteractor
import com.leinardi.forlago.library.android.api.interactor.android.DeleteWebViewDataInteractor
import com.leinardi.forlago.library.feature.Feature
import com.leinardi.forlago.library.navigation.api.navigator.ForlagoNavigator
import com.leinardi.forlago.ui.MainActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    @IntoSet
    fun provideAccountFeature(
        application: Application,
        deleteWebViewDataInteractor: DeleteWebViewDataInteractor,
        invalidateAccessTokenInteractor: InvalidateAccessTokenInteractor,
        invalidateRefreshTokenInteractor: InvalidateRefreshTokenInteractor,
        navigator: ForlagoNavigator,
        removeAccountsInteractor: RemoveAccountsInteractor,
    ): Feature = AccountFeature(
        deleteWebViewDataInteractor = deleteWebViewDataInteractor,
        invalidateAccessTokenInteractor = invalidateAccessTokenInteractor,
        invalidateRefreshTokenInteractor = invalidateRefreshTokenInteractor,
        mainActivityIntent = MainActivity.createIntent(application),
        navigator = navigator,
        removeAccountsInteractor = removeAccountsInteractor,
    )
}
