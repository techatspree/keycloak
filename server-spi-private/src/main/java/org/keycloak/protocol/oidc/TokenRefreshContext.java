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

import java.util.Map;

import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MultivaluedMap;
import org.keycloak.http.HttpRequest;

import org.keycloak.common.ClientConnection;
import org.keycloak.events.EventBuilder;
import org.keycloak.models.ClientModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.services.cors.Cors;

/**
 * Token refresh context
 *
 * @author <a href="mailto:markus.dahm@spree.de">Markus Dahm</a>
 */
public record TokenRefreshContext(
    Object tokenGrantType, // org.keycloak.protocol.oidc.grants.OAuth2GrantTypeBase
    KeycloakSession session,
    HttpRequest request,
    MultivaluedMap<String, String> formParams,
    Cors cors,
    RealmModel realm,

    EventBuilder event,
    ClientModel client,
    ClientConnection clientConnection,
    Object clientConfig, // org.keycloak.protocol.oidc.OIDCAdvancedConfigWrapper
    HttpHeaders headers,
    Object tokenManager, // org.keycloak.protocol.oidc.TokenManager

    Map<String, String> clientAuthAttributes,
    String scopeParameter,
    String refreshToken) {
}
