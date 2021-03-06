/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.servicecomb.swagger.invocation.arguments.producer;

import org.apache.servicecomb.swagger.engine.SwaggerEnvironment;
import org.apache.servicecomb.swagger.engine.SwaggerProducer;
import org.apache.servicecomb.swagger.invocation.SwaggerInvocation;
import org.apache.servicecomb.swagger.invocation.schemas.SpringmvcAddBodyV1;
import org.apache.servicecomb.swagger.invocation.schemas.SpringmvcAddV1;
import org.apache.servicecomb.swagger.invocation.schemas.SpringmvcAddWrapperV1;
import org.apache.servicecomb.swagger.invocation.schemas.models.AddWrapperV1;
import org.junit.Assert;
import org.junit.Test;

public class TestSpringmvc {
  @Test
  public void add() {
    SwaggerProducer swaggerProducer = new SwaggerEnvironment().createProducer(new SpringmvcAddV1(), null);
    ProducerArgumentsMapper mapper = swaggerProducer.findOperation("add").getArgumentsMapper();

    SwaggerInvocation invocation = new SwaggerInvocation();
    invocation.setSwaggerArguments(new Object[] {1, 2});

    Object[] arguments = mapper.toProducerArgs(invocation);

    Assert.assertEquals(2, arguments.length);
    Assert.assertEquals(1, arguments[0]);
    Assert.assertEquals(2, arguments[1]);
  }

  @Test
  public void addWrapper() {
    SwaggerProducer swaggerProducer = new SwaggerEnvironment().createProducer(new SpringmvcAddWrapperV1(), null);
    ProducerArgumentsMapper mapper = swaggerProducer.findOperation("add").getArgumentsMapper();

    SwaggerInvocation invocation = new SwaggerInvocation();
    invocation.setSwaggerArguments(new Object[] {1, 2});

    Object[] arguments = mapper.toProducerArgs(invocation);

    Assert.assertEquals(1, arguments.length);
    AddWrapperV1 paramV1 = (AddWrapperV1) arguments[0];
    Assert.assertEquals(1, paramV1.getX());
    Assert.assertEquals(2, paramV1.y);
  }

  @Test
  public void addBody() {
    SwaggerProducer swaggerProducer = new SwaggerEnvironment().createProducer(new SpringmvcAddBodyV1(), null);
    ProducerArgumentsMapper mapper = swaggerProducer.findOperation("add").getArgumentsMapper();

    AddWrapperV1 addBody = new AddWrapperV1();
    SwaggerInvocation invocation = new SwaggerInvocation();
    invocation.setSwaggerArguments(new Object[] {addBody});

    Object[] arguments = mapper.toProducerArgs(invocation);

    Assert.assertEquals(1, arguments.length);
    Assert.assertSame(addBody, arguments[0]);
  }
}
