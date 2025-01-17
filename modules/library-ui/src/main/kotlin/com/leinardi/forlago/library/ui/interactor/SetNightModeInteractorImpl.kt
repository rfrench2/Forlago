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

package com.leinardi.forlago.library.ui.interactor

import android.app.Application
import android.app.UiModeManager
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.leinardi.forlago.library.ui.api.NightMode
import com.leinardi.forlago.library.ui.api.interactor.SetNightModeInteractor
import timber.log.Timber
import javax.inject.Inject

internal class SetNightModeInteractorImpl @Inject constructor(
    private val application: Application,
) : SetNightModeInteractor {
    override operator fun invoke(mode: NightMode) {
        Timber.d("Set night mode to $mode")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val uiModeManager = ContextCompat.getSystemService(application, UiModeManager::class.java)
            uiModeManager?.setApplicationNightMode(mode.uiModeManagerValue)
        } else {
            AppCompatDelegate.setDefaultNightMode(mode.appCompatDelegateValue)
        }
    }
}
