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
import io.vertx.core.buffer.Buffer;

import java.lang.reflect.Method;

public enum WebSocketMethodAnnotations {

    @SuppressWarnings(value={"unchecked", "rawtypes"})
    WEBSOCKET_ON_MESSAGE(OnMessage.class){

        @Override
        public Handler getHandler(Method method, Object invokeBean){
            Handler<Buffer> bufferHandler = null;
            try{
                bufferHandler = (Handler<Buffer>) method.invoke(invokeBean);
            }catch (ReflectiveOperationException reflectiveException){

            }
            return bufferHandler;
        }
    },

    @SuppressWarnings(value={"unchecked", "rawtypes"})
    WEBSOCKET_ON_CLOSE(OnClose.class){
        @Override
        public Handler getHandler(Method method, Object invokeBean) {
            Handler<Void> closeHandler = null;
            try{
                closeHandler = (Handler<Void>) method.invoke(invokeBean);
            }catch (ReflectiveOperationException reflectiveException){

            }
            return closeHandler;
        }
    };

    @SuppressWarnings(value={"unchecked", "rawtypes"})
    private WebSocketMethodAnnotations(Class annotationClass){

    }

    @SuppressWarnings(value={"unchecked", "rawtypes"})
    public abstract Handler getHandler(Method method, Object invokeBean);
}
