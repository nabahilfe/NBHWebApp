package eu.nabahilfe.webapp.accountings;

import java.math.BigDecimal;

public interface Accountable {

    public abstract Long getAccountableId();
    public abstract String getAccountableTableName();
    public abstract BigDecimal getAmount();

}
