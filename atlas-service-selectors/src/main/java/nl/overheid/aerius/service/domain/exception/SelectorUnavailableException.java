package nl.overheid.aerius.service.domain.exception;

public class SelectorUnavailableException extends Exception {
  private static final long serialVersionUID = 1L;

  public SelectorUnavailableException(final String message) {
    super(message);
  }
}
