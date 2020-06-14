package nl.overheid.aerius.wui.service.json;

import com.google.gwt.json.client.JSONValue;

public class JSONValueHandle {
  private final JSONValue inner;

  public JSONValueHandle(final JSONValue inner) {
    this.inner = inner;
  }

  public JSONValue getInner() {
    return inner;
  }

  public boolean isString() {
    return inner.isString() != null;
  }

  public String asString() {
    return inner.isString().stringValue();
  }

  public Integer asInteger() {
    return (int) inner.isNumber().doubleValue();
  }

  public Double asNumber() {
    return inner.isNumber().doubleValue();
  }

  public boolean isObject() {
    return inner.isObject() != null;
  }

  public JSONObjectHandle asObjectHandle() {
    return new JSONObjectHandle(inner.isObject());
  }

  public boolean isArray() {
    return inner.isArray() != null;
  }

  public JSONArrayHandle asArray() {
    return new JSONArrayHandle(inner.isArray());
  }

  public boolean isNumber() {
    return inner.isNumber() != null;
  }

  public boolean isBoolean() {
    return inner.isBoolean() != null;
  }

  public boolean isNull() {
    return inner.isNull() != null;
  }
}
