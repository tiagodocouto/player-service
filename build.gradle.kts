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

import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // Project
    idea
    alias(libs.plugins.kotlinx.kover)
    // Kotlin
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.spring)
    // Spring
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.management)
    // Testing
    alias(libs.plugins.tests.allure)
    alias(libs.plugins.tests.pitest)
    alias(libs.plugins.tests.pitest.github)
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
    // Mappers
    implementation(libs.mapstruct)
    kapt(libs.mapstruct.processor)
    // Test
    testImplementation(libs.bundles.test.spring.boot)
    testImplementation(libs.bundles.test.archunit)
    testImplementation(libs.bundles.test.kotest)
    testImplementation(libs.bundles.test.mocks)
    testImplementation(libs.bundles.test.testcontainers)
    // Code Quality
    detektPlugins(libs.bundles.quality.deteket)
    pitest(libs.bundles.test.pitest)
}

kover {
    koverReport {
        defaults {
            this.xml { onCheck = true }
            this.html { onCheck = true }
            this.verify { onCheck = true }
        }
    }
}

detekt {
    val dependencyConfigurationDetekt: String by project
    config.setFrom(dependencyConfigurationDetekt)
    buildUponDefaultConfig = true
    allRules = true
}

java { sourceCompatibility = JavaVersion.VERSION_20 }

spotless {
    val dependencyConfigurationDiktat: String by project
    kotlin {
        target("src/main/**/*.kt")
        ktfmt()
        ktlint()
        diktat().configFile(dependencyConfigurationDiktat)
        trimTrailingWhitespace()
        endWithNewline()
    }
    kotlinGradle {
        target("*.gradle.kts")
        ktlint()
        diktat().configFile(dependencyConfigurationDiktat)
    }
    format("misc") {
        target("*.md", "*.yml", "*.properties", ".gitignore")
        trimTrailingWhitespace()
        indentWithSpaces()
        endWithNewline()
    }
}

allure {
    val dependencyVersionAllure: String by project
    version = dependencyVersionAllure
}

pitest {
    val dependencyVersionPitest: String by project
    val dependencyVersionPitestJunit5: String by project
    pitestVersion = dependencyVersionPitest
    junit5PluginVersion = dependencyVersionPitestJunit5
    threads = Runtime.getRuntime().availableProcessors()
    targetClasses = listOf("$group.*")
    outputFormats = listOf("XML", "HTML", "gitci")
    mutators = listOf("STRONGER", "EXTENDED", "SPRING")
    features = listOf("+gitci", "+KOTLIN")
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
        reports.sarif.required = true
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
            allureReport,
            dependencyUpdates,
        )
    }
}

configurations {
    developmentOnly
    runtimeClasspath { extendsFrom(configurations.developmentOnly.get()) }
    compileOnly { extendsFrom(configurations.annotationProcessor.get()) }
    // Detekt Configuration
    val dependencyNameDetekt: String by project
    val dependencyPackageKotlin: String by project
    val dependencyVersionDetektKotlin: String by project
    matching { it.name == dependencyNameDetekt }.all {
        resolutionStrategy.eachDependency {
            if (requested.group == dependencyPackageKotlin) {
                useVersion(dependencyVersionDetektKotlin)
            }
        }
    }
}
