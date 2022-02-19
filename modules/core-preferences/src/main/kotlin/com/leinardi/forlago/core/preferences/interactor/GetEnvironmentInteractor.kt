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

package com.leinardi.forlago.core.preferences.interactor

import androidx.datastore.preferences.core.stringPreferencesKey
import com.leinardi.forlago.core.preferences.repository.AppDataStoreRepository
import javax.inject.Inject

class GetEnvironmentInteractor @Inject constructor(
    private val appDataStoreRepository: AppDataStoreRepository,
) {
    suspend operator fun invoke(): Environment = Environment.valueOf(appDataStoreRepository.readValue(ENVIRONMENT_PREF_KEY, Environment.STAGE.name))

    enum class Environment(val restUrl: String, val graphQlUrl: String) {
        DEV("https://api.dev.example.com", "https://apollo-fullstack-tutorial.herokuapp.com/graphql"),
        MOCK("https://localhost:8000", "https://localhost:8000"),
        PROD("https://api.example.com", "https://apollo-fullstack-tutorial.herokuapp.com/graphql"),
        STAGE("https://api.stage.example.com", "https://apollo-fullstack-tutorial.herokuapp.com/graphql");
    }

    companion object {
        val ENVIRONMENT_PREF_KEY = stringPreferencesKey("environment")
    }
}
