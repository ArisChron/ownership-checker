package io.decision.ownershipchecker.domain;

import java.util.List;

public class CheckResult {
    private Domain domain;
    private int confidencePct;

    private String owner;
    private String contributors;

    private String risk;     // Low/Medium/High
    private String riskWhy;

    private List<String> aiCan;
    private List<String> humanMust;

    private String ownershipCard;
    private String roast;

    private String footerTrust;

    public Domain getDomain() { return domain; }
    public void setDomain(Domain domain) { this.domain = domain; }

    public int getConfidencePct() { return confidencePct; }
    public void setConfidencePct(int confidencePct) { this.confidencePct = confidencePct; }

    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }

    public String getContributors() { return contributors; }
    public void setContributors(String contributors) { this.contributors = contributors; }

    public String getRisk() { return risk; }
    public void setRisk(String risk) { this.risk = risk; }

    public String getRiskWhy() { return riskWhy; }
    public void setRiskWhy(String riskWhy) { this.riskWhy = riskWhy; }

    public List<String> getAiCan() { return aiCan; }
    public void setAiCan(List<String> aiCan) { this.aiCan = aiCan; }

    public List<String> getHumanMust() { return humanMust; }
    public void setHumanMust(List<String> humanMust) { this.humanMust = humanMust; }

    public String getOwnershipCard() { return ownershipCard; }
    public void setOwnershipCard(String ownershipCard) { this.ownershipCard = ownershipCard; }

    public String getRoast() { return roast; }
    public void setRoast(String roast) { this.roast = roast; }

    public String getFooterTrust() { return footerTrust; }
    public void setFooterTrust(String footerTrust) { this.footerTrust = footerTrust; }
}
