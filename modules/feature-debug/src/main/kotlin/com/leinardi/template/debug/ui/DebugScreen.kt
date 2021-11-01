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

package com.leinardi.template.debug.ui

import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.leinardi.template.debug.interactor.GetDebugInfoInteractor
import com.leinardi.template.ui.component.SettingsGroup
import com.leinardi.template.ui.component.SettingsMenuLink
import com.leinardi.template.ui.theme.TemplateTypography
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
fun DebugScreen() {
    val viewModel = hiltViewModel<DebugViewModel>()

    DebugScreen(
        state = viewModel.viewState.value,
        sendEvent = { viewModel.onUiEvent(it) },
        effectFlow = viewModel.effect,
    )
}

@Composable
fun DebugScreen(
    state: DebugContract.State,
    effectFlow: Flow<DebugContract.Effect>,
    sendEvent: (event: DebugContract.Event) -> Unit,
) {
    val scaffoldState = rememberScaffoldState()
    LaunchedEffect(effectFlow) {
        effectFlow.onEach { effect ->
            when (effect) {
                is DebugContract.Effect.ShowSnackbar -> scaffoldState.snackbarHostState.showSnackbar(
                    message = effect.message,
                    duration = SnackbarDuration.Short
                )
            }
        }.collect()
    }
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopAppBar(title = { Text("Debug screen") }) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                when (state.selectedNavigationItem) {
                    DebugViewModel.BottomNavigationItem.Info -> Info(state)
                    DebugViewModel.BottomNavigationItem.Features -> Features(state)
                }
            }
        },
        bottomBar = {
            BottomNavigation {
                state.bottomNavigationItems.forEachIndexed() { index, screen ->
                    BottomNavigationItem(
                        icon = { Icon(screen.icon, screen.label) },
                        label = { Text(screen.label) },
                        selected = state.selectedNavigationItem == state.bottomNavigationItems[index],
                        onClick = {
                            sendEvent(DebugContract.Event.OnBottomNavigationItemSelected(state.bottomNavigationItems[index]))
                        }
                    )
                }
            }
        }
    )
}

@Composable
private fun Info(state: DebugContract.State) {
    Column(modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)) {
        Text(
            text = state.debugInfo.app.name,
            color = MaterialTheme.colors.primary,
            style = TemplateTypography.h4
        )
        ProvideTextStyle(value = MaterialTheme.typography.caption) {
            CompositionLocalProvider(
                LocalContentAlpha provides ContentAlpha.medium,
                content = {
                    Text(text = "Version name: ${state.debugInfo.app.versionName}")
                    Text(text = "Version code: ${state.debugInfo.app.versionCode}")
                    Text(text = "Application ID: ${state.debugInfo.app.packageName}")
                }
            )
        }
    }
    SettingsGroup(
        title = { Text(text = "Device info") }
    ) {
        Column {
            SettingsMenuLink(
                title = { Text(text = "Manufacturer") },
                subtitle = { Text(text = state.debugInfo.device.manufacturer) }
            ) {
            }
            SettingsMenuLink(
                title = { Text(text = "Model") },
                subtitle = { Text(text = state.debugInfo.device.model) }
            ) {
            }
            SettingsMenuLink(
                title = { Text(text = "Resolution in Px") },
                subtitle = { Text(text = state.debugInfo.device.resolutionPx) }
            ) {
            }
            SettingsMenuLink(
                title = { Text(text = "Resolution in Dp") },
                subtitle = { Text(text = state.debugInfo.device.resolutionDp) }
            ) {
            }
            SettingsMenuLink(
                title = { Text(text = "Density") },
                subtitle = { Text(text = state.debugInfo.device.density.toString()) }
            ) {
            }
            SettingsMenuLink(
                title = { Text(text = "API level") },
                subtitle = { Text(text = state.debugInfo.device.apiLevel.toString()) }
            ) {
            }
        }
    }
}

@Composable
private fun Features(state: DebugContract.State) {
    state.featureComposableList.forEach { composable -> composable() }
}

@Preview
@Composable
fun DebugScreenPreview() {
    val debugInfo = GetDebugInfoInteractor.DebugInfo(
        GetDebugInfoInteractor.DebugInfo.App(
            name = "App name",
            versionName = "versionName",
            versionCode = 123L,
            packageName = "packageName",
        ),
        GetDebugInfoInteractor.DebugInfo.Device(
            manufacturer = Build.MANUFACTURER,
            model = Build.MODEL,
            resolutionPx = "resolutionPx",
            resolutionDp = "resolutionDp",
            density = 160,
            apiLevel = Build.VERSION.SDK_INT,
        )
    )
    DebugScreen(DebugContract.State(debugInfo, emptyList()), Channel<DebugContract.Effect>().receiveAsFlow()) {}
}
