/**
 * Designed and developed by Aidan Follestad (@afollestad)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.afollestad.vvalidator.assertion

import android.content.Context
import android.view.View
import com.afollestad.vvalidator.ValidationContainer
import com.afollestad.vvalidator.testutil.assertEqualTo
import com.afollestad.vvalidator.testutil.assertFalse
import com.afollestad.vvalidator.testutil.assertTrue
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.junit.Test

open class TestView(context: Context) : View(context, null)

class TestBaseAssertion : Assertion<TestView, TestBaseAssertion>() {
  override fun isValid(view: TestView) = true

  override fun defaultDescription() = "Hello, World!"
}

/** @author Aidan Follestad (@afollestad) */
class AssertionTest {
  companion object {
    private const val VIEW_ID = 1
    private const val STRING_ID = 2
  }

  private val context = mock<Context> {
    on { getString(STRING_ID) } doReturn "Hello, Resource!"
  }
  private val view = mock<TestView> {
    on { id } doReturn VIEW_ID
  }

  @Suppress("UNCHECKED_CAST")
  private val testContainer = object : ValidationContainer(context) {
    override fun <T : View> findViewById(id: Int): T? = view as T
  }

  private val assertion = TestBaseAssertion().apply {
    this.container = testContainer
  }

  @Test fun description() {
    assertion.description()
        .assertEqualTo("Hello, World!")
    assertion.description("Hey there")
    assertion.description()
        .assertEqualTo("Hey there")
  }

  @Test fun description_res() {
    assertion.description()
        .assertEqualTo("Hello, World!")
    assertion.description(STRING_ID)
    assertion.description()
        .assertEqualTo("Hello, Resource!")
  }

  @Test fun condition() {
    assertion.condition = null
    assertion.isConditionMet()
        .assertTrue()
    assertion.condition = { true }
    assertion.isConditionMet()
        .assertTrue()
    assertion.condition = { false }
    assertion.isConditionMet()
        .assertFalse()
  }
}
