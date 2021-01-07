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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class CSSRuleTest {
	@Test
	public void testBasic() {

		CSSSelector selector = new CSSSelector("div");

		CSSRule rule = new CSSRule(selector);

		assertEquals(selector, rule.getSelectors().get(0));

	}

	@Test
	public void testRemoveSelector() {
		CSSSelector selector = new CSSSelector("div");

		CSSRule rule = new CSSRule(selector);
		rule.removeSelector(selector);

		assertEquals(0, rule.getSelectors().size());
	}

	@Test
	public void testRemovePropertyValue() {
		CSSSelector selector = new CSSSelector("div");
		CSSPropertyValue propertyValue = new CSSPropertyValue("width", "100px");
		CSSRule rule = new CSSRule(selector);
		rule.addPropertyValue(propertyValue);

		rule.removePropertyValue(propertyValue);

		assertEquals(0, rule.getPropertyValues().size());
	}

	@Test
	public void testAddSelectors() {
		List<CSSSelector> selectors = new ArrayList<CSSSelector>();
		selectors.add(new CSSSelector("div"));
		selectors.add(new CSSSelector("table"));

		CSSRule rule = new CSSRule(selectors);
		rule.addSelectors(selectors);

		assertEquals(4, rule.getSelectors().size());

	}
}
