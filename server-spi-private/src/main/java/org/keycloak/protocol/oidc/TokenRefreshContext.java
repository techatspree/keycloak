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
public class TokenRefreshContext {
    private final Object tokenGrantType; // org.keycloak.protocol.oidc.grants.OAuth2GrantTypeBase
    private final KeycloakSession session;
    private final HttpRequest request;
    private final MultivaluedMap<String, String> formParams;
    private final Cors cors;
    private final RealmModel realm;

    private final EventBuilder event;
    private final ClientModel client;
    private final ClientConnection clientConnection;
    private final Object clientConfig; // org.keycloak.protocol.oidc.OIDCAdvancedConfigWrapper
    private final HttpHeaders headers;
    private Object tokenManager; // org.keycloak.protocol.oidc.TokenManager

    private final Map<String, String> clientAuthAttributes;
    private final String scopeParameter;
    private final String refreshToken;

    public TokenRefreshContext(
        final Object tokenGrantType,
        final KeycloakSession session,
        final HttpRequest request,
        final MultivaluedMap<String, String> formParams,
        final Cors cors,
        final RealmModel realm,
        final EventBuilder event,
        final ClientModel client,
        final ClientConnection clientConnection,
        final Object clientConfig,
        final HttpHeaders headers,
        final Object tokenManager,
        final Map<String, String> clientAuthAttributes,
        final String scopeParameter,
        final String refreshToken) {
        this.tokenGrantType = tokenGrantType;
        this.session = session;
        this.request = request;
        this.formParams = formParams;
        this.cors = cors;
        this.realm = realm;
        this.event = event;
        this.client = client;
        this.clientConnection = clientConnection;
        this.clientConfig = clientConfig;
        this.headers = headers;
        this.tokenManager = tokenManager;
        this.clientAuthAttributes = clientAuthAttributes;
        this.scopeParameter = scopeParameter;
        this.refreshToken = refreshToken;
    }

  public Object getTokenGrantType() {
    return tokenGrantType;
  }

  public KeycloakSession getSession() {
    return session;
  }

  public HttpRequest getRequest() {
    return request;
  }

  public MultivaluedMap<String, String> getFormParams() {
    return formParams;
  }

  public Cors getCors() {
    return cors;
  }

  public RealmModel getRealm() {
    return realm;
  }

  public EventBuilder getEvent() {
    return event;
  }

  public ClientModel getClient() {
    return client;
  }

  public ClientConnection getClientConnection() {
    return clientConnection;
  }

  public Object getClientConfig() {
    return clientConfig;
  }

  public HttpHeaders getHeaders() {
    return headers;
  }

  public Object getTokenManager() {
    return tokenManager;
  }

  public void setTokenManager(final Object tokenManager) {
    this.tokenManager = tokenManager;
  }

  public Map<String, String> getClientAuthAttributes() {
    return clientAuthAttributes;
  }

  public String getScopeParameter() {
    return scopeParameter;
  }

  public String getRefreshToken() {
    return refreshToken;
  }
}
