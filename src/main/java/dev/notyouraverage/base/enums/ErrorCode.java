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
package dev.notyouraverage.base.enums;

import dev.notyouraverage.base.constants.Constants;
import dev.notyouraverage.base.dtos.response.wrapper.ErrorCodeTrait;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum ErrorCode implements ErrorCodeTrait {
    INTERNAL_SERVER_ERROR("001", "Internal Server Error"),
    INPUT_VALIDATION_ERROR("002", "Input Validation Error"),
    NO_HANDLER_FOUND("003", "No Handler Found"),
    FORBIDDEN("004", "Forbidden to access the resource"),
    UNAUTHORIZED("005", "Unauthorized access"),
    ;

    private final String code;

    private final String message;

    ErrorCode(String code, String message) {
        this.code = Constants.BASE_ERROR_CODE_PREFIX + code;
        this.message = message;
    }
}
