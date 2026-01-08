package eu.nabahilfe.webapp.accountings;

import java.math.BigDecimal;

public interface Accountable {

    public abstract String getAccounableEntityName();
    public abstract BigDecimal getAmount();

}
