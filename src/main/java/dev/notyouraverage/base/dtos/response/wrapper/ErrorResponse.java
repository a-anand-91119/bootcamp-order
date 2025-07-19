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
package dev.notyouraverage.base.dtos.response.wrapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record ErrorResponse(
        String errorCode,
        String message,
        @JsonInclude(JsonInclude.Include.NON_NULL) String detail
) implements Serializable {
    public static ErrorResponse from(ErrorCodeTrait errorCodeTrait) {
        return ErrorResponse.builder()
                .errorCode(errorCodeTrait.getCode())
                .message(errorCodeTrait.getMessage())
                .build();
    }

    public static ErrorResponse from(ErrorCodeTrait errorCodeTrait, String details) {
        return ErrorResponse.builder()
                .errorCode(errorCodeTrait.getCode())
                .message(errorCodeTrait.getMessage())
                .detail(details)
                .build();
    }

    @Override
    public String toString() {
        return "%s :: %s :: %s".formatted(errorCode, message, detail);
    }
}
