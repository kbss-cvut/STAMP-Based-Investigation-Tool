package cz.cvut.kbss.inbas.reporting.dto.event;

import java.net.URI;

public class FactorGraphEdge {

    private Integer from;

    private Integer to;

    private URI linkType;

    public FactorGraphEdge() {
    }

    public FactorGraphEdge(Integer from, Integer to, URI linkType) {
        this.from = from;
        this.to = to;
        this.linkType = linkType;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

    public URI getLinkType() {
        return linkType;
    }

    public void setLinkType(URI linkType) {
        this.linkType = linkType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FactorGraphEdge that = (FactorGraphEdge) o;

        if (from != null ? !from.equals(that.from) : that.from != null) return false;
        if (to != null ? !to.equals(that.to) : that.to != null) return false;
        return linkType != null ? linkType.equals(that.linkType) : that.linkType == null;

    }

    @Override
    public int hashCode() {
        int result = from != null ? from.hashCode() : 0;
        result = 31 * result + (to != null ? to.hashCode() : 0);
        result = 31 * result + (linkType != null ? linkType.hashCode() : 0);
        return result;
    }
}
