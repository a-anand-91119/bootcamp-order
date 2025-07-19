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

public class HeaderConstants {

    public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";

    public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";

    public static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";

    public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";

    public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";

    public static final String ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";

    public static final String CACHE_CONTROL = "Cache-Control";

    public static final String CONTENT_SECURITY_POLICY = "Content-Security-Policy";

    public static final String CROSS_ORIGIN_EMBEDDER_POLICY = "Cross-Origin-Embedder-Policy";

    public static final String CROSS_ORIGIN_OPENER_POLICY = "Cross-Origin-Opener-Policy";

    public static final String CROSS_ORIGIN_RESOURCE_POLICY = "Cross-Origin-Resource-Policy";

    public static final String ORIGIN = "Origin";

    public static final String PERMISSIONS_POLICY = "Permissions-Policy";

    public static final String PRAGMA = "Pragma";

    public static final String REFERRER_POLICY = "Referrer-Policy";

    public static final String STRICT_TRANSPORT_SECURITY = "Strict-Transport-Security";

    public static final String X_CONTENT_TYPE_OPTIONS = "X-Content-Type-Options";

    public static final String X_FRAME_OPTIONS = "X-Frame-Options";

    public static final String X_PERMITTED_CROSS_DOMAIN_POLICIES = "X-Permitted-Cross-Domain-Policies";

    public static final String PERMISSIONS_POLICY_VALUE = "accelerometer=(),ambient-light-sensor=(),autoplay=(),battery=(),camera=(),display-capture=(),"
            + "document-domain=(),encrypted-media=(),fullscreen=(),gamepad=(),geolocation=(),gyroscope=(),"
            + "layout-animations=(self),legacy-image-formats=(self),magnetometer=(),microphone=(),midi=(),"
            + "oversized-images=(self),payment=(),picture-in-picture=(),publickey-credentials-get=(),"
            + "speaker-selection=(),sync-xhr=(self),unoptimized-images=(self),unsized-media=(self),usb=(),"
            + "screen-wake-lock=(),web-share=(),xr-spatial-tracking=()";

    private HeaderConstants() {
    }
}
