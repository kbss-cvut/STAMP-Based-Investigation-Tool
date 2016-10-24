package cz.cvut.kbss.inbas.reporting.model;

import cz.cvut.kbss.inbas.reporting.model.qam.Question;
import cz.cvut.kbss.inbas.reporting.model.util.factorgraph.FactorGraphItem;
import cz.cvut.kbss.inbas.reporting.model.util.factorgraph.FactorGraphNodeVisitor;
import cz.cvut.kbss.jopa.model.annotations.*;

import java.io.Serializable;
import java.net.URI;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@OWLClass(iri = Vocabulary.s_c_Event)
public class Event extends AbstractEntity implements FactorGraphItem, Serializable, Comparable<Event> {

    @OWLObjectProperty(iri = Vocabulary.s_p_has_factor, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Factor> factors;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_part, fetch = FetchType.EAGER, cascade = {CascadeType.MERGE,
            CascadeType.REMOVE})
    private Set<Event> children;

    @OWLDataProperty(iri = Vocabulary.s_p_has_start_time)
    private Date startTime;

    @OWLDataProperty(iri = Vocabulary.s_p_has_end_time)
    private Date endTime;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_event_type)
    private Set<URI> eventTypes;

    @OWLDataProperty(iri = Vocabulary.s_p_child_index)
    private Integer index;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_related_question, cascade = {CascadeType.MERGE,
            CascadeType.REMOVE}, fetch = FetchType.EAGER)
    private Question question;

    @Types
    private Set<String> types;

    @Transient
    private Integer referenceId;

    public Event() {
    }

    public Event(Event other) {
        this.startTime = other.startTime;
        this.endTime = other.endTime;
        this.eventTypes = other.eventTypes;
        this.index = other.index;
        if (other.types != null) {
            this.types = new HashSet<>(other.types);
        }
        if (other.question != null) {
            this.question = new Question(other.question);
        }
    }

    public Set<Factor> getFactors() {
        return factors;
    }

    public void setFactors(Set<Factor> factors) {
        this.factors = factors;
    }

    @Override
    public void addFactor(Factor f) {
        Objects.requireNonNull(f);
        if (factors == null) {
            this.factors = new HashSet<>();
        }
        factors.add(f);
    }

    public Set<Event> getChildren() {
        return children;
    }

    public void setChildren(Set<Event> children) {
        this.children = children;
    }

    @Override
    public void addChild(Event child) {
        Objects.requireNonNull(child);
        if (children == null) {
            this.children = new HashSet<>();
        }
        children.add(child);
    }

    public Set<URI> getEventTypes() {
        return eventTypes;
    }

    /**
     * Sets type of this event.
     * <p>
     * Also adds the event type's URI to this instance's types.
     *
     * @param eventTypes The type to set
     * @see Vocabulary#s_p_has_event_type
     */
    public void setEventTypes(Set<URI> eventTypes) {
        this.eventTypes = eventTypes;
        if (eventTypes != null) {
            if (types == null) {
                this.types = new HashSet<>();
            }
            eventTypes.forEach(e -> types.add(e.toString()));
        }
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * Represents position at among other children of this Event's parent.
     * <p>
     * This index can be used to order Event's children.
     *
     * @return Integer specifying the position or {@code null}, if the index is not relevant here (e.g. this event has
     * no parent)
     */
    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Set<String> getTypes() {
        return types;
    }

    public void setTypes(Set<String> types) {
        this.types = types;
    }

    /**
     * Reference id which was used by the corresponding DTO instance (if it was used).
     * <p>
     * Can be useful for identification of this instance in case we cannot rely on URI (e.g. when it has not been
     * generated, yet).
     * <p>
     * Note that in most cases the return value will be {@code null}. This is a non-persistent field.
     *
     * @return Reference id, can be {@code null}
     */
    public Integer getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }

    @Override
    public void accept(FactorGraphNodeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "Event{" + uri +
                ", types=" + types +
                '}';
    }

    @Override
    public int compareTo(Event o) {
        if (index != null && o.index != null) {
            return index.compareTo(o.index);
        }
        // If either index is missing, do not use it at all. It could break sorted set equals/hashCode contract
        return hashCode() - o.hashCode();
    }
}
