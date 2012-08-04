/*
 * Copyright (C) 2011 Google, Inc.
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
package org.junit.contrib.truth.subjects;

import java.util.Arrays;
import java.util.Iterator;

import org.junit.contrib.truth.FailureStrategy;
import org.junit.contrib.truth.util.GwtCompatible;

/**
 * @author Kevin Bourrillion
 */
@GwtCompatible
public class IterableSubject<S extends IterableSubject<S, T, C>, T, C extends Iterable<T>> extends Subject<S, C> {

  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static <T, C extends Iterable<T>> IterableSubject<? extends IterableSubject<?, T, C>, T, C> create(
      FailureStrategy failureStrategy, Iterable<T> list) {
    return new IterableSubject(failureStrategy, list);
  }

  // TODO: Arguably this should even be package private
  protected IterableSubject(FailureStrategy failureStrategy, C list) {
    super(failureStrategy, list);
  }

  /**
   * @deprecated - please pass your iterable into a collection first.
   */
  @Deprecated
  public And<S> contains(Object item) {
    for (Object t : getSubject()) {
      if (item == t || item != null && item.equals(t)) {
        return nextChain();
      }
    }
    fail("contains", item);
    throw new AssertionError(); // needed for "EXPECT" behaviour.
  }

  /**
   * Attests that the subject holds no more objects, or fails.
   */
  public And<S> isEmpty() {
    if (getSubject().iterator().hasNext()) {
      fail("is empty");
    }
    return nextChain();
  }

  /**
   * Attests that the subject holds one or more objects, or fails
   */
  public And<S> isNotEmpty() {
    if (!getSubject().iterator().hasNext()) {
      fail("is not empty");
    }
    return nextChain();
  }

  /**
   * Asserts that the items are supplied in the order given by the iterable. For
   * Collections and other things which contain items but may not have guaranteed
   * iteration order, this method should be overridden.
   */
  public And<S> iteratesOverSequence(Object... expectedItems) {
    Iterator<T> actualItems = getSubject().iterator();
    for (Object expected : expectedItems) {
      if (!actualItems.hasNext()) {
        fail("iterates through", Arrays.asList(expectedItems));
      } else {
        Object actual = actualItems.next();
        if (actual == expected || actual != null && actual.equals(expected)) {
          continue;
        } else {
          fail("iterates through", Arrays.asList(expectedItems));
        }
      }
    }
    if (actualItems.hasNext()) {
      fail("iterates through", Arrays.asList(expectedItems));
    }
    return nextChain();
  }
}
