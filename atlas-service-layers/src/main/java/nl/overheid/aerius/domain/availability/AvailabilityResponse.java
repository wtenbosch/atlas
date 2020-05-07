package nl.overheid.aerius.domain.availability;

public class AvailabilityResponse {
  private boolean result;

  public AvailabilityResponse() {}

  public AvailabilityResponse(final boolean result) {
    this.result = result;
  }

  public boolean getResult() {
    return result;
  }

  public void setResult(final boolean result) {
    this.result = result;
  }
}
