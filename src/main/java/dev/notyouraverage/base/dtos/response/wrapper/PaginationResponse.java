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

import lombok.Builder;
import org.springframework.data.domain.Page;

@Builder
public record PaginationResponse(
        Integer pageNumber, Integer pageSize, Long totalElements,
        Integer totalPages
) {
    public static <T> PaginationResponse from(Page<T> page) {
        return PaginationResponse.builder()
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .pageNumber(page.getNumber() + 1)
                .pageSize(page.getSize())
                .build();
    }
}
