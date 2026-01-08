/*
 *  Copyright 2021 Red Hat, Inc. and/or its affiliates
 *  and other contributors as indicated by the @author tags.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.keycloak.protocol.oidc;

import jakarta.ws.rs.core.Response;

import org.keycloak.provider.Provider;

/**
 * Provides token refresh mechanism for supported tokens. Analogous to {@link TokenExchangeProvider}.
 *
 * @author <a href="mailto:markus.dahm@spree.de">Markus Dahm</a>
 */
public interface TokenRefreshProvider extends Provider {
    /**
     * Check if refresh request is supported by this provider
     *
     * @param context token refresh context
     * @return true if the request is supported
     */
    boolean supports(TokenRefreshContext context);

    /**
     * Refresh the <code>token</code>.
     *
     * @param context token refresh context
     * @return response with a new token
     */
    Response refresh(TokenRefreshContext context);
}
