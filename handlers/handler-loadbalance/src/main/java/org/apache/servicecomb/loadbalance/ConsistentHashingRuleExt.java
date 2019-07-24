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

package org.apache.servicecomb.loadbalance;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Random;

import com.google.common.hash.Hashing;
import org.apache.servicecomb.core.Invocation;

/**
 * A random rule.
 */
public class ConsistentHashingRuleExt implements RuleExt {
    private Random random = new Random();

    @Override
    public ServiceCombServer choose(List<ServiceCombServer> servers, Invocation invocation) {
        if (servers.size() == 0) {
            return null;
        }

        //这里做一下特殊处理，在获取不到本机的 host 的时候，将 hostAddress 设成随机数，避免根据 null 进行分发造成单个机器负载过大
        Random random = new Random();
        String hostAddress = Integer.toString(random.nextInt());

        //获取 client 端的本机 host 作为一致性哈希的识别依据
        try{
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        }catch (UnknownHostException ignore){
            ignore.printStackTrace();
        }

        long addressHash = hostAddress.hashCode();

        int index = Hashing.consistentHash(addressHash, servers.size());
        return servers.get(index);
    }
}