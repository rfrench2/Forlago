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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun BarScreen() {
    val viewModel = hiltViewModel<BarViewModel>()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Green)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .semantics { contentDescription = "Bar" },
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .background(Color.Yellow)
        ) {
            Text(
                "Bar screen",
                Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = typography.h4,
            )
        }
        Box(
            Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
        ) {
            Text(
                "Text = ${viewModel.viewState.text.value}",
                Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = typography.h4,
            )
        }
        Button(
            onClick = { viewModel.onBackClicked() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}

@Preview
@Composable
fun BarScreenPreview() {
    BarScreen()
}