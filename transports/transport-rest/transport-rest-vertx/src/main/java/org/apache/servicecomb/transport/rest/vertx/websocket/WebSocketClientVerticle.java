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
package org.apache.servicecomb.transport.rest.vertx.websocket;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;

public class WebSocketClientVerticle extends AbstractVerticle implements WebSocketClient{

    private HttpClient httpClient;

    //TODO: read from yaml, see org.apache.servicecomb.transport.rest.client.TransportClientConfig
    private HttpClientOptions httpClientOptions;

    public void init(){

    }

    @Override
    public void start(){

    }


    @Override
    public void close() {

    }

    @Override
    public WebSocketClient writeTextMessage(String text) {
        return null;
    }

    @Override
    public WebSocketClient writeBinaryMessage(Buffer data) {
        return null;
    }

    @Override
    public WebSocketClient messageHandler(Handler<Buffer> handler) {
        return null;
    }

    @Override
    public WebSocketClient closeHandler(Handler<Void> handler) {
        return null;
    }

    @Override
    public WebSocketClient exceptionHandler(Handler<Throwable> handler) {
        return null;
    }
}
