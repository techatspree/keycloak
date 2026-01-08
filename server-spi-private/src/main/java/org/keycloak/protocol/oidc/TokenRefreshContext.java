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
 *
 * @param tokenGrantType org.keycloak.protocol.oidc.grants.OAuth2GrantTypeBase
 * @param clientConfig   org.keycloak.protocol.oidc.OIDCAdvancedConfigWrapper
 * @param tokenManager   org.keycloak.protocol.oidc.TokenManager
 */
public record TokenRefreshContext(Object tokenGrantType, KeycloakSession session, HttpRequest request, MultivaluedMap<String, String> formParams,
                                  Cors cors, RealmModel realm, EventBuilder event, ClientModel client, ClientConnection clientConnection,
                                  Object clientConfig, HttpHeaders headers, Object tokenManager, Map<String, String> clientAuthAttributes,
                                  String scopeParameter, String refreshToken) {
}
