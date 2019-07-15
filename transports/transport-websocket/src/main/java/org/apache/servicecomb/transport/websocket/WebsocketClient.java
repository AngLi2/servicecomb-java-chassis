package org.apache.servicecomb.transport.websocket;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.impl.WebSocketImpl;
import org.apache.servicecomb.loadbalance.ServiceCombServer;

import java.util.List;

public class WebsocketClient{

    HttpServer httpServer;

    List<ServiceCombServer> serviceCombServerList;

    @Fluent
    public WebsocketClient handshake(ServiceCombServer serviceCombServer){
        return this;
    }

    @Fluent
    public WebsocketClient handshake(List<ServiceCombServer> serviceCombServerList){
        return this;
    }

    //TODO: LB 策略选择
}
