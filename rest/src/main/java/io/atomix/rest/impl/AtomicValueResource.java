/*
 * Copyright 2017-present Open Networking Foundation
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
package io.atomix.rest.impl;

import io.atomix.primitives.value.AsyncAtomicValue;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * Atomic value resource.
 */
public class AtomicValueResource extends AbstractRestResource {
  private final AsyncAtomicValue<String> value;

  public AtomicValueResource(AsyncAtomicValue<String> value) {
    this.value = value;
  }

  @GET
  @Path("/")
  @Produces(MediaType.APPLICATION_JSON)
  public void get(@Suspended AsyncResponse response) {
    value.get().whenComplete((result, error) -> {
      if (error == null) {
        response.resume(Response.status(Status.OK)
            .entity(result)
            .build());
      } else {
        response.resume(Response.status(Status.INTERNAL_SERVER_ERROR).build());
      }
    });
  }

  @PUT
  @Path("/")
  @Consumes(MediaType.TEXT_PLAIN)
  public void set(String body, @Suspended AsyncResponse response) {
    value.set(body).whenComplete((result, error) -> {
      if (error == null) {
        response.resume(Response.status(Status.OK).build());
      } else {
        response.resume(Response.status(Status.INTERNAL_SERVER_ERROR).build());
      }
    });
  }

  @POST
  @Path("/cas")
  @Produces(MediaType.APPLICATION_JSON)
  public void compareAndSet(CompareAndSetRequest request, @Suspended AsyncResponse response) {
    value.compareAndSet(request.getExpect(), request.getUpdate()).whenComplete((result, error) -> {
      if (error == null) {
        response.resume(Response.status(Status.OK)
            .entity(result)
            .build());
      } else {
        response.resume(Response.status(Status.INTERNAL_SERVER_ERROR).build());
      }
    });
  }

  /**
   * Compare and set request.
   */
  static class CompareAndSetRequest {
    private String expect;
    private String update;

    public String getExpect() {
      return expect;
    }

    public void setExpect(String expect) {
      this.expect = expect;
    }

    public String getUpdate() {
      return update;
    }

    public void setUpdate(String update) {
      this.update = update;
    }
  }
}
