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
package dev.notyouraverage.base.filters;

import dev.notyouraverage.base.configurations.RequestContext;
import dev.notyouraverage.base.constants.Constants;
import dev.notyouraverage.base.constants.FilterOrderingConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(value = FilterOrderingConstants.HTTP_LOGGING_FILTER_ORDER)
@Slf4j
public class HttpLoggingFilter extends OncePerRequestFilter {
    private static final Set<String> ignoredPaths = Set.of("/health");

    private final RequestContext requestContext;

    public HttpLoggingFilter(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    private static boolean shouldLogRequest(HttpServletRequest request) {
        return !ignoredPaths.contains(request.getRequestURI());
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try (MDC.MDCCloseable ignored = MDC.putCloseable(Constants.REQUEST_ID, requestContext.getRequestId())) {
            filterChain.doFilter(request, response);
            if (shouldLogRequest(request)) {
                log.info("Request to {}: {} - {}", request.getMethod(), formURI(request), response.getStatus());
            }
        }
    }

    private String formURI(HttpServletRequest request) {
        String queryString = request.getQueryString() == null ? "" : "?" + request.getQueryString();
        return request.getRequestURI() + queryString;
    }
}
