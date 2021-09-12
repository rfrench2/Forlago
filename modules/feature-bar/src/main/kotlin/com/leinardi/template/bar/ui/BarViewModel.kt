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

package com.leinardi.template.bar.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.leinardi.template.navigation.TemplateNavigator
import com.leinardi.template.navigation.destination.bar.BarDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BarViewModel @Inject constructor(
    private val templateNavigator: TemplateNavigator,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val viewState = ViewState()

    init {
        viewState.text.value = checkNotNull(savedStateHandle[BarDestination.TEXT_PARAM])
    }

    fun onBackClicked() {
        templateNavigator.navigateUp()
    }

    inner class ViewState {
        val text = mutableStateOf("")
    }

}
