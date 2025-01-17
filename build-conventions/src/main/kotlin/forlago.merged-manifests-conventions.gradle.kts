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
tasks {
    register<Copy>("copyMergedManifests") {
        dependsOn(tasks.matching { it.name matches "^process.*Manifest$".toRegex() })
        mustRunAfter(tasks.matching { it.name matches "^process.*Manifest$".toRegex() })
        mkdir("versions/mergedManifests")
        from("$buildDir/intermediates/merged_manifests") {
            include("**/*.xml")
        }
        into("versions/mergedManifests")
        filter { line -> line.replace("(android:version.*=\".*\")|(android:testOnly=\".*\")".toRegex(), "") }
    }

    named("check") {
        dependsOn(tasks.named("copyMergedManifests"))
    }
}
