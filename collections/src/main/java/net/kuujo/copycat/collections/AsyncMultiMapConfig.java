/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.kuujo.copycat.collections;

import net.kuujo.copycat.ResourceConfig;
import net.kuujo.copycat.StateLogConfig;
import net.kuujo.copycat.cluster.ClusterConfig;
import net.kuujo.copycat.cluster.coordinator.CoordinatedResourceConfig;
import net.kuujo.copycat.cluster.coordinator.CoordinatedResourcePartitionConfig;
import net.kuujo.copycat.collections.internal.map.DefaultAsyncMultiMap;

import java.util.Map;

/**
 * Asynchronous multi-map configuration.
 *
 * @author <a href="http://github.com/kuujo">Jordan Halterman</a>
 */
public class AsyncMultiMapConfig extends ResourceConfig<AsyncMultiMapConfig> {

  public AsyncMultiMapConfig() {
  }

  public AsyncMultiMapConfig(Map<String, Object> config) {
    super(config);
  }

  protected AsyncMultiMapConfig(AsyncMultiMapConfig config) {
    super(config);
  }

  @Override
  public AsyncMultiMapConfig copy() {
    return new AsyncMultiMapConfig(this);
  }

  @Override
  public CoordinatedResourceConfig resolve(ClusterConfig cluster) {
    StateLogConfig config = new StateLogConfig(toMap());
    return new CoordinatedResourceConfig()
      .withResourceFactory(DefaultAsyncMultiMap::new)
      .withResourceConfig(config)
      .withElectionTimeout(getElectionTimeout())
      .withHeartbeatInterval(getHeartbeatInterval())
      .withLog(getLog())
      .withPartitions(new CoordinatedResourcePartitionConfig()
        .withPartition(1)
        .withReplicas(cluster.getMembers())
        .withResourceConfig(config));
  }

}
