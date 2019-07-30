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

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import org.apache.servicecomb.transport.rest.vertx.RestServerVerticle;

public class WebSocketServerVerticle extends RestServerVerticle implements WebSocketServer{

    public void init(){

    }

    @Override
    public void start(){

    }

    @Override
    public void close() {

    }

    @Override
    public WebSocketServer writeTextMessage(String text) {
        return null;
    }

    @Override
    public WebSocketServer writeBinaryMessage(Buffer data) {
        return null;
    }

    @Override
    public WebSocketServer messageHandler(Handler<Buffer> handler) {
        return null;
    }

    @Override
    public WebSocketServer closeHandler(Handler<Void> handler) {
        return null;
    }

    @Override
    public WebSocketServer exceptionHandler(Handler<Throwable> handler) {
        return null;
    }

    @Override
    public WebSocketServer publishTextMessage(String text) {
        return null;
    }

    @Override
    public WebSocketServer publishBinaryMessage(Buffer data) {
        return null;
    }
}
