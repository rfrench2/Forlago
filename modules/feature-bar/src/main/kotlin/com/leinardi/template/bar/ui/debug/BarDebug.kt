/*
 * Copyright 2021 Roberto Leinardi.
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

package com.leinardi.template.bar.ui.debug

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun BarDebug() {
    val viewModel = hiltViewModel<BarDebugViewModel>()

    BarDebug(
        state = viewModel.viewState.value,
        sendEvent = { viewModel.onUiEvent(it) },
    )
}

@Composable
fun BarDebug(
    state: BarDebugContract.State,
    sendEvent: (event: BarDebugContract.Event) -> Unit,
) {
    Column {
        Box(
            Modifier
                .fillMaxWidth()
        ) {
            Text(
                "Hello from the Bar module!",
                Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = typography.h4,
            )
        }
    }
}

@Preview
@Composable
fun BarDebugPreview() {
    BarDebug(BarDebugContract.State) {}
}
