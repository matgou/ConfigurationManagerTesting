package run.order66.application.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Process.
 */
@Entity
@Table(name = "ccol_cm_process")
public class Process implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Size(max = 50)
    @Column(name = "process_name", length = 50)
    private String processName;

    @ManyToOne
    private Process parent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProcessName() {
        return processName;
    }

    public Process processName(String processName) {
        this.processName = processName;
        return this;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public Process getParent() {
        return parent;
    }

    public Process parent(Process process) {
        this.parent = process;
        return this;
    }

    public void setParent(Process process) {
        this.parent = process;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Process process = (Process) o;
        if (process.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, process.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Process{" +
            "id=" + id +
            ", processName='" + processName + "'" +
            '}';
    }
}
