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

import com.julienviet.pgclient.codec.encoder.message.Terminate;

class CloseConnectionCommand extends CommandBase {

  static final CloseConnectionCommand INSTANCE = new CloseConnectionCommand();

  private CloseConnectionCommand() {
  }

  @Override
  void exec(SocketConnection conn) {
    conn.writeMessage(Terminate.INSTANCE);
  }

  @Override
  void fail(Throwable err) {
  }
}
