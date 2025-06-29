// ASM: a very small and fast Java bytecode manipulation framework
// Copyright (c) 2000-2011 INRIA, France Telecom
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
// 1. Redistributions of source code must retain the above copyright
//    notice, this list of conditions and the following disclaimer.
// 2. Redistributions in binary form must reproduce the above copyright
//    notice, this list of conditions and the following disclaimer in the
//    documentation and/or other materials provided with the distribution.
// 3. Neither the name of the copyright holders nor the names of its
//    contributors may be used to endorse or promote products derived from
//    this software without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
// THE POSSIBILITY OF SUCH DAMAGE.
package org.babyfish.jimmer.impl.asm.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility methods to convert an array of primitive or object values to a mutable ArrayList, not
 * baked by the array (unlike {@link java.util.Arrays#asList}).
 *
 * @author Eric Bruneton
 */
final class Util {

  private Util() {}

  static <T> List<T> add(final List<T> list, final T element) {
    List<T> newList = list == null ? new ArrayList<>(1) : list;
    newList.add(element);
    return newList;
  }

  static <T> List<T> asArrayList(final int length) {
    List<T> list = new ArrayList<>(length);
    for (int i = 0; i < length; ++i) {
      list.add(null);
    }
    return list;
  }

  static <T> List<T> asArrayList(final T[] array) {
    if (array == null) {
      return new ArrayList<>();
    }
    ArrayList<T> list = new ArrayList<>(array.length);
    for (T t : array) {
      list.add(t); // NOPMD(UseArraysAsList): we want a modifiable list.
    }
    return list;
  }

  static List<Byte> asArrayList(final byte[] byteArray) {
    if (byteArray == null) {
      return new ArrayList<>();
    }
    ArrayList<Byte> byteList = new ArrayList<>(byteArray.length);
    for (byte b : byteArray) {
      byteList.add(b); // NOPMD(UseArraysAsList): we want a modifiable list.
    }
    return byteList;
  }

  static List<Boolean> asArrayList(final boolean[] booleanArray) {
    if (booleanArray == null) {
      return new ArrayList<>();
    }
    ArrayList<Boolean> booleanList = new ArrayList<>(booleanArray.length);
    for (boolean b : booleanArray) {
      booleanList.add(b); // NOPMD(UseArraysAsList): we want a modifiable list.
    }
    return booleanList;
  }

  static List<Short> asArrayList(final short[] shortArray) {
    if (shortArray == null) {
      return new ArrayList<>();
    }
    ArrayList<Short> shortList = new ArrayList<>(shortArray.length);
    for (short s : shortArray) {
      shortList.add(s); // NOPMD(UseArraysAsList): we want a modifiable list.
    }
    return shortList;
  }

  static List<Character> asArrayList(final char[] charArray) {
    if (charArray == null) {
      return new ArrayList<>();
    }
    ArrayList<Character> charList = new ArrayList<>(charArray.length);
    for (char c : charArray) {
      charList.add(c); // NOPMD(UseArraysAsList): we want a modifiable list.
    }
    return charList;
  }

  static List<Integer> asArrayList(final int[] intArray) {
    if (intArray == null) {
      return new ArrayList<>();
    }
    ArrayList<Integer> intList = new ArrayList<>(intArray.length);
    for (int i : intArray) {
      intList.add(i); // NOPMD(UseArraysAsList): we want a modifiable list.
    }
    return intList;
  }

  static List<Float> asArrayList(final float[] floatArray) {
    if (floatArray == null) {
      return new ArrayList<>();
    }
    ArrayList<Float> floatList = new ArrayList<>(floatArray.length);
    for (float f : floatArray) {
      floatList.add(f); // NOPMD(UseArraysAsList): we want a modifiable list.
    }
    return floatList;
  }

  static List<Long> asArrayList(final long[] longArray) {
    if (longArray == null) {
      return new ArrayList<>();
    }
    ArrayList<Long> longList = new ArrayList<>(longArray.length);
    for (long l : longArray) {
      longList.add(l); // NOPMD(UseArraysAsList): we want a modifiable list.
    }
    return longList;
  }

  static List<Double> asArrayList(final double[] doubleArray) {
    if (doubleArray == null) {
      return new ArrayList<>();
    }
    ArrayList<Double> doubleList = new ArrayList<>(doubleArray.length);
    for (double d : doubleArray) {
      doubleList.add(d); // NOPMD(UseArraysAsList): we want a modifiable list.
    }
    return doubleList;
  }

  static <T> List<T> asArrayList(final int length, final T[] array) {
    List<T> list = new ArrayList<>(length);
    for (int i = 0; i < length; ++i) {
      list.add(array[i]); // NOPMD(UseArraysAsList): we convert a part of the array.
    }
    return list;
  }
}
