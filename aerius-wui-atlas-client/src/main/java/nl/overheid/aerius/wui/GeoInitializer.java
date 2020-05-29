package nl.overheid.aerius.wui;

import com.google.inject.ImplementedBy;

@ImplementedBy(GeoInitializerNoOp.class)
public interface GeoInitializer {}
