/*
 * Copyright (c) 2023 Tiago do Couto.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

import com.diffplug.gradle.spotless.SpotlessExtension
import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // Project
    idea
    alias(libs.plugins.kotlinx.kover)
    // Kotlin
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    // Spring
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.management)
    // Quality
    alias(libs.plugins.quality.versions)
    alias(libs.plugins.quality.catalog)
    alias(libs.plugins.quality.detekt)
    alias(libs.plugins.quality.spotless)
    alias(libs.plugins.quality.sonarqube)
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    // Development
    developmentOnly(libs.spring.dev.tools)
    // Annotations Processors
    annotationProcessor(libs.spring.boot.processor)
    // Runtime Dependencies
    runtimeOnly(libs.kotlin.reflect)
    // Spring
    implementation(libs.bundles.spring.boot)
    // Test
    testImplementation(libs.bundles.spring.boot)
    testImplementation(libs.bundles.test.kotest)
    testImplementation(libs.bundles.test.archunit)
    testImplementation(libs.bundles.test.mocks)
    // Code Quality
    detektPlugins(libs.bundles.quality.deteket)
}

kover {
    useJacoco()
    koverReport {
        defaults {
            this.xml { onCheck = true }
            this.html { onCheck = true }
            this.verify { onCheck = true }
        }
    }
}

detekt {
    buildUponDefaultConfig = true
    allRules = true
    config.setFrom("$rootDir/detekt-config.yml")
}

configure<SpotlessExtension> {
    kotlin {
        target("src/main/**/*.kt")
        ktfmt()
        ktlint()
        diktat().configFile("diktat-analysis.yml")
        trimTrailingWhitespace()
        endWithNewline()
    }
    kotlinGradle {
        target("*.gradle.kts")
        ktlint()
        diktat().configFile("diktat-analysis.yml")
    }
    format("misc") {
        target("*.md", "*.yml", "*.properties", ".gitignore")
        trimTrailingWhitespace()
        indentWithSpaces()
        endWithNewline()
    }
}

tasks {
    // Compile
    withType<KotlinCompile> {
        val compilerArgs: String by project
        val sourceCompatibility: String by project
        kotlinOptions {
            freeCompilerArgs += compilerArgs
            jvmTarget = sourceCompatibility
        }
    }
    // Testing
    withType<Test> { useJUnitPlatform() }
    // Code Quality
    withType<Detekt> {
        reports.sarif.required.set(true)
    }
    // Default Tasks
    check {
        dependsOn(
            clean,
            detekt,
            spotlessApply,
            test,
        )
        finalizedBy(
            dependencyUpdates,
        )
    }
}

java { sourceCompatibility = JavaVersion.VERSION_20 }

configurations {
    developmentOnly
    runtimeClasspath { extendsFrom(configurations.developmentOnly.get()) }
    compileOnly { extendsFrom(configurations.annotationProcessor.get()) }
}
