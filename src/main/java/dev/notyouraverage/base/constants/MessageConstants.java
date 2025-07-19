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

import lombok.experimental.UtilityClass;

@UtilityClass
public class MessageConstants {

    public static final String MUST_BE_NULL_OR_MUST_NOT_BE_BLANK = "must be null or must not be blank";

    public static final String GLOBAL_EXCEPTION_HANDLER_CAPTURE_MESSAGE = "Exception of type `%s` captured in the GlobalExceptionHandler";

    public static final String FIELD_VALIDATION_ERROR_MESSAGE_FORMAT = "%s: %s";

    public static final String ENUM_INVALID_CAST_MESSAGE = "%s: must be among %s";

    public static final String REQUIRED_REQUEST_BODY_IS_MISSING = "Required request body is missing";

    public static final String UNSUPPORTED_MEDIA_TYPE_MESSAGE = "MediaType `%s` is not supported. MediaType must be among %s";

    public static final String PASSWORD_IS_COMPROMISED = "The password you have provided is compromised. Please try a stronger password.";

    public static final String DUPLICATE_VALUE_FOUND_IN_ENUM = "Duplicate value `%s` found in enum `%s`";

    public static final String THIS_METHOD_MUST_BE_INVOKED_WITH_AN_ENUM_CLASS = "This method must be invoked with an Enum class";

    public static final String NO_HANDLER_FOUND_FOR_THE_REQUESTED_PATH = "No handler found for the requested path: `%s`";

    public static final String ERROR_PARSING_THE_REQUEST_BODY = "Error parsing the request body";

    public static final String INVALID_FIELD_TYPE_ASSIGNMENT = "%s: Cannot assign value `%s` to a field of type `%s`";

    public static final String TIMESTAMP_MUST_BE_OF_FORMAT = "%s: must be of format %s";

    public static final String INVALID_FORMAT = "Invalid format";

    public static final String SCHEDULED_JOB_START_MESSAGE = "=== {} STARTED ===";
}
