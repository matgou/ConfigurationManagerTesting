package info.kapable.utils.configurationmanager.reporting.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Rule.
 */
@Entity
@Table(name = "ccol_cm_rule")
public class Rule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "rule_name")
    private String ruleName;

    @Lob
    @Column(name = "rule_args")
    private String ruleArgs;

    @ManyToOne
    private RuleType ruleType;

    @ManyToOne
    private Process process;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRuleName() {
        return ruleName;
    }

    public Rule ruleName(String ruleName) {
        this.ruleName = ruleName;
        return this;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleArgs() {
        return ruleArgs;
    }

    public Rule ruleArgs(String ruleArgs) {
        this.ruleArgs = ruleArgs;
        return this;
    }

    public void setRuleArgs(String ruleArgs) {
        this.ruleArgs = ruleArgs;
    }

    public RuleType getRuleType() {
        return ruleType;
    }

    public Rule ruleType(RuleType ruleType) {
        this.ruleType = ruleType;
        return this;
    }

    public void setRuleType(RuleType ruleType) {
        this.ruleType = ruleType;
    }

    public Process getProcess() {
        return process;
    }

    public Rule process(Process process) {
        this.process = process;
        return this;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Rule rule = (Rule) o;
        if (rule.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, rule.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Rule{" +
            "id=" + id +
            ", ruleName='" + ruleName + "'" +
            ", ruleArgs='" + ruleArgs + "'" +
            '}';
    }
}
