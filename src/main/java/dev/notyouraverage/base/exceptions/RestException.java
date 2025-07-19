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
package dev.notyouraverage.base.exceptions;

import dev.notyouraverage.base.dtos.response.wrapper.ErrorResponse;
import java.util.List;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RestException extends AppException {
    private final HttpStatus httpStatus;

    private final List<ErrorResponse> errorResponses;

    public RestException(HttpStatus httpStatus, ErrorResponse errorResponse) {
        this(httpStatus, errorResponse, null);
    }

    public RestException(HttpStatus httpStatus, ErrorResponse errorResponse, Throwable cause) {
        this(httpStatus, List.of(errorResponse), cause);
    }

    public RestException(HttpStatus httpStatus, List<ErrorResponse> errorResponses) {
        this(httpStatus, errorResponses, null);
    }

    public RestException(HttpStatus httpStatus, List<ErrorResponse> errorResponses, Throwable cause) {
        super(cause);
        this.httpStatus = httpStatus;
        this.errorResponses = errorResponses;
    }
}
