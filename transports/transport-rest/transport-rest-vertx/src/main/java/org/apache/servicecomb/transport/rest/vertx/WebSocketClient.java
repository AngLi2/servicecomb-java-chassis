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
package org.apache.servicecomb.transport.rest.vertx;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.WebSocket;
import io.vertx.core.http.WebSocketFrame;
import org.apache.commons.lang.StringUtils;
import org.apache.servicecomb.serviceregistry.api.registry.MicroserviceInstance;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class WebSocketClient {
    
    private WebSocket webSocket;

//    List<MicroserviceInstance> microserviceInstances;

    public void connectTo(MicroserviceInstance microserviceInstance){
        List<String> endpoints = microserviceInstance.getEndpoints();
        String restEndpoint = findFirstRestUri(endpoints);
        if(StringUtils.isEmpty(restEndpoint)){
            throw new NullPointerException("no standard rest uri found, please check endpoint list");
        }
        this.init(restEndpoint);
    }

//
//    public void setMicroserviceInstances(String appId, String serviceName, String versionRule){
//        setMicroserviceInstances(RegistryUtils.findServiceInstance(appId, serviceName, versionRule));
//    }
//
//    public void setMicroserviceInstances(List<MicroserviceInstance> microserviceInstances){
//        this.microserviceInstances = microserviceInstances;
//        if(!hasMicroserviceInstances((this.microserviceInstances))){
//            throw new NullPointerException("No Microservice Instance Found");
//        }
//    }

//    private boolean hasMicroserviceInstances(List<MicroserviceInstance> microserviceInstances){
//        if(null==microserviceInstances || 0==microserviceInstances.size()){
//            return false;
//        }
//        return true;
//    }

    @Fluent
    public WebSocketClient exceptionHandler(Handler<Throwable> handler){
        this.webSocket.exceptionHandler(handler);
        return this;
    }
    

    @Fluent
    public WebSocketClient handler(Handler<Buffer> handler){
        this.webSocket.handler(handler);
        return this;
    }

    @Fluent
    public WebSocketClient pause(){
        this.webSocket.pause();
        return this;
    }

    @Fluent
    public WebSocketClient resume(){
        this.webSocket.resume();
        return this;
    }

    @Fluent
    public WebSocketClient fetch(long amount){
        this.webSocket.fetch(amount);
        return this;
    }

    @Fluent
    public WebSocketClient endHandler(Handler<Void> endHandler){
        this.webSocket.endHandler(endHandler);
        return this;
    }

    @Fluent
    public WebSocketClient write(Buffer data){
        this.webSocket.write(data);
        return this;
    }

    @Fluent
    public WebSocketClient setWriteQueueMaxSize(int maxSize){
        this.webSocket.setWriteQueueMaxSize(maxSize);
        return this;
    }

    @Fluent
    public WebSocketClient drainHandler(Handler<Void> handler){
        this.webSocket.drainHandler(handler);
        return this;
    }

    @Fluent
    public WebSocketClient writeFrame(WebSocketFrame frame){
        this.webSocket.writeFrame(frame);
        return this;
    }

    @Fluent
    public WebSocketClient writeFinalTextFrame(String text){
        this.webSocket.writeFinalTextFrame(text);
        return this;
    }

    @Fluent
    public WebSocketClient writeFinalBinaryFrame(Buffer data){
        this.webSocket.writeFinalBinaryFrame(data);
        return this;
    }

    @Fluent
    public WebSocketClient writeBinaryMessage(Buffer data){
        this.webSocket.writeBinaryMessage(data);
        return this;
    }

    @Fluent
    public WebSocketClient writeTextMessage(String text){
        this.webSocket.writeTextMessage(text);
        return this;
    }

    @Fluent
    public WebSocketClient closeHandler(Handler<Void> handler){
        this.webSocket.closeHandler(handler);
        return this;
    }

    @Fluent
    public WebSocketClient frameHandler(Handler<WebSocketFrame> handler){
        this.webSocket.frameHandler(handler);
        return this;
    }

    private String findFirstRestUri(List<String> endpoints){
        String endpoint = endpoints
                .stream()
                .filter((String str)->str.startsWith("rest"))
                .findFirst()
                .orElse(null);
        return endpoint;
    }

    private void init(String endpoint) {
        URI formatUri = null;

        try{
            formatUri = new URI(endpoint);
        }catch (URISyntaxException ignore){
            ignore.printStackTrace();
        }

        int port = formatUri.getPort();
        String host = formatUri.getHost();
        String path = formatUri.getPath();
        init(port, host, path);
    }

    private void init(int port, String host, String path){
        Vertx vertx = Vertx.vertx();

        CountDownLatch countDownLatch = new CountDownLatch(1);

        vertx.createHttpClient().websocket(port, host, path,
                ws -> {
                    this.webSocket = ws;
                    countDownLatch.countDown();
                });

        try{
            countDownLatch.await();
        }catch (InterruptedException ignore){
            ignore.printStackTrace();
        }
    }

}
