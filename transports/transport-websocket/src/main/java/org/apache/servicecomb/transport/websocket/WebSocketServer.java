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
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ServerWebSocket;
import org.apache.servicecomb.codec.protobuf.definition.OperationProtobuf;
import org.apache.servicecomb.core.Endpoint;
import org.apache.servicecomb.core.Invocation;
import org.apache.servicecomb.core.SCBEngine;
import org.apache.servicecomb.core.definition.MicroserviceMeta;
import org.apache.servicecomb.core.definition.OperationMeta;
import org.apache.servicecomb.core.definition.SchemaMeta;
import org.apache.servicecomb.core.invocation.InvocationFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class WebSocketServer {

    private ServerWebSocket serverWebSocket;

    private Invocation invocation;

    private Endpoint endpoint;

    private OperationMeta operationMeta;

    private final AtomicInteger connectedClientCounter = new AtomicInteger(0);
    private final AtomicInteger sendMsgCounter = new AtomicInteger(0);


    @Fluent
    public WebSocketServer initAndListen(int port) {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        Vertx.vertx().createHttpServer().websocketHandler(serverWebSocket -> {
            this.serverWebSocket = serverWebSocket;
            countDownLatch.countDown();
        }).listen(port);

        try {
            countDownLatch.await();
        } catch (Exception e) {
            System.out.println("Init fail");
        }

        //TODO
        MicroserviceMeta microserviceMeta = SCBEngine.getInstance().getProducerMicroserviceMeta();
        //this.operationMeta = microserviceMeta.findOperation(operationName);

        //TODO: 根据 localAddress 生成 endpoint?
        //endpoint = serverWebSocket.localAddress();

//        invocation = InvocationFactory.forProvider(endpoint,
//                operationMeta,
//                null);
//        invocation.getInvocationStageTrace().startSchedule();

        WebSocketServerConnection.getInstance().init(this.serverWebSocket);
        this.connectedClientCounter.incrementAndGet();
        System.out.println("websocketserver init successfully, counter is: " + connectedClientCounter);
        return this;
    }

    @Fluent
    public WebSocketServer receiveHandler(Handler<Buffer> bufferHandler) {
        this.serverWebSocket.handler(bufferHandler);
        return this;
    }

    @Fluent
    public WebSocketServer close() {
        this.connectedClientCounter.decrementAndGet();
        this.serverWebSocket.close();
        System.out.println("websocketserver closed");
        return this;
    }

    @Fluent
    public WebSocketServer sendTestMessage(String message) {
        this.sendMsgCounter.incrementAndGet();
        this.serverWebSocket.writeTextMessage(message);
        System.out.println("Message sent, message is: " + message + " counter is: " + sendMsgCounter);
        return this;
    }

    @Fluent
    public WebSocketServer sendBinaryMessage(Buffer buffer) {
        this.sendMsgCounter.incrementAndGet();
        this.serverWebSocket.writeBinaryMessage(buffer);
        return this;
    }
}
