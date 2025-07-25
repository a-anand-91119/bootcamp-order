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
import dev.notyouraverage.base.enums.ResponseStatus;
import java.time.LocalDateTime;
import java.util.List;

public record ResponseWrapper<T>(
        @JsonInclude(JsonInclude.Include.NON_NULL) T data,
        @JsonInclude(JsonInclude.Include.NON_NULL) PaginationResponse pagination,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) List<ErrorResponse> errors,
        MetaResponse meta
) {

    public static <T> ResponseWrapper<T> success(T data) {
        return success(data, null);
    }

    public static <T> ResponseWrapper<T> success(T data, PaginationResponse pagination) {
        MetaResponse metaResponse = MetaResponse.builder()
                .status(ResponseStatus.SUCCESS)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseWrapper<>(data, pagination, null, metaResponse);
    }

    public static <T> ResponseWrapper<T> failure(List<ErrorResponse> errorResponses) {
        MetaResponse metaResponse = MetaResponse.builder()
                .status(ResponseStatus.FAILURE)
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseWrapper<>(null, null, errorResponses, metaResponse);
    }

}
