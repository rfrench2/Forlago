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

import androidx.compose.runtime.Composable
import com.leinardi.template.debug.interactor.GetDebugInfoInteractor
import com.leinardi.template.ui.base.ViewEffect
import com.leinardi.template.ui.base.ViewEvent
import com.leinardi.template.ui.base.ViewState

class DebugContract {
    data class State(
        val debugInfo: GetDebugInfoInteractor.DebugInfo,
        val featureComposableList: List<@Composable () -> Unit>,
        val bottomNavigationItems: List<DebugViewModel.BottomNavigationItem> = listOf(
            DebugViewModel.BottomNavigationItem.Info,
            DebugViewModel.BottomNavigationItem.Features
        ),
        val selectedNavigationItem: DebugViewModel.BottomNavigationItem = bottomNavigationItems[0]
    ) : ViewState

    sealed class Event : ViewEvent {
        object OnBackButtonClicked : Event() // Remove if not used
        data class OnBottomNavigationItemSelected(val selectedNavigationItem: DebugViewModel.BottomNavigationItem) : Event()
        object OnShowSnackbarButtonClicked : Event() // Remove if not used
    }

    sealed class Effect : ViewEffect {
        data class ShowSnackbar(val message: String) : Effect() // Remove if not used
    }
}
