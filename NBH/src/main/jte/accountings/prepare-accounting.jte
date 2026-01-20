@import java.time.LocalDate

@import static eu.nabahilfe.webapp.DateFormatter.*
@import static eu.nabahilfe.webapp.NumberFormatter.*

@import eu.nabahilfe.webapp.accountings.AccountingEntry

@param AccountingEntry accountingEntry

@param java.lang.String successMessage = null
@param java.lang.String errorMessage = null


@template.layout.main(content = @`

    <h3>Buchungsdatensatz anlegen</h3>
    <p>Damit wird dokumentiert, dass die Buchung auf dem Bankkonto durchgeführt wurde.</p>
    <br/>

    <div class="row">

        <!-- first action is for create, second action is for update -->
        <form action="${accountingEntry.getId() == null ? "/accountings" : "/accountings/" + accountingEntry.getId()}" method="post">

          <div class="form-group row">

            <label for="accountableClass" class="col-sm-2 col-form-label">Buchung für</label>
            <div class="col-sm-10">
              <input type="text" class="form-control" name="accountableClass" id="accountableClass" value="${accountingEntry.getAccountableClassDisplayName()}" disabled readonly>
            </div>

            <label for="transactionType" class="col-sm-2 col-form-label">Buchungsart</label>
            <div class="col-sm-10">
              <input type="text" class="form-control" name="transactionType" id="transactionType" value="${accountingEntry.getTransactionType()}"  disabled readonly>
            </div>

            <label for="transactionDate" class="col-sm-2 col-form-label">Transaktionsdatum</label>
            <div class="col-sm-10">
              <input type="text" class="form-control" name="transactionDate" id="transactionDate" value="${dateReverseDE(accountingEntry.getTransactionDate())}" disabled readonly>
            </div>

            <label for="transactionAmount" class="col-sm-2 col-form-label">Betrag in €</label>
            <div class="col-sm-10">
              <input type="text" class="form-control" name="transactionAmount" id="transactionAmount" value="${numberDE(accountingEntry.getTransactionAmount())}" disabled readonly>
            </div>

            <label for="fromMember" class="col-sm-2 col-form-label">Von Mitglied</label>
            <div class="col-sm-10">
              <input type="text" class="form-control" name="fromMember" id="fromMember" value="${accountingEntry.getAccountableMember().getNameAndAddress()}" disabled readonly>
            </div>


            <p></p>

            <label for="accountingDate" class="col-sm-2 col-form-label">Buchungsdatum<span class="text-danger">*</span></label>
            <div class="col-sm-10">
              <input type="date" class="form-control" name="accountingDate" id="accountingDate" value="${formatISO(accountingEntry.getAccountingDate())}" required>
            </div>

          </div>

            <br/>

          <div class="form-group row">
            <div class="col-sm-10">
                <button type="submit" class="btn btn-primary bi-floppy"> Speichern</button>
            </div>
          </div>

        <br/>
        @template.components.display-statusmessage(successMessage, errorMessage)

        </form>

    </div>

`)



