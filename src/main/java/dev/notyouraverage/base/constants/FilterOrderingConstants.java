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
package dev.notyouraverage.base.constants;

import org.springframework.core.Ordered;

public class FilterOrderingConstants {

    public static final int CORS_FILTER_ORDER = Ordered.HIGHEST_PRECEDENCE;

    public static final int REQUEST_METADATA_INITIALIZER_FILTER_ORDER = Ordered.HIGHEST_PRECEDENCE + 1;

    public static final int HTTP_LOGGING_FILTER_ORDER = Ordered.HIGHEST_PRECEDENCE + 2;

    private FilterOrderingConstants() {
    }
}
