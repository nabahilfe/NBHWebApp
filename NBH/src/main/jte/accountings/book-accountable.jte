@import java.time.LocalDate

@import static eu.nabahilfe.webapp.DateFormatter.*
@import eu.nabahilfe.webapp.accountings.AccountingEntry

@param AccountingEntry accountingEntry

@param java.lang.String successMessage = null
@param java.lang.String errorMessage = null


@template.layout.main(content = @`

    <h3>Accounting Eintrag anlegen/bearbeitenn</h3>

    <div class="row">

        <!-- first action is for create, second action is for update -->
        <form action="${accountingEntry.getId() == null ? "/accountings" : "/accountings/" + accountingEntry.getId()}" method="post">

          <div class="form-group row">

            <label for="accountingType" class="col-sm-2 col-form-label">INCOMING/OUTGOING<span class="text-danger">*</span></label>
            <div class="col-sm-10">
              <input type="text" class="form-control" name="accountingType" id="accountingType" value="${accountingEntry.getAccountingType()}" required>
            </div>

            <label for="amount" class="col-sm-2 col-form-label">Betrag<span class="text-danger">*</span></label>
            <div class="col-sm-10">
              <input type="text" class="form-control" name="amount" id="amount" value="${accountingEntry.getAmount()}" required>
            </div>

            <label for="accountingDate" class="col-sm-2 col-form-label">Buchungsdatum<span class="text-danger">*</span></label>
            <div class="col-sm-10">
              <input type="date" class="form-control" name="accountingDate" id="accountingDate"
                    value="${accountingEntry.getAmount() != null ? accountingEntry.getAmount().toString() : ""}" required>
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



