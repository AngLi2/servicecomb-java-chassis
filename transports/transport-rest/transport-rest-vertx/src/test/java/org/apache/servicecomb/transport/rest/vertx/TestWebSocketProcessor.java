package org.apache.servicecomb.transport.rest.vertx;

import io.vertx.core.http.ServerWebSocket;
import org.junit.Assert;
import org.junit.Test;

import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

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
        //测试的时候注意设置端口号为空闲的端口号
        @WebSocketReference(port = 8080)
        ServerWebSocket serverWebSocket;
    }

    class TestNotWebSocket{
        //用于测试当 WebSocketReference 注解标注在非 ServerWebSocket 的情况
        @WebSocketReference(port = 8080)
        String fakeWebSocket;
    }

    @Test
    public void postProcessAfterInitialization(){
        Object bean = new Object();
        Assert.assertSame(bean, webSocketProcessor.postProcessAfterInitialization(bean, "test"));
    }

    @Test
    public void testNoWebSocketRefrence(){
        TestNotWebSocket bean = new TestNotWebSocket();

        Assert.assertNull(bean.fakeWebSocket);

        Assert.assertSame(bean, webSocketProcessor.postProcessBeforeInitialization(bean, "id"));

        Assert.assertNull(bean.fakeWebSocket);
    }

    @Test
    public void testWebSocketRefrence(){
        TestWebSocket bean = new TestWebSocket();

        Assert.assertNull(bean.serverWebSocket);

        //这里需要在 port 发送任意一个 websocket 请求，否则会导致 postProcessBeforeInitialization handler 阻塞在这
        Assert.assertSame(bean, webSocketProcessor.postProcessBeforeInitialization(bean, "id"));

        Assert.assertNotNull(bean.serverWebSocket);

        bean.serverWebSocket.writeTextMessage("asdasdasd");

        bean.serverWebSocket.handler(System.out::println);

        for(;;) {
            Scanner scanner = new Scanner(System.in);
            String text = scanner.next();
            bean.serverWebSocket.writeTextMessage(text);
        }
    }


}
