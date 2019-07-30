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

import io.vertx.core.Vertx;

import java.util.Scanner;

public class TestWebSocketServer {

    WebSocketServer webSocketServer;

    public static void main(String[] args) {
        TestWebSocketServer testWebSocketServer = new TestWebSocketServer();
        testWebSocketServer.start();
    }

    public  void start(){
        this.webSocketServer = new WebSocketServer();
        webSocketServer
                .initAndListen(8080)
                .receiveHandler(System.out::println)
                .sendTestMessage("das");


        for(;;){
            Scanner scanner = new Scanner(System.in);
            String msg = scanner.next();
            this.webSocketServer.sendTestMessage(msg);
        }
    }
}
