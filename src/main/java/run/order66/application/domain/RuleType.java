package run.order66.application.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A RuleType.
 */
@Entity
@Table(name = "ccol_cm_ruletype")
public class RuleType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Size(max = 50)
    @Column(name = "rule_type_name", length = 50)
    private String ruleTypeName;

    @Column(name = "checker_bean_name")
    private String checkerBeanName;

    @Column(name = "description")
    private String description;

    @Column(name = "required_arguments_list")
    private String requiredArgumentsList;

    @Column(name = "reporting_bean_name")
    private String reportingBeanName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRuleTypeName() {
        return ruleTypeName;
    }

    public RuleType ruleTypeName(String ruleTypeName) {
        this.ruleTypeName = ruleTypeName;
        return this;
    }

    public void setRuleTypeName(String ruleTypeName) {
        this.ruleTypeName = ruleTypeName;
    }

    public String getCheckerBeanName() {
        return checkerBeanName;
    }

    public RuleType checkerBeanName(String checkerBeanName) {
        this.checkerBeanName = checkerBeanName;
        return this;
    }

    public void setCheckerBeanName(String checkerBeanName) {
        this.checkerBeanName = checkerBeanName;
    }

    public String getDescription() {
        return description;
    }

    public RuleType description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequiredArgumentsList() {
        return requiredArgumentsList;
    }

    public RuleType requiredArgumentsList(String requiredArgumentsList) {
        this.requiredArgumentsList = requiredArgumentsList;
        return this;
    }

    public void setRequiredArgumentsList(String requiredArgumentsList) {
        this.requiredArgumentsList = requiredArgumentsList;
    }

    public String getReportingBeanName() {
        return reportingBeanName;
    }

    public RuleType reportingBeanName(String reportingBeanName) {
        this.reportingBeanName = reportingBeanName;
        return this;
    }

    public void setReportingBeanName(String reportingBeanName) {
        this.reportingBeanName = reportingBeanName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RuleType ruleType = (RuleType) o;
        if (ruleType.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, ruleType.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "RuleType{" +
            "id=" + id +
            ", ruleTypeName='" + ruleTypeName + "'" +
            ", checkerBeanName='" + checkerBeanName + "'" +
            ", description='" + description + "'" +
            ", requiredArgumentsList='" + requiredArgumentsList + "'" +
            ", reportingBeanName='" + reportingBeanName + "'" +
            '}';
    }
}
