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
import io.vertx.core.http.ServerWebSocket;

import java.util.concurrent.atomic.AtomicInteger;

public class WebSocketServer {

    private ServerWebSocket serverWebSocket;

    private final AtomicInteger receiveMsgCounter = new AtomicInteger(0);
    private final AtomicInteger connectedClientCounter = new AtomicInteger(0);
    private final AtomicInteger sendMsgCounter = new AtomicInteger(0);

    public WebSocketServer(ServerWebSocket serverWebSocket){
        this.serverWebSocket = serverWebSocket;
    }

    @Fluent
    public WebSocketServer init(){
        WebSocketServerConnection.getInstance().init(this.serverWebSocket);
        this.connectedClientCounter.incrementAndGet();
        return this;
    }

    @Fluent
    public WebSocketServer receiveHandler(Handler<Buffer> bufferHandler){
        this.receiveMsgCounter.incrementAndGet();
        this.serverWebSocket.handler(bufferHandler);
        return this;
    }

    @Fluent
    public WebSocketServer close(){
        this.connectedClientCounter.decrementAndGet();
        this.serverWebSocket.close();
        return this;
    }

    @Fluent
    public WebSocketServer sendTestMessage(String message){
        this.sendMsgCounter.incrementAndGet();
        this.serverWebSocket.writeTextMessage(message);
        return this;
    }

    @Fluent
    public WebSocketServer sendBinaryMessage(Buffer buffer){
        this.sendMsgCounter.incrementAndGet();
        this.serverWebSocket.writeBinaryMessage(buffer);
        return this;
    }

    public AtomicInteger getReceiveMsgCounter(){
        return this.receiveMsgCounter;
    }

    public AtomicInteger getConnectedClientCounter(){
        return this.connectedClientCounter;
    }
}
