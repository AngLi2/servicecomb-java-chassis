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

import io.vertx.core.http.HttpClient;
import io.vertx.core.http.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebSocketClientConnection {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketServerConnection.class);

    private WebSocketClientConnection() {
    }

    private static volatile WebSocketClientConnection instance = null;

    public static WebSocketClientConnection getInstance() {
        if (null == instance) { //Single Checked
            synchronized (WebSocketClientConnection.class) {
                if (null == instance) { //Double Checked
                    instance = new WebSocketClientConnection();
                }
            }
        }
        return instance;
    }

    public void init(WebSocket webSocket) {
        String remoteAddress = webSocket.remoteAddress().toString();

        System.out.println("connect from "+ remoteAddress + " in thread "+ Thread.currentThread().getName());
        LOGGER.info("connect from {}, in thread {}",
                remoteAddress,
                Thread.currentThread().getName());

        webSocket.exceptionHandler(e -> {
            System.out.println(("exception from "+ remoteAddress + " in thread "+ Thread.currentThread().getName()+ ", cause "+ e.getMessage()));
            LOGGER.error("exception from {}, in thread {}, cause {}",
                    remoteAddress,
                    Thread.currentThread().getName(),
                    e.getMessage());
        }).closeHandler(Void -> {
            System.out.println("disconnect from "+ remoteAddress + " in thread "+ Thread.currentThread().getName());
            LOGGER.info("disconnected from {}, in thread {}",
                    remoteAddress,
                    Thread.currentThread().getName());
        });
    }
}
