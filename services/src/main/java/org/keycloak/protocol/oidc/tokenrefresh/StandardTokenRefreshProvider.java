/*
 * Copyright 2025 Red Hat, Inc. and/or its affiliates
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
 *
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.keycloak.protocol.oidc.tokenrefresh;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.keycloak.OAuthErrorException;
import org.keycloak.events.Details;
import org.keycloak.events.Errors;
import org.keycloak.models.AuthenticatedClientSessionModel;
import org.keycloak.models.UserSessionModel;
import org.keycloak.protocol.oidc.OIDCAdvancedConfigWrapper;
import org.keycloak.protocol.oidc.TokenManager;
import org.keycloak.protocol.oidc.TokenRefreshContext;
import org.keycloak.protocol.oidc.TokenRefreshProvider;
import org.keycloak.protocol.oidc.grants.OAuth2GrantType;
import org.keycloak.protocol.oidc.grants.OAuth2GrantTypeBase;
import org.keycloak.protocol.oidc.grants.RefreshTokenGrantType;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.services.CorsErrorResponseException;
import org.keycloak.services.clientpolicy.ClientPolicyException;
import org.keycloak.services.clientpolicy.context.TokenRefreshResponseContext;
import org.keycloak.services.util.MtlsHoKTokenUtil;

import org.jboss.logging.Logger;

/**
 * Default implementation of refresh token provider.
 *
 * @author <a href="mailto:markus.dahm@spree.de">Markus Dahm</a>
 */
public class StandardTokenRefreshProvider implements TokenRefreshProvider {
  private static final Logger logger = Logger.getLogger(RefreshTokenGrantType.class);

  @Override
  public boolean supports(final TokenRefreshContext context) {
    return true;
  }

  /**
   * Code moved from {@link RefreshTokenGrantType#process(OAuth2GrantType.Context)}
   *
   * @param refreshContext token refresh context
   */
  @Override
  public Response refresh(final TokenRefreshContext refreshContext) {
    AccessTokenResponse res;

    try {
      final var tokenManager = (TokenManager) refreshContext.tokenManager();
      final var session = refreshContext.session();

      // KEYCLOAK-6771 Certificate Bound Token
      TokenManager.AccessTokenResponseBuilder responseBuilder = tokenManager.refreshAccessToken(
          session,
          session.getContext().getUri(),
          refreshContext.clientConnection(),
          refreshContext.realm(),
          refreshContext.client(),
          refreshContext.refreshToken(),
          refreshContext.event(),
          refreshContext.headers(),
          refreshContext.request(),
          refreshContext.scopeParameter()
      );

      final var clientConfig = (OIDCAdvancedConfigWrapper) refreshContext.clientConfig();
      final var tokenGrantType = (OAuth2GrantTypeBase) refreshContext.tokenGrantType();
      tokenGrantType.checkAndBindMtlsHoKToken(responseBuilder, clientConfig.isUseRefreshToken());

      session.clientPolicy().triggerOnEvent(new TokenRefreshResponseContext(refreshContext.formParams(), responseBuilder));

      res = responseBuilder.build();

      if (!responseBuilder.isOfflineToken()) {
        UserSessionModel userSession = session.sessions().getUserSession(refreshContext.realm(), res.getSessionState());
        AuthenticatedClientSessionModel clientSession = userSession.getAuthenticatedClientSessionByClient(refreshContext.client().getId());
        tokenGrantType.updateClientSession(clientSession);
        tokenGrantType.updateUserSessionFromClientAuth(userSession);
      }
    } catch (OAuthErrorException e) {
      logger.trace(e.getMessage(), e);

      // KEYCLOAK-6771 Certificate Bound Token
      if (MtlsHoKTokenUtil.CERT_VERIFY_ERROR_DESC.equals(e.getDescription())) {
        refreshContext.event().detail(Details.REASON, e.getDescription());
        refreshContext.event().error(Errors.NOT_ALLOWED);

        throw new CorsErrorResponseException(refreshContext.cors(), e.getError(), e.getDescription(), Response.Status.UNAUTHORIZED);
      } else {
        refreshContext.event().detail(Details.REASON, e.getDescription());
        refreshContext.event().error(Errors.INVALID_TOKEN);

        throw new CorsErrorResponseException(refreshContext.cors(), e.getError(), e.getDescription(), Response.Status.BAD_REQUEST);
      }
    } catch (ClientPolicyException cpe) {
      refreshContext.event().detail(Details.REASON, Details.CLIENT_POLICY_ERROR);
      refreshContext.event().detail(Details.CLIENT_POLICY_ERROR, cpe.getError());
      refreshContext.event().detail(Details.CLIENT_POLICY_ERROR_DETAIL, cpe.getErrorDetail());
      refreshContext.event().error(cpe.getError());

      throw new CorsErrorResponseException(refreshContext.cors(), cpe.getError(), cpe.getErrorDetail(), cpe.getErrorStatus());
    }

    refreshContext.event().success();

    return refreshContext.cors().add(Response.ok(res, MediaType.APPLICATION_JSON_TYPE));
  }

  @Override
  public void close() {
  }
}
