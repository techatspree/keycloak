/*
 * Copyright 2024 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.keycloak.protocol.oidc.grants;

import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.core.Response;

import org.keycloak.OAuthErrorException;
import org.keycloak.events.Details;
import org.keycloak.events.EventType;
import org.keycloak.protocol.oidc.TokenRefreshContext;
import org.keycloak.protocol.oidc.TokenRefreshProvider;
import org.keycloak.services.CorsErrorResponseException;
import org.keycloak.services.clientpolicy.ClientPolicyException;
import org.keycloak.services.clientpolicy.context.TokenRefreshClientPolicyContext;

import static org.keycloak.OAuth2Constants.REFRESH_TOKEN;

/**
 * <a href="https://datatracker.ietf.org/doc/html/rfc6749#section-6">OAuth 2.0 Refresh Token Grant</a>
 *
 * @author <a href="mailto:demetrio@carretti.pro">Dmitry Telegin</a> (et al.)
 */
public class RefreshTokenGrantType extends OAuth2GrantTypeBase {
  @Override
  public Response process(Context context) {
    setContext(context);

    String refreshToken = formParams.getFirst(REFRESH_TOKEN);
    event.detail(Details.AUTH_METHOD, REFRESH_TOKEN);
    event.client(client);

    if (refreshToken == null) {
      throw new CorsErrorResponseException(cors, OAuthErrorException.INVALID_REQUEST, "No refresh token", Response.Status.BAD_REQUEST);
    }

    final String scopeParameter = getRequestedScopes();
    final TokenRefreshContext refreshContext = new TokenRefreshContext(this,
        session, request, formParams, cors, realm, event, client, clientConnection, clientConfig, headers, tokenManager, clientAuthAttributes,
        scopeParameter, refreshToken
    );
    final TokenRefreshProvider tokenRefreshProvider = session.getKeycloakSessionFactory()
        .getProviderFactoriesStream(TokenRefreshProvider.class)
        .sorted((f1, f2) -> f2.order() - f1.order())
        .map(f -> session.getProvider(TokenRefreshProvider.class, f.getId()))
        .filter(p -> p.supports(refreshContext))
        .findFirst()
        .orElseThrow(() -> new InternalServerErrorException("No token refresh provider available"));

    try {
      session.clientPolicy().triggerOnEvent(new TokenRefreshClientPolicyContext(formParams, client));
    } catch (ClientPolicyException cpe) {
      event.detail(Details.REASON, Details.CLIENT_POLICY_ERROR);
      event.detail(Details.CLIENT_POLICY_ERROR, cpe.getError());
      event.detail(Details.CLIENT_POLICY_ERROR_DETAIL, cpe.getErrorDetail());
      event.error(cpe.getError());
      throw new CorsErrorResponseException(cors, cpe.getError(), cpe.getErrorDetail(), cpe.getErrorStatus());
    }

    return tokenRefreshProvider.refresh(refreshContext);
  }

  @Override
  public EventType getEventType() {
    return EventType.REFRESH_TOKEN;
  }

}
