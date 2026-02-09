package io.decision.ownershipchecker.domain;

public enum Domain {
  STRATEGIC("Strategic"),
  ARCHITECTURAL("Architectural"),
  DELIVERY("Delivery"),
  OPERATIONAL("Operational");

  private final String label;
  Domain(String label) { this.label = label; }
  public String label() { return label; }
}