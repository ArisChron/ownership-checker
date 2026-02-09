package io.decision.ownershipchecker.domain;

import jakarta.validation.constraints.NotBlank;

public class DecisionInput {

    @NotBlank(message = "Please describe the decision (1–5 sentences).")
    private String description;

    private String team;
    private String system;


    private int reversibility = 3;  // 3=Hard,2=Medium,1=Easy
    private int horizon = 2;        // 3=Quarters+,2=Weeks,1=Days
    private int blast = 2;          // 3=Company,2=Multi-team,1=Single
    private int failureCost = 2;    // 3=High,2=Medium,1=Low
    private int frequency = 2;      // 3=Systemic,2=Occasional,1=One-off
    private int customerImpact = 2; // 3=Customers,2=Engineers/Leadership

    private boolean roastEnabled = true;
    private String tone = "serious"; // "serious" | "spicy"

    // getters/setters
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getTeam() { return team; }
    public void setTeam(String team) { this.team = team; }

    public String getSystem() { return system; }
    public void setSystem(String system) { this.system = system; }

    public int getReversibility() { return reversibility; }
    public void setReversibility(int reversibility) { this.reversibility = reversibility; }

    public int getHorizon() { return horizon; }
    public void setHorizon(int horizon) { this.horizon = horizon; }

    public int getBlast() { return blast; }
    public void setBlast(int blast) { this.blast = blast; }

    public int getFailureCost() { return failureCost; }
    public void setFailureCost(int failureCost) { this.failureCost = failureCost; }

    public int getFrequency() { return frequency; }
    public void setFrequency(int frequency) { this.frequency = frequency; }

    public int getCustomerImpact() { return customerImpact; }
    public void setCustomerImpact(int customerImpact) { this.customerImpact = customerImpact; }

    public boolean isRoastEnabled() { return roastEnabled; }
    public void setRoastEnabled(boolean roastEnabled) { this.roastEnabled = roastEnabled; }

    public String getTone() { return tone; }
    public void setTone(String tone) { this.tone = tone; }
}
