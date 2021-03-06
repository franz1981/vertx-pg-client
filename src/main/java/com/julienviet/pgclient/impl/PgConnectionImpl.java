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

import com.julienviet.pgclient.*;
import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;

import java.util.UUID;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class PgConnectionImpl extends PgOperationsImpl implements PgConnection, Connection.Holder {

  private final Context context;
  public final Connection conn;
  private volatile Handler<Throwable> exceptionHandler;
  private volatile Handler<Void> closeHandler;

  public PgConnectionImpl(Context context, Connection conn) {
    this.context = context;
    this.conn = conn;
  }

  @Override
  public Connection connection() {
    return conn;
  }

  @Override
  public void handleClosed() {
    Handler<Void> handler = closeHandler;
    if (handler != null) {
      context.runOnContext(handler);
    }
  }

  @Override
  protected void schedule(CommandBase cmd) {
    conn.schedule(cmd);
  }

  @Override
  public void handleException(Throwable err) {
    Handler<Throwable> handler = exceptionHandler;
    if (handler != null) {
      context.runOnContext(v -> {
        handler.handle(err);
      });
    }
  }

  @Override
  public boolean isSSL() {
    return conn.isSsl();
  }

  @Override
  public void execute(String sql, Handler<AsyncResult<ResultSet>> handler) {
    conn.schedule(new SimpleQueryCommand(sql, new ResultSetBuilder(handler)));
  }

  @Override
  public PgConnection closeHandler(Handler<Void> handler) {
    closeHandler = handler;
    return this;
  }

  @Override
  public PgConnection exceptionHandler(Handler<Throwable> handler) {
    exceptionHandler = handler;
    return this;
  }

  @Override
  public void close() {
    conn.close(this);
  }

  @Override
  public PgPreparedStatement prepare(String sql) {
    // todo : should somehow try to reuse existing cache or make it automatic ? (I think we can)
    return new PgPreparedStatementImpl(conn, sql, UUID.randomUUID().toString());
  }
}
