package info.kapable.utils.configurationmanager.reporting.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import info.kapable.utils.configurationmanager.reporting.domain.enumeration.StatusEnum;

/**
 * A RuleReport.
 */
@Entity
@Table(name = "ccol_cm_rulereport")
public class RuleReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "report_date")
    private LocalDate reportDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusEnum status;

    @Lob
    @Column(name = "log")
    private String log;

    @ManyToOne
    private Rule rule;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public RuleReport reportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
        return this;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public RuleReport status(StatusEnum status) {
        this.status = status;
        return this;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public String getLog() {
        return log;
    }

    public RuleReport log(String log) {
        this.log = log;
        return this;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public Rule getRule() {
        return rule;
    }

    public RuleReport rule(Rule rule) {
        this.rule = rule;
        return this;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RuleReport ruleReport = (RuleReport) o;
        if (ruleReport.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, ruleReport.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "RuleReport{" +
            "id=" + id +
            ", reportDate='" + reportDate + "'" +
            ", status='" + status + "'" +
            ", log='" + log + "'" +
            '}';
    }
}
