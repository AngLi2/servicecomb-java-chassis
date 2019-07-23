package org.apache.servicecomb.transport.rest.vertx;

import io.vertx.core.http.ServerWebSocket;
import org.junit.Assert;
import org.junit.Test;

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
public class TestWebSocketProcessor {
    WebSocketProcessor webSocketProcessor = new WebSocketProcessor();

    class TestWebSocket{
        @WebSocketSchema(port = 8081)
        ServerWebSocket serverWebSocket;
    }

    @Test
    public void postProcessAfterInitialization(){
        Object bean = new Object();
        Assert.assertSame(bean, webSocketProcessor.postProcessAfterInitialization(bean, "test"));
    }

    @Test
    public void testRefrence(){
        TestWebSocket bean = new TestWebSocket();

        Assert.assertNull(bean.serverWebSocket);

        webSocketProcessor.setEmbeddedValueResolver((strVal) -> strVal);
        Assert.assertSame(bean, webSocketProcessor.postProcessBeforeInitialization(bean, "id"));

        System.out.println("send");
        bean.serverWebSocket.writeTextMessage("das");
//        Assert.assertNotNull(bean.serverWebSocket);
    }


}
