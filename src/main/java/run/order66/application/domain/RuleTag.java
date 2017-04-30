package run.order66.application.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Objects;

/**
 * A RuleTag.
 */
@Entity
@Table(name = "rule_tag")
public class RuleTag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 3, max = 255)
    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @ManyToOne
    @JsonIgnore
    private Rule rule;

    public RuleTag() {
    	this.name = "";
    }
    public RuleTag(String name) {
    	this.name = name;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public RuleTag name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Rule getRule() {
        return rule;
    }

    public RuleTag rule(Rule rule) {
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
        RuleTag ruleTag = (RuleTag) o;
        if (ruleTag.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, ruleTag.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "RuleTag{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
