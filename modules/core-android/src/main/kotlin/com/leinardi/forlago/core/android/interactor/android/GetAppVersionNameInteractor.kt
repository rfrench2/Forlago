/*
 * Copyright 2022 Roberto Leinardi.
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

package com.leinardi.forlago.core.android.interactor.android

import android.app.Application
import android.content.pm.PackageManager
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAppVersionNameInteractor @Inject constructor(
    private val application: Application,
) {
    operator fun invoke(): String? = try {
        application.packageManager.getPackageInfo(application.packageName, 0).versionName
    } catch (e: PackageManager.NameNotFoundException) {
        Timber.e(e)
        null
    }
}
