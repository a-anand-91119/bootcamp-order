/*
 * Copyright (C) 2025 NotYourAverageDev
 *
 * Licensed under the Apache License, version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.notyouraverage.commons.utils;

import java.util.function.Predicate;
import java.util.stream.Stream;
import lombok.experimental.UtilityClass;

/**
 * Utility class providing helper methods for working with enums.
 */
@UtilityClass
public class EnumUtils {

    /**
     * Returns the first enum constant of the specified {@code enumClass} that
     * matches the given {@code predicate}.
     *
     * @param enumClass the enum class to search
     * @param predicate the condition to match
     * @return the first enum constant that matches the predicate
     * @throws IllegalArgumentException if no matching enum constant is found
     * @example
     *
     *          <pre>
     *          {
     *              @code
     *              enum Status {
     *                  ACTIVE,
     *                  INACTIVE
     *              }
     *              Status statusEnum = EnumUtils.of(Status.class, s -> s.name().startsWith("A")); // returns Status.ACTIVE
     *          }
     *          </pre>
     */
    public static <T extends Enum<T>> T of(Class<T> enumClass, Predicate<T> predicate) {
        return Stream.of(enumClass.getEnumConstants())
                .filter(predicate)
                .findFirst()
                .orElseThrow(
                        () -> new IllegalArgumentException("No matching enum found in " + enumClass.getSimpleName())
                );
    }

    public static <T extends Enum<T>> T of(Class<T> enumClass, Predicate<T> predicate, T defaultValue) {
        return Stream.of(enumClass.getEnumConstants())
                .filter(predicate)
                .findFirst()
                .orElse(defaultValue);
    }
}
