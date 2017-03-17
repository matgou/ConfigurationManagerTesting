package info.kapable.utils.configurationmanager.reporting.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
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

    @Column(name = "submit_at")
    private ZonedDateTime submitAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @Column(name = "finish_at")
    private ZonedDateTime finishAt;

    @ManyToOne
    private Rule rule;

    @ManyToOne
    private User user;

    public RuleReport() {
        this.setUpdatedAt(ZonedDateTime.now());
    }
    
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
        this.setUpdatedAt(ZonedDateTime.now());
        return this;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
        this.setUpdatedAt(ZonedDateTime.now());
    }

    public StatusEnum getStatus() {
        return status;
    }

    public RuleReport status(StatusEnum status) {
        this.status = status;
        this.setUpdatedAt(ZonedDateTime.now());
        return this;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
        this.setUpdatedAt(ZonedDateTime.now());
    }

    public String getLog() {
        return log;
    }

    public RuleReport log(String log) {
        this.log = log;
        this.setUpdatedAt(ZonedDateTime.now());
        return this;
    }

    public void setLog(String log) {
        this.log = log;
        this.setUpdatedAt(ZonedDateTime.now());
    }

    public ZonedDateTime getSubmitAt() {
        return submitAt;
    }

    public RuleReport submitAt(ZonedDateTime submitAt) {
        this.submitAt = submitAt;
        this.setUpdatedAt(ZonedDateTime.now());
        return this;
    }

    public void setSubmitAt(ZonedDateTime submitAt) {
        this.submitAt = submitAt;
        this.setUpdatedAt(ZonedDateTime.now());
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public RuleReport updatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ZonedDateTime getFinishAt() {
        return finishAt;
    }

    public RuleReport finishAt(ZonedDateTime finishAt) {
        this.finishAt = finishAt;
        this.setUpdatedAt(ZonedDateTime.now());
        return this;
    }

    public void setFinishAt(ZonedDateTime finishAt) {
        this.finishAt = finishAt;
        this.setUpdatedAt(ZonedDateTime.now());
    }

    public Rule getRule() {
        return rule;
    }

    public RuleReport rule(Rule rule) {
        this.rule = rule;
        this.setUpdatedAt(ZonedDateTime.now());
        return this;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
        this.setUpdatedAt(ZonedDateTime.now());
    }

    public User getUser() {
        return user;
    }

    public RuleReport user(User user) {
        this.user = user;
        this.setUpdatedAt(ZonedDateTime.now());
        return this;
    }

    public void setUser(User user) {
        this.user = user;
        this.setUpdatedAt(ZonedDateTime.now());
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
            ", submitAt='" + submitAt + "'" +
            ", updatedAt='" + updatedAt + "'" +
            ", finishAt='" + finishAt + "'" +
            '}';
    }
}
