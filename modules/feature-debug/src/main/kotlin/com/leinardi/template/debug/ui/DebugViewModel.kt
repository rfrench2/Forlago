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

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector
import com.leinardi.template.debug.interactor.GetDebugInfoInteractor
import com.leinardi.template.feature.interactor.GetFeaturesInteractor
import com.leinardi.template.navigation.TemplateNavigator
import com.leinardi.template.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DebugViewModel @Inject constructor(
    private val getDebugInfoInteractor: GetDebugInfoInteractor,
    private val getFeaturesInteractor: GetFeaturesInteractor,
    private val templateNavigator: TemplateNavigator,
) : BaseViewModel<DebugContract.Event, DebugContract.State, DebugContract.Effect>() {

    override fun provideInitialState() = DebugContract.State(
        debugInfo = getDebugInfoInteractor(),
        featureComposableList = getFeaturesInteractor().map { it.debugComposable }
    )

    override fun handleEvent(event: DebugContract.Event) {
        when (event) {
            is DebugContract.Event.OnBackButtonClicked -> templateNavigator.navigateUp()
            DebugContract.Event.OnShowSnackbarButtonClicked -> sendEffect { DebugContract.Effect.ShowSnackbar("Lorem ipsum dolor sit amet...") }
            is DebugContract.Event.OnBottomNavigationItemSelected ->
                updateState { viewState.value.copy(selectedNavigationItem = event.selectedNavigationItem) }
        }
    }

    sealed class BottomNavigationItem(val label: String, val icon: ImageVector) {
        object Info : BottomNavigationItem("Info", Icons.Filled.Info)
        object Features : BottomNavigationItem("Features", Icons.Filled.Favorite)
    }
}
