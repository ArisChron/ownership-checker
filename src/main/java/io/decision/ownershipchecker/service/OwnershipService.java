package io.decision.ownershipchecker.service;


import io.decision.ownershipchecker.domain.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OwnershipService {

    public CheckResult check(DecisionInput in) {
        // Domain scoring (transparent & deterministic)
        double strategic = in.getHorizon() + in.getBlast() + in.getFailureCost();
        double architectural = in.getReversibility() + in.getBlast() + in.getFrequency();
        double delivery = in.getHorizon() + in.getFrequency() + in.getCustomerImpact();
        double operational = (4 - in.getHorizon()) + (4 - in.getBlast()) + (4 - in.getReversibility()) + (in.getFailureCost() * 0.5);

        var top = maxOf(strategic, architectural, delivery, operational);
        var second = secondOf(strategic, architectural, delivery, operational, top.domain());

        int confidence = computeConfidencePct(top, second);

        Domain domain = top.domain();

        var result = new CheckResult();
        result.setDomain(domain);
        result.setConfidencePct(confidence);

        // Owner mapping
        switch (domain) {
            case STRATEGIC -> {
                result.setOwner("CEO / Exec Team");
                result.setContributors("CTO, Product, Finance");
                result.setAiCan(List.of("Summarize options & scenarios", "Support risk framing", "Draft decision memo"));
                result.setHumanMust(List.of("Final call & accountability", "Budget/value trade-offs", "Policy/strategy approval"));
            }
            case ARCHITECTURAL -> {
                result.setOwner("CTO / Head of Engineering");
                result.setContributors("Platform Team, Security, Tech Leads");
                result.setAiCan(List.of("Compare options & trade-offs", "Draft ADR", "Estimate cost/perf impact"));
                result.setHumanMust(List.of("Final architecture choice", "Risk acceptance", "Cross-team alignment & standards"));
            }
            case DELIVERY -> {
                result.setOwner("Product + Tech Lead (duo)");
                result.setContributors("Design, QA, Stakeholders");
                result.setAiCan(List.of("Highlight dependencies & risks", "Assist estimation", "Draft release notes"));
                result.setHumanMust(List.of("Priority & scope calls", "Commitment decisions", "Cut scope under pressure"));
            }
            case OPERATIONAL -> {
                result.setOwner("Team Lead / On-call owner");
                result.setContributors("Ops/SRE, Support");
                result.setAiCan(List.of("Suggest runbook steps", "Summarize incident timeline", "Draft postmortem sections"));
                result.setHumanMust(List.of("Incident command", "Customer-facing decisions", "Change approvals in prod"));
            }
        }

        // Risk level: “unclear owner” proxy based on high blast + hard to reverse + high cost
        int riskScore = 0;
        if (in.getBlast() == 3) riskScore += 2;
        if (in.getReversibility() == 3) riskScore += 2;
        if (in.getFailureCost() == 3) riskScore += 2;
        if (in.getHorizon() == 3) riskScore += 1;

        String risk = (riskScore >= 5) ? "High" : (riskScore >= 3 ? "Medium" : "Low");
        result.setRisk(risk);

        String riskWhy = buildRiskWhy(in);
        result.setRiskWhy(riskWhy);

        // Ownership card
        result.setOwnershipCard(buildCard(in, result));

        // Roast
        if (in.isRoastEnabled()) {
            result.setRoast(generateRoast(in, result));
        } else {
            result.setRoast("");
        }

        // Trust footer copy
        result.setFooterTrust("Decision support, not decision replacement. No accounts. No personal data. Aggregated usage only.");

        return result;
    }

    private String buildRiskWhy(DecisionInput in) {
        StringBuilder sb = new StringBuilder();
        if (in.getBlast() == 3) sb.append("Company-wide impact");
        else if (in.getBlast() == 2) sb.append("Multi-team impact");
        else sb.append("Single-team impact");

        sb.append(" + ");
        sb.append(in.getReversibility() == 3 ? "hard to reverse" : (in.getReversibility() == 2 ? "moderately reversible" : "easy to reverse"));

        sb.append(" + ");
        sb.append(in.getFailureCost() == 3 ? "high failure cost" : (in.getFailureCost() == 2 ? "medium failure cost" : "low failure cost"));

        return sb.toString();
    }

    private String buildCard(DecisionInput in, CheckResult r) {
        String title = inferTitle(in.getDescription());
        String ctx = "";
        if (notBlank(in.getTeam()) || notBlank(in.getSystem())) {
            ctx = "Context: " +
                    (notBlank(in.getTeam()) ? in.getTeam() : "—") +
                    " / " +
                    (notBlank(in.getSystem()) ? in.getSystem() : "—") +
                    "\n";
        }

        return ""
                + "Ownership Card — " + title + "\n"
                + "Domain: " + r.getDomain().label() + "\n"
                + "Owner: " + r.getOwner() + "\n"
                + "Contributors: " + r.getContributors() + "\n"
                + "Risk if unclear: " + r.getRisk() + " (" + r.getRiskWhy() + ")\n"
                + (ctx.isEmpty() ? "" : ctx)
                + "\nAI allowed:\n" + bullets(r.getAiCan())
                + "\nHuman required:\n" + bullets(r.getHumanMust());
    }

    private String bullets(List<String> items) {
        StringBuilder sb = new StringBuilder();
        for (String s : items) sb.append("- ").append(s).append("\n");
        return sb.toString();
    }

    private String inferTitle(String desc) {
        if (desc == null) return "Decision";
        String d = desc.trim().replaceAll("\\s+", " ");
        if (d.length() <= 52) return d;
        return d.substring(0, 52) + "…";
    }

    private String generateRoast(DecisionInput in, CheckResult r) {
        boolean spicy = "spicy".equalsIgnoreCase(in.getTone());
        boolean highRisk = "High".equalsIgnoreCase(r.getRisk());

        if (!spicy) {
            if (highRisk) return "This spans multiple teams and is hard to reverse — make the owner explicit before it becomes an incident.";
            return "If the owner isn’t explicit, this will drift into meetings instead of decisions.";
        }

        // Spicy (still enterprise-safe)
        if (highRisk) return "Classic: high impact + fuzzy ownership. That’s how future incidents are born.";
        return "Shared ownership detected. Translation: nobody decides under pressure.";
    }

    private boolean notBlank(String s) { return s != null && !s.isBlank(); }

    private int computeConfidencePct(TopScore top, TopScore second) {
        double spread = Math.max(0.2, top.score() - second.score());
        double conf = Math.min(0.95, 0.55 + (spread * 0.08));
        return (int)Math.round(conf * 100);
    }

    private record TopScore(Domain domain, double score) {}

    private TopScore maxOf(double strategic, double architectural, double delivery, double operational) {
        Domain d = Domain.STRATEGIC; double max = strategic;
        if (architectural > max) { max = architectural; d = Domain.ARCHITECTURAL; }
        if (delivery > max) { max = delivery; d = Domain.DELIVERY; }
        if (operational > max) { max = operational; d = Domain.OPERATIONAL; }
        return new TopScore(d, max);
    }

    private TopScore secondOf(double strategic, double architectural, double delivery, double operational, Domain top) {
        // crude but clear: remove top, take max of remaining
        double s = (top == Domain.STRATEGIC) ? -999 : strategic;
        double a = (top == Domain.ARCHITECTURAL) ? -999 : architectural;
        double d = (top == Domain.DELIVERY) ? -999 : delivery;
        double o = (top == Domain.OPERATIONAL) ? -999 : operational;

        Domain dom = Domain.STRATEGIC; double max = s;
        if (a > max) { max = a; dom = Domain.ARCHITECTURAL; }
        if (d > max) { max = d; dom = Domain.DELIVERY; }
        if (o > max) { max = o; dom = Domain.OPERATIONAL; }
        return new TopScore(dom, max);
    }
}
