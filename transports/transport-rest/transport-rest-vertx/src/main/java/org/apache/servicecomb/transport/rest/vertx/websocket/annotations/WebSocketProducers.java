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
package org.apache.servicecomb.transport.rest.vertx.websocket.annotations;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.ext.web.Router;

import org.apache.servicecomb.foundation.common.utils.BeanUtils;
import org.apache.servicecomb.foundation.vertx.VertxUtils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class WebSocketProducers implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        try {
            processProvider(bean, beanName);
        } catch (Exception e) {

        }


        return bean;
    }

    @SuppressWarnings("unchecked")
    protected void processProvider(Object bean, String beanName) {

        Class<?> beanClass = BeanUtils.getImplClassFromBean(bean);

        if (beanClass == null) {
            return;
        }
        WebSocket webSocket = beanClass.getAnnotation(WebSocket.class);
        if (webSocket == null) {
            return;
        }

        String endPoint = webSocket.path();
        if (StringUtils.isEmpty(endPoint)) {
            System.out.println("[INFO] EndPoint is null, the class is used as client");
        } else {
            VertxOptions vertxOptions = new VertxOptions();
            Vertx vertx = VertxUtils.getOrCreateVertxByName("WebSocket", vertxOptions);
            Router router = Router.router(vertx);

            HttpServer httpServer = vertx.createHttpServer();

            router.route(endPoint).handler(routingContext -> {
                ServerWebSocket serverWebSocket = routingContext.request().upgrade();
                try{
                    //TODO
                    //process(bean);
                }catch (Exception e){

                }
                Handler<Void> startHandler = v -> {
                    serverWebSocket.writeTextMessage("QNM");
                };
                startHandler.handle(null);
                serverWebSocket.handler(messageHandler);
                serverWebSocket.closeHandler(closeHandler);
            });

            httpServer.requestHandler(router).listen(8080);
        }
    }

    Handler<Buffer> messageHandler = null;
    Handler<Void> closeHandler = null;

    @SuppressWarnings("unchecked")
    public void process(Object bean, ServerWebSocket serverWebSocket) throws Exception {
        System.out.println("start process");

        if (bean == null) {
            System.out.println("bean is null");
            return;
        }

        Method[] methods = bean.getClass().getMethods();

        if (methods == null) {
            System.out.println("no onMessage");
            return;
        }


        for (Method method : methods) {
            Annotation messageAnnotation = method.getAnnotation(OnMessage.class);
            if (messageAnnotation != null) {
                messageHandler = (Handler<Buffer>) returnIfExist(WebSocketMethodAnnotations.WEBSOCKET_ON_MESSAGE,
                        "can not find message annotation",
                        method, bean);
            }

            Annotation closeAnnotation = method.getAnnotation(OnClose.class);
            if (closeAnnotation != null) {
                closeHandler = (Handler<Void>) returnIfExist(WebSocketMethodAnnotations.WEBSOCKET_ON_CLOSE,
                        "can not find close annotation",
                        method, bean);
            }
        }
        return;
    }

    private Handler<?> returnIfExist(WebSocketMethodAnnotations annotation, String msg, Method method, Object bean) {
        if (annotation == null) {
            System.out.println(msg);
            return null;
        } else {
            return annotation.getHandler(method, bean);
        }
    }
}
