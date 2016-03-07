package cz.cvut.kbss.inbas.audit.dto;

import cz.cvut.kbss.inbas.audit.model.ReportingPhase;

public class InvestigationReportDto extends AbstractReportDto {

    private FactorDto rootFactor;

    private Links links;

    public FactorDto getRootFactor() {
        return rootFactor;
    }

    public void setRootFactor(FactorDto rootFactor) {
        this.rootFactor = rootFactor;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public ReportingPhase getPhase() {
        return ReportingPhase.INVESTIGATION;
    }

    @Override
    public String toString() {
        return "InvestigationReportDto{" +
                "uri=" + getUri() +
                ", key='" + getKey() + '\'' +
                ", summary='" + getSummary() + '\'' +
                ", occurrence=" + getOccurrence() +
                '}';
    }
}