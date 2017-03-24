package run.order66.application.service.dto;


import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Objects;

import run.order66.application.domain.Process;
import run.order66.application.domain.RuleReport;
import run.order66.application.domain.enumeration.StatusEnum;

/**
 * A RuleReport.
 */
public class RuleLastReportDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    
    private LocalDate reportDate;

    private StatusEnum status;

    private String log;

    private ZonedDateTime submitAt;

    private ZonedDateTime updatedAt;

    private ZonedDateTime finishAt;

    public RuleLastReportDTO() {
        // Empty constructor needed for MapStruct.
    }

    public RuleLastReportDTO(RuleReport report) {
        this.id = report.getId();
        this.reportDate = report.getReportDate();
        this.status = report.getStatus();
        this.log = report.getLog();
        this.submitAt = report.getSubmitAt();
        this.updatedAt = report.getUpdatedAt();
        this.finishAt = report.getFinishAt();
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

	public void setReportDate(LocalDate reportDate) {
		this.reportDate = reportDate;
	}

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	public ZonedDateTime getSubmitAt() {
		return submitAt;
	}

	public void setSubmitAt(ZonedDateTime submitAt) {
		this.submitAt = submitAt;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	public ZonedDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(ZonedDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public ZonedDateTime getFinishAt() {
		return finishAt;
	}

	public void setFinishAt(ZonedDateTime finishAt) {
		this.finishAt = finishAt;
	}
}
