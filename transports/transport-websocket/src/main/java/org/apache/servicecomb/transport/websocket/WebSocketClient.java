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
package org.apache.servicecomb.transport.websocket;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.WebSocket;

import java.util.concurrent.atomic.AtomicInteger;

public class WebSocketClient {


    private WebSocket webSocket;

    private final AtomicInteger receiveMsgCounter = new AtomicInteger(0);
    private final AtomicInteger sendMsgCounter = new AtomicInteger(0);

    public WebSocketClient(WebSocket webSocket) {
        this.webSocket = webSocket;
    }

    @Fluent
    public WebSocketClient init() {
        WebSocketClientConnection.getInstance().init(this.webSocket);
        return this;
    }

    @Fluent
    public WebSocketClient receiveHandler(Handler<Buffer> bufferHandler) throws Exception {
        this.receiveMsgCounter.incrementAndGet();
        this.webSocket.handler(bufferHandler);
        return this;
    }

    @Fluent
    public WebSocketClient close() {
        this.webSocket.close();
        return this;
    }

    @Fluent
    public WebSocketClient sendTestMessage(String message) {
        this.sendMsgCounter.incrementAndGet();
        this.webSocket.writeTextMessage(message);
        return this;
    }

    @Fluent
    public WebSocketClient sendBinaryMessage(Buffer buffer, int s) {
        this.sendMsgCounter.incrementAndGet();
        this.webSocket.writeBinaryMessage(buffer);
        return this;
    }

    public AtomicInteger getReceiveMsgCounter() {
        return this.receiveMsgCounter;
    }

    public AtomicInteger getConnectedClientCounter() {
        return this.sendMsgCounter;
    }
}
