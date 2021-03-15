package org.example.toolsapp;

;
//import com.fasterxml.jackson.annotation.*;

public class Result {
    private String updated;
    private String source;
    private String target;
    private double value;
    private double quantity;
    private double amount;

    //@JsonProperty("updated")
    public String getUpdated() { return updated; }
    //@JsonProperty("updated")
    public void setUpdated(String value) { this.updated = value; }

   // @JsonProperty("source")
    public String getSource() { return source; }
    //@JsonProperty("source")
    public void setSource(String value) { this.source = value; }

    //@JsonProperty("target")
    public String getTarget() { return target; }
    //@JsonProperty("target")
    public void setTarget(String value) { this.target = value; }

    //@JsonProperty("value")
    public double getValue() { return value; }
    //@JsonProperty("value")
    public void setValue(double value) { this.value = value; }

    //@JsonProperty("quantity")
    public double getQuantity() { return quantity; }
    //@JsonProperty("quantity")
    public void setQuantity(double value) { this.quantity = value; }

    //@JsonProperty("amount")
    public double getAmount() { return amount; }
    //@JsonProperty("amount")
    public void setAmount(double value) { this.amount = value; }
}
