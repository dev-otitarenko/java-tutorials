/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 */

package com.maestro.lib.cssparser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public final class CSSPropertyValueTest {
	@Test
	public void testGetters() {
		CSSPropertyValue propertyValue = new CSSPropertyValue("width", "100px");

		assertEquals(propertyValue.getProperty(), "width");
		assertEquals(propertyValue.getValue(), "100px");
	}

	@Test
	public void testHashCode() {
		CSSPropertyValue propertyValue = new CSSPropertyValue("width", "100px");

		assertEquals(propertyValue.toString().hashCode(), propertyValue.hashCode());
	}

	@Test
	public void testEquals() {
		CSSPropertyValue selector1a = new CSSPropertyValue("width", "100%");
		CSSPropertyValue selector1b = new CSSPropertyValue("width", "100%");
		CSSPropertyValue selector2 = new CSSPropertyValue("width", "200%");
		CSSPropertyValue selectorNull = null;

		assertEquals(selector1a, selector1b);
		assertNotEquals(selector1a, selector2);
		assertNotEquals(selector1a, selectorNull);
	}
}
