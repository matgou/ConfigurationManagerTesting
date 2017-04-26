package run.order66.application.domain;


import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import run.order66.application.domain.enumeration.StatusEnum;

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

    @Column(name = "key")
    private String KEY;

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
    
    @ManyToOne
    @JsonIgnore
    private RuleReport parent;


	@OneToMany(fetch=FetchType.EAGER, mappedBy = "parent")
    private Set<RuleReport> childs = new HashSet<>();

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

    public String getKEY() {
        return KEY;
    }

    public RuleReport KEY(String KEY) {
        this.KEY = KEY;
        return this;
    }

    public void setKEY(String KEY) {
        this.KEY = KEY;
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

    public ZonedDateTime getSubmitAt() {
        return submitAt;
    }

    public RuleReport submitAt(ZonedDateTime submitAt) {
        this.submitAt = submitAt;
        return this;
    }

    public void setSubmitAt(ZonedDateTime submitAt) {
        this.submitAt = submitAt;
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
        return this;
    }

    public void setFinishAt(ZonedDateTime finishAt) {
        this.finishAt = finishAt;
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

    public User getUser() {
        return user;
    }

    public RuleReport user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<RuleReport> getChilds() {
        return childs;
    }

    public RuleReport childs(Set<RuleReport> ruleReports) {
        this.childs = ruleReports;
        return this;
    }

    public RuleReport addChilds(RuleReport ruleReport) {
        this.childs.add(ruleReport);
        ruleReport.setParent(this);
        return this;
    }

    public RuleReport removeChilds(RuleReport ruleReport) {
        this.childs.remove(ruleReport);
        ruleReport.setParent(null);
        return this;
    }

    public void setChilds(Set<RuleReport> ruleReports) {
        this.childs = ruleReports;
    }

    public RuleReport getParent() {
		return parent;
	}

	public void setParent(RuleReport parent) {
		this.parent = parent;
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
            ", KEY='" + KEY + "'" +
            ", log='" + log + "'" +
            ", submitAt='" + submitAt + "'" +
            ", updatedAt='" + updatedAt + "'" +
            ", finishAt='" + finishAt + "'" +
            '}';
    }
}
