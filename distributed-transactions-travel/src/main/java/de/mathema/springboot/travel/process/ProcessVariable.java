package de.mathema.springboot.travel.process;

public enum ProcessVariable {
  TRAVEL_BOOKING_ID("travelBookingId"),
  CREATED("created");

  private final String variablenname;

  ProcessVariable(final String variablenname) {
    this.variablenname = variablenname;
  }

  public String getVariablenname() {
    return variablenname;
  }
}
