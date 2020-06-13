package nl.overheid.aerius.service.domain;

public enum ServiceSelectorNameEnum {
  HABITAT_TYPE("habitatTypeCode"),

  GOAL_HABITAT_TYPE("goalHabitatTypeCode"),

  SPECIES("speciesCode"),

  REHABILITATION_STRATEGY("rehabilitationStrategyCode"),

  NATURA_2000_AREA("natura2000AreaCode"),

  YEAR("year"),

  COUNTRY("countryCode"),

  SECTOR("sectorCode"),

  SECTORGROUP("sectorgroupCode"),

  OTHER_DEPOSITION_CODE("otherDepositionType"),

  COMPARING_DATASET("comparingDataset"),

  SEGMENT_REGISTER("segmentRegister"),

  SEGMENT_MONITOR("segmentMonitor");

  private final String name;

  private ServiceSelectorNameEnum(final String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}