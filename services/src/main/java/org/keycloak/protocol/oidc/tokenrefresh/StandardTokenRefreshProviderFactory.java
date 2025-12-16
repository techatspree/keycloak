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

import org.keycloak.Config;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.protocol.oidc.TokenRefreshProvider;
import org.keycloak.protocol.oidc.TokenRefreshProviderFactory;

/**
 * Provider factory for token refresh, which is compliant with the <a href="https://datatracker.ietf.org/doc/html/rfc6749#section-6">OAuth 2.0 Refresh Token Grant</a>
 *
 * @author <a href="mailto:markus.dahm@spree.de">Markus Dahm</a>
 */
public class StandardTokenRefreshProviderFactory implements TokenRefreshProviderFactory {
  @Override
  public TokenRefreshProvider create(KeycloakSession session) {
    return new StandardTokenRefreshProvider();
  }

  @Override
  public void init(Config.Scope config) {
  }

  @Override
  public void postInit(KeycloakSessionFactory factory) {
  }

  @Override
  public void close() {
  }

  @Override
  public String getId() {
    return "standard-refresh";
  }
}
