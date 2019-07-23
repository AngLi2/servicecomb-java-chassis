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

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;

@Component
public class WebSocketProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        ReflectionUtils.doWithFields(bean.getClass(), new ReflectionUtils.FieldCallback() {
            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                //此注解只处理 ServerWebSocket,若被标注在非 ServerWebSocket 对象，则抛出异常
                if (!field.getType().equals(ServerWebSocket.class)) {
                    return;
                }

                processWebSocketFeild(bean, field);
            }
        });

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String baenName) throws BeansException {
        return bean;
    }

    protected void processWebSocketFeild(Object bean, Field field) {
        WebSocketSchema webSocketSchema = field.getAnnotation(WebSocketSchema.class);

        //若没有被标注 WebSocketSchema 注解，则直接退出
        if (null == webSocketSchema) {
            return;
        }

        handleWebSocketField(bean, field, webSocketSchema);
    }

    private void handleWebSocketField(Object obj, Field field, WebSocketSchema webSocketSchema) {
        //将要监听的地址
        int port = webSocketSchema.port();

        Vertx vertx = Vertx.vertx();

        HttpServer httpServer = vertx.createHttpServer();

        //这里使用一个门闩保证 websocket 实例可以初始化成功，否则异步调用 websocket 会造成实例未被初始化的问题
        CountDownLatch countDownLatch = new CountDownLatch(1);

        httpServer.websocketHandler(serverWebSocket -> {
            //handler 内部代码只在事件触发时被调用，类似于 lazy 加载模式
            ReflectionUtils.makeAccessible(field);

            //通过反射把 lambda 的 serverwebsocket 写入到被注解对象
            ReflectionUtils.setField(field, obj, serverWebSocket);

            //写入完成，门闩 count down
            countDownLatch.countDown();
        }).listen(port);

        //等待门闩释放，即等待对象初始化过程结束
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
