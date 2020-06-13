package nl.overheid.aerius.wui.domain.auth;

public class AuthorizationInfo {
  private String tokenType;
  private long expiresIn;
  private String accessToken;
  private String refreshToken;

  public void setTokenType(final String tokenType) {
    this.tokenType = tokenType;
  }

  public void setExpiresIn(final long expiresIn) {
    this.expiresIn = expiresIn;
  }

  public void setAccessToken(final String accessToken) {
    this.accessToken = accessToken;
  }

  public void setRefreshToken(final String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public String getTokenType() {
    return tokenType;
  }

  /**
   * Get the expiration time, in seconds
   */
  public long getExpiresIn() {
    return expiresIn;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  @Override
  public String toString() {
    return "AuthorizationInfo [tokenType=" + tokenType + ", expiresIn=" + expiresIn + "]";
  }
}
