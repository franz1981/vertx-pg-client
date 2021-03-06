/*
 * Copyright (C) 2017 Julien Viet
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
 *
 */

package com.julienviet.pgclient.impl;

import com.julienviet.pgclient.PgUpdate;
import com.julienviet.pgclient.UpdateResult;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:emad.albloushi@gmail.com">Emad Alblueshi</a>
 */

public class PgUpdateImpl implements PgUpdate {

  final PgPreparedStatementImpl ps;
  final List<Object> params;

  PgUpdateImpl(PgPreparedStatementImpl ps, List<Object> params) {
    this.ps = ps;
    this.params = params;
  }

  @Override
  public void execute(Handler<AsyncResult<UpdateResult>> handler) {
    ps.update(Collections.singletonList(params), ar -> handler.handle(ar.map(results -> results.get(0))));
  }
}
