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

package io.github.tiagodocouto.helper.rule

import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.lang.ArchCondition
import com.tngtech.archunit.lang.ConditionEvents
import com.tngtech.archunit.lang.SimpleConditionEvent.violated
import io.github.tiagodocouto.helper.extension.ListExtensions.isEmpty

private const val SUFFIX_TEST = "Test"
private const val SUFFIX_KT = "Kt"
private const val IS_ANONYMOUS_CLASS = "$"
typealias TestClassesBySimpleClassName = Map<String, List<JavaClass>>

object ArchConditionShouldHaveTestForClass :
    ArchCondition<JavaClass>("should have test for class") {
    private val JavaClass.shouldIgnore: Boolean
        get() = simpleName.endsWith(SUFFIX_TEST) ||
            simpleName.endsWith(SUFFIX_KT) ||
            name.contains(IS_ANONYMOUS_CLASS) ||
            isInterface ||
            isAnonymousClass
    private lateinit var testClassesBySimpleClassName: TestClassesBySimpleClassName

    override fun init(allObjectsToTest: MutableCollection<JavaClass>) {
        testClassesBySimpleClassName =
            allObjectsToTest.filter { clazz ->
                clazz.name.endsWith(SUFFIX_TEST) && !clazz.isAnonymousClass
            }.groupBy { clazz -> clazz.simpleName }
    }

    override fun check(item: JavaClass, events: ConditionEvents) {
        if (item.shouldIgnore) {
            return
        }

        testClassesBySimpleClassName[item.simpleName + SUFFIX_TEST].isEmpty {
            events.add(violated(item, "${item.name} does not have a test class"))
        }
    }
}
