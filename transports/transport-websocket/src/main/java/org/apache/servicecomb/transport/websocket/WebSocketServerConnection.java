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

import io.vertx.core.http.ServerWebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebSocketServerConnection {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketServerConnection.class);

    private WebSocketServerConnection() {
    }

    private static volatile WebSocketServerConnection instance = null;

    public static WebSocketServerConnection getInstance() {
        if (null == instance) { //Single Checked
            synchronized (WebSocketServerConnection.class) {
                if (null == instance) { //Double Checked
                    instance = new WebSocketServerConnection();
                }
            }
        }
        return instance;
    }

    public void init(ServerWebSocket serverWebSocket) {
        String remoteAddress = serverWebSocket.remoteAddress().toString();

        LOGGER.info("connect from {}, in thread {}",
                remoteAddress,
                Thread.currentThread().getName());

        serverWebSocket.exceptionHandler(e -> {
            LOGGER.error("exception from {}, in thread {}, cause {}",
                    remoteAddress,
                    Thread.currentThread().getName(),
                    e.getMessage());
        }).closeHandler(Void -> {
            LOGGER.info("disconnected from {}, in thread {}",
                    remoteAddress,
                    Thread.currentThread().getName());
        });
    }
}
