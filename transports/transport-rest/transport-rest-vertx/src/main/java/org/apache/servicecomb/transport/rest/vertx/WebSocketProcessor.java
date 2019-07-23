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
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringValueResolver;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;

@Component
public class WebSocketProcessor implements BeanPostProcessor, EmbeddedValueResolverAware {
    private StringValueResolver resolver;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        ReflectionUtils.doWithFields(bean.getClass(), new ReflectionUtils.FieldCallback() {
            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                processWebSocketFeild(bean, field);
            }
        });

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String baenName) throws BeansException {
        return bean;
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver stringValueResolver) {
        this.resolver = stringValueResolver;
    }

    protected void processWebSocketFeild(Object bean, Field field){
        WebSocketSchema webSocketSchema = field.getAnnotation(WebSocketSchema.class);
        if(null == webSocketSchema){
            return;
        }

        handleWebSocketField(bean, field, webSocketSchema);
    }

    private void handleWebSocketField(Object obj, Field field, WebSocketSchema webSocketSchema){
        System.out.println("handle web socket field");
        int port = webSocketSchema.port();
        Vertx vertx = Vertx.vertx();

        CountDownLatch countDownLatch = new CountDownLatch(1);
        System.out.println("count down");
        vertx.createHttpServer().websocketHandler(serverWebSocket -> {
            System.out.println("start listen");
            System.out.println(field);
            System.out.println(obj);
            System.out.println(serverWebSocket);
            ReflectionUtils.makeAccessible(field);
            ReflectionUtils.setField(field, obj, serverWebSocket);
            countDownLatch.countDown();
        }).listen(port);


        try{
            countDownLatch.await();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
