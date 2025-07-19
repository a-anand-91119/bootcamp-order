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
package dev.notyouraverage.base.controllers;

import dev.notyouraverage.base.constants.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {

    @RequestMapping(method = { RequestMethod.GET, RequestMethod.HEAD })
    @Operation(summary = "Health check API", description = "API returns OK while the system is healthy and is ready to accept incoming requests.", tags = {
            "Health Check" }, operationId = "health", responses = @ApiResponse(responseCode = "200", description = "Service is ready for accepting incoming requests"))
    public ResponseEntity<String> health() {
        return ResponseEntity.ok(Constants.OK);
    }

}
