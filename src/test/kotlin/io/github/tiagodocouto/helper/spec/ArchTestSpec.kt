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

package io.github.tiagodocouto.helper.spec

import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.Priority.HIGH
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.priority
import com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS
import com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION
import com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING
import com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JODATIME
import com.tngtech.archunit.library.GeneralCodingRules.testClassesShouldResideInTheSamePackageAsImplementation
import io.github.tiagodocouto.helper.rule.ArchConditionShouldHaveTestForClass
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

private const val SUFFIX_EXCEPTION = "Exception"
private const val PACKAGE_BASE = "io.github.tiagodocouto.playerservice"
private const val PACKAGE_BASE_APP = "$PACKAGE_BASE.app.."
private const val PACKAGE_BASE_DOMAIN = "$PACKAGE_BASE.domain.."
private const val PACKAGE_BASE_INFRA = "$PACKAGE_BASE.infra.."

/**
 * A [ArchTestSpec] extends [TestSpec] with all default Arch Test
 */
abstract class ArchTestSpec : TestSpec() {
    /**
     * Certify that no Classes throws any kind of generic [Exception]
     */
    @ArchTest
    val noClassesShouldThrowGenericExceptions: ArchRule =
        NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS

    /**
     * Certify that no Classes uses generic java logging
     */
    @ArchTest
    val noClassesShouldUseJavaUtilLogging: ArchRule =
        NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING

    /**
     * Certify that no Classes use org.joda.time
     */
    @ArchTest
    val noClassesShouldUseJodaTime: ArchRule =
        NO_CLASSES_SHOULD_USE_JODATIME

    /**
     * Certify that no Classes use field injection
     * it should use constructor injection
     */
    @ArchTest
    val noClassesShouldUseFieldInjectionAnnotation: ArchRule =
        NO_CLASSES_SHOULD_USE_FIELD_INJECTION

    /**
     * Certify that all Classes have a Test
     */
    @ArchTest
    val allClassShouldHaveTest: ArchRule =
        priority(HIGH)
            .classes().should(ArchConditionShouldHaveTestForClass)
            .`as`("All classes should have test")

    /**
     * Certify that all tests are in the same package as their classes
     */
    @ArchTest
    val testClassesShouldResideInTheSamePackageAsImplementation: ArchRule =
        testClassesShouldResideInTheSamePackageAsImplementation()

    /**
     * Certify that all Classes inside the INFRA layer
     * won't access any APP layer classes
     */
    @ArchTest
    val noInfraInsideAppLayer: ArchRule =
        priority(HIGH)
            .noClasses().that().resideInAPackage(PACKAGE_BASE_INFRA)
            .should().dependOnClassesThat().resideInAnyPackage(PACKAGE_BASE_APP)

    /**
     * Certify that all Classes inside the APP layer
     * won't access any INFRA layer classes
     */
    @ArchTest
    val noAppInsideInfraLayer: ArchRule =
        priority(HIGH)
            .noClasses().that().resideInAPackage(PACKAGE_BASE_APP)
            .should().dependOnClassesThat().resideInAnyPackage(PACKAGE_BASE_INFRA)

    /**
     * Certify that all Classes inside the DOMAIN layer
     * won't access any APP layer classes
     */
    @ArchTest
    val noAppInsideDomainLayer: ArchRule =
        priority(HIGH)
            .noClasses().that().resideInAPackage(PACKAGE_BASE_DOMAIN)
            .should().dependOnClassesThat().resideInAnyPackage(PACKAGE_BASE_APP)

    /**
     * Certify that annotated DOCUMENTS are inside DOMAIN layer only
     */
    @ArchTest
    val documentsShouldResideInsideDomainDocument: ArchRule =
        priority(HIGH)
            .classes().that().areAnnotatedWith(Document::class.java)
            .should().resideInAPackage(PACKAGE_BASE_DOMAIN)

    /**
     * Certify that REPOSITORIES are annotated and inside the INFRA layer only
     */
    @ArchTest
    val repositoriesShouldBeInInfraLayer: ArchRule =
        priority(HIGH)
            .classes().that().areAnnotatedWith(Repository::class.java)
            .should().beInterfaces()
            .andShould().resideInAPackage(PACKAGE_BASE_INFRA)

    /**
     * Certify that SERVICES are annotated and inside DOMAIN layer
     */
    @ArchTest
    val servicesShouldBeInDomainLayer: ArchRule =
        priority(HIGH)
            .classes().that().areAnnotatedWith(Service::class.java)
            .should().notBeInnerClasses()
            .andShould().resideInAPackage(PACKAGE_BASE_DOMAIN)

    /**
     * Certify that SERVICES are annotated and inside DOMAIN layer
     */
    @ArchTest
    val controllersShouldBeInAppLayer: ArchRule =
        priority(HIGH)
            .classes().that().areAnnotatedWith(Controller::class.java)
            .should().notBeInnerClasses()
            .andShould().resideInAPackage(PACKAGE_BASE_APP)

    /**
     * Certify that EXCEPTIONS extend Extension
     */
    @ArchTest
    val exceptionsShouldHaveExceptionAsSuffix: ArchRule =
        priority(HIGH)
            .classes().that().areAssignableTo(Exception::class.java)
            .should().haveSimpleNameEndingWith(SUFFIX_EXCEPTION)
}
