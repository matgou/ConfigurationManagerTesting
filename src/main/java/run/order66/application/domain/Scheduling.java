package run.order66.application.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import run.order66.application.domain.enumeration.TriggerEnum;

/**
 * A Scheduling.
 */
@Entity
@Table(name = "scheduling")
public class Scheduling implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "trigger")
    private TriggerEnum trigger;

    @Lob
    @Column(name = "rule")
    private String rule;

    @Column(name = "scheduling_label")
    private String schedulingLabel;

    @ManyToMany
    @JoinTable(name = "scheduling_rules",
               joinColumns = @JoinColumn(name="schedulings_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="rules_id", referencedColumnName="id"))
    private Set<Rule> rules = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TriggerEnum getTrigger() {
        return trigger;
    }

    public Scheduling trigger(TriggerEnum trigger) {
        this.trigger = trigger;
        return this;
    }

    public void setTrigger(TriggerEnum trigger) {
        this.trigger = trigger;
    }

    public String getRule() {
        return rule;
    }

    public Scheduling rule(String rule) {
        this.rule = rule;
        return this;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getQuartzCronRule() {
    	return "0 " + this.rule;
    }
    public String getSchedulingLabel() {
        return schedulingLabel;
    }

    public Scheduling schedulingLabel(String schedulingLabel) {
        this.schedulingLabel = schedulingLabel;
        return this;
    }

    public void setSchedulingLabel(String schedulingLabel) {
        this.schedulingLabel = schedulingLabel;
    }

    public Set<Rule> getRules() {
        return rules;
    }

    public Scheduling rules(Set<Rule> rules) {
        this.rules = rules;
        return this;
    }

    public Scheduling addRules(Rule rule) {
        this.rules.add(rule);
        return this;
    }

    public Scheduling removeRules(Rule rule) {
        this.rules.remove(rule);
        return this;
    }

    public void setRules(Set<Rule> rules) {
        this.rules = rules;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Scheduling scheduling = (Scheduling) o;
        if (scheduling.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, scheduling.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Scheduling{" +
            "id=" + id +
            ", trigger='" + trigger + "'" +
            ", rule='" + rule + "'" +
            ", schedulingLabel='" + schedulingLabel + "'" +
            '}';
    }
}
