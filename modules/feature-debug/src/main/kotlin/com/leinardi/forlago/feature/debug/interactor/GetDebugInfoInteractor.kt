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

package com.leinardi.forlago.feature.debug.interactor

import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.pm.PackageInfoCompat
import javax.inject.Inject

class GetDebugInfoInteractor @Inject constructor(
    private val context: Application,
) {
    @Suppress("DEPRECATION")
    operator fun invoke(): DebugInfo {
        val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.packageManager.getPackageInfo(context.packageName, PackageManager.PackageInfoFlags.of(0))
        } else {
            context.packageManager.getPackageInfo(context.packageName, 0)
        }
        val appName: String = context.packageManager.run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                return@run getApplicationLabel(
                    getApplicationInfo(context.packageName, PackageManager.ApplicationInfoFlags.of(PackageManager.GET_META_DATA.toLong())),
                ).toString()
            } else {
                return@run getApplicationLabel(getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)).toString()
            }
        }
        val versionName = packageInfo?.versionName ?: "Undefined"
        val versionCode: Long = PackageInfoCompat.getLongVersionCode(packageInfo)
        val displayMetrics = context.resources.displayMetrics

        return DebugInfo(
            DebugInfo.App(
                name = appName,
                versionName = versionName,
                versionCode = versionCode,
                packageName = context.packageName,
            ),
            DebugInfo.Device(
                manufacturer = Build.MANUFACTURER,
                model = Build.MODEL,
                resolutionPx = "${displayMetrics.widthPixels} x ${displayMetrics.heightPixels}",
                resolutionDp = "%.2f x %.2f".format(
                    displayMetrics.widthPixels / displayMetrics.density,
                    displayMetrics.heightPixels / displayMetrics.density,
                ),
                density = displayMetrics.density,
                scaledDensity = displayMetrics.scaledDensity,
                densityDpi = displayMetrics.densityDpi,
                apiLevel = Build.VERSION.SDK_INT,
            ),
        )
    }

    data class DebugInfo(
        val app: App,
        val device: Device,
    ) {
        data class App(
            val name: String,
            val versionName: String,
            val versionCode: Long,
            val packageName: String,
        )

        data class Device(
            val manufacturer: String,
            val model: String,
            val resolutionPx: String,
            val resolutionDp: String,
            val density: Float,
            val scaledDensity: Float,
            val densityDpi: Int,
            val apiLevel: Int,
        )
    }
}
