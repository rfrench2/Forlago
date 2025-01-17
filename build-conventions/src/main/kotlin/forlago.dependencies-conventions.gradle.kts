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
import com.android.build.gradle.internal.tasks.factory.dependsOn
import java.util.Locale

afterEvaluate {
    val outputPath = "$projectDir/versions/dependencies"
    mkdir(outputPath)
    val compileDependencyReportTask = tasks.register("generateRuntimeDependenciesReport") {
        description = "Generates a text file containing the Runtime classpath dependencies."
    }
    project.configurations.filter { it.name.contains("RuntimeClasspath") }.forEach { configuration ->
        val configurationTask = tasks.register(
            "generate${configuration.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }}DependenciesReport",
            DependencyReportTask::class.java,
        ) {
            configurations = setOf(configuration)
            outputFile = File("$outputPath/${configuration.name}Dependencies.txt")
        }
        compileDependencyReportTask.configure { dependsOn(configurationTask) }
    }
    tasks.named("check").dependsOn(tasks.named("generateRuntimeDependenciesReport"))
}
