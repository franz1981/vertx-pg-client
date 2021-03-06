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

package com.julienviet.pgclient.codec.util;

import io.netty.buffer.ByteBuf;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.List;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.*;
import static javax.xml.bind.DatatypeConverter.*;


public class Util {

  private static final byte ZERO = 0;

  public static String readCString(ByteBuf src, Charset charset) {
    int len = src.bytesBefore(ZERO);
    String s = src.readCharSequence(len, charset).toString();
    src.readByte();
    return s;
  }

  public static String readCStringUTF8(ByteBuf src) {
    int len = src.bytesBefore(ZERO);
    String s = src.readCharSequence(len, UTF_8).toString();
    src.readByte();
    return s;
  }

  public static void writeCString(ByteBuf dst, String s, Charset charset) {
    dst.writeCharSequence(s, charset);
    dst.writeByte(0);
  }

  public static void writeCString(ByteBuf dst, ByteBuf buf) {
    // Important : won't not change data index
    dst.writeBytes(buf, buf.readerIndex(), buf.readableBytes());
    dst.writeByte(0);
  }

  public static void writeCStringUTF8(ByteBuf dst, String s) {
    dst.writeCharSequence(s, UTF_8);
    dst.writeByte(0);
  }

  public static void writeCString(ByteBuf dst, byte[] bytes) {
    dst.writeBytes(bytes, 0, bytes.length);
    dst.writeByte(0);
  }

  public static byte[][] paramValues(List<Object> parameters) {
    if(parameters.size() > 100)
      throw new IllegalStateException("params size must be <= 100");
    byte[][] params = new byte[parameters.size()][];
    for (int c = 0; c < parameters.size(); ++c) {
      Object param = parameters.get(c);
      if(param == null) {
        params[c] = null;
      } else if (param.getClass() == Boolean.class) {
        params[c] = (boolean) param ? new byte[]{'t'} : new byte[]{'f'};
      } else if (param.getClass() == byte[].class) {
        params[c] = ("\\x" + printHexBinary((byte[])param)).getBytes(UTF_8);
      } else if (
        param.getClass() == Character.class ||
        param.getClass() == String.class ||
        param.getClass() == Short.class ||
        param.getClass() == Integer.class ||
        param.getClass() == Long.class ||
        param.getClass() == BigInteger.class ||
        param.getClass() == Float.class ||
        param.getClass() == Double.class ||
        param.getClass() == BigDecimal.class ||
        param.getClass() == LocalDate.class ||
        param.getClass() == LocalTime.class ||
        param.getClass() == OffsetTime.class ||
        param.getClass() == LocalDateTime.class ||
        param.getClass() == OffsetDateTime.class ||
        param.getClass() == Instant.class ||
        param.getClass() == JsonObject.class ||
        param.getClass() == JsonArray.class ||
        param.getClass() == UUID.class
        ) {
        params[c] =  parameters.get(c).toString().getBytes(UTF_8);
      } else {
        throw new UnsupportedOperationException(param.getClass() + " is not supported");
      }
    }
    return params;
  }

}
