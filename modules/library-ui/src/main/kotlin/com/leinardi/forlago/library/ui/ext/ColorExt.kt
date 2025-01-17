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

package com.leinardi.forlago.library.ui.ext

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils

@Composable
fun Color.getTextColor() = MaterialTheme.colorScheme.contentColorFor(this).takeOrElse {
    val onSurfaceContrast = ColorUtils.calculateContrast(MaterialTheme.colorScheme.onSurface.toArgb(), toArgb())
    val surfaceContrast = ColorUtils.calculateContrast(MaterialTheme.colorScheme.surface.toArgb(), toArgb())
    return@takeOrElse if (onSurfaceContrast > surfaceContrast) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.surface
}
