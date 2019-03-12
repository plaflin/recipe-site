package com.teamtreehouse.model;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Ingredient
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String item;

    private String condition;

    @NotBlank
    private String quantity;

    public Ingredient(String item, String condition, String quantity)
    {
        this.item = item;
        this.condition = condition;
        this.quantity = quantity;
    }

    public Ingredient()
    {
        this(null, null, null);
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getItem()
    {
        return item;
    }

    public void setItem(String item)
    {
        this.item = item;
    }

    public String getCondition()
    {
        return condition;
    }

    public void setCondition(String condition)
    {
        this.condition = condition;
    }

    public String getQuantity()
    {
        return quantity;
    }

    public void setQuantity(String quantity)
    {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Ingredient that = (Ingredient)o;

        if (id != null ? !id.equals(that.id) : that.id != null)
            return false;
        if (item != null ? !item.equals(that.item) : that.item != null)
            return false;
        if (condition != null ? !condition.equals(that.condition) : that.condition != null)
            return false;
        return quantity != null ? quantity.equals(that.quantity) : that.quantity == null;

    }

    @Override
    public int hashCode()
    {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (item != null ? item.hashCode() : 0);
        result = 31 * result + (condition != null ? condition.hashCode() : 0);
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        return result;
    }

    @Override
    public String toString()
    {
        return "Ingredient{" +
                "id=" + id +
                ", item='" + item + '\'' +
                ", condition='" + condition + '\'' +
                ", quantity='" + quantity + '\'' +
                //", recipe=" + recipe +
                '}';
    }
}
