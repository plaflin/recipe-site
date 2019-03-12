package com.teamtreehouse.model;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;

@Entity
public class Step
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String stepName;

    public Step(String stepName)
    {
        this.stepName = stepName;
    }

    public Step()
    {
        this(null);
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getStepName()
    {
        return stepName;
    }

    public void setStepName(String stepName)
    {
        this.stepName = stepName;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Step step = (Step)o;

        if (id != null ? !id.equals(step.id) : step.id != null)
            return false;
        return stepName != null ? stepName.equals(step.stepName) : step.stepName == null;

    }

    @Override
    public int hashCode()
    {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (stepName != null ? stepName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString()
    {
        return "Step{" +
                "id=" + id +
                ", stepName='" + stepName + '\'' +
                //", recipe=" + recipe +
                '}';
    }
}
