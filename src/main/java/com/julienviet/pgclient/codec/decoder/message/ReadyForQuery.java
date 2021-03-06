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

package com.julienviet.pgclient.codec.decoder.message;

import com.julienviet.pgclient.codec.decoder.InboundMessage;
import com.julienviet.pgclient.codec.TransactionStatus;

import java.util.Objects;

/**
 *
 * <p>
 * The frontend can issue commands. Every message returned from the backend has transaction status
 * that would be one of the following
 *
 * <p>
 * IDLE : Not in a transaction block
 * <p>
 * ACTIVE : In transaction block
 * <p>
 * FAILED : Failed transaction block (queries will be rejected until block is ended)
 *
 * @author <a href="mailto:emad.albloushi@gmail.com">Emad Alblueshi</a>
 *
 */

public class ReadyForQuery implements InboundMessage {

  private final TransactionStatus transactionStatus;

  public ReadyForQuery(TransactionStatus transactionStatus) {
    this.transactionStatus = transactionStatus;
  }

  public TransactionStatus getTransactionStatus() {
    return transactionStatus;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ReadyForQuery that = (ReadyForQuery) o;
    return transactionStatus == that.transactionStatus;
  }

  @Override
  public int hashCode() {
    return Objects.hash(transactionStatus);
  }


  @Override
  public String toString() {
    return "ReadyForQuery{" +
      "transactionStatus=" + transactionStatus +
      '}';
  }
}
