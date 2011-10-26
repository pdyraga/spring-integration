/*
 * Copyright 2002-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.integration.amqp.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.test.util.TestUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Mark Fisher
 * @since 2.1
 */
@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class AmqpInboundGatewayParserTests {

	@Autowired
	private ApplicationContext context;

	@Test
	public void customMessageConverter() {
		Object gateway = context.getBean("gateway");
		MessageConverter gatewayConverter = TestUtils.getPropertyValue(gateway, "amqpMessageConverter", MessageConverter.class);
		MessageConverter templateConverter = TestUtils.getPropertyValue(gateway, "amqpTemplate.messageConverter", MessageConverter.class);
		TestConverter testConverter = context.getBean("testConverter", TestConverter.class);
		assertSame(testConverter, gatewayConverter);
		assertSame(testConverter, templateConverter);
		assertEquals(Boolean.TRUE, TestUtils.getPropertyValue(gateway, "autoStartup"));
		assertEquals(0, TestUtils.getPropertyValue(gateway, "phase"));
	}

	@Test
	public void verifyLifeCycle() {
		Object gateway = context.getBean("autoStartFalseGateway");
		assertEquals(Boolean.FALSE, TestUtils.getPropertyValue(gateway, "autoStartup"));
		assertEquals(123, TestUtils.getPropertyValue(gateway, "phase"));
	}

	private static class TestConverter extends SimpleMessageConverter {}

}
