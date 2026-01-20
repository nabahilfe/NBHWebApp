@import java.time.LocalDate

@import static eu.nabahilfe.webapp.DateFormatter.*
@import eu.nabahilfe.webapp.accountings.AccountingEntry

@param AccountingEntry accountingEntry

@param java.lang.String successMessage = null
@param java.lang.String errorMessage = null


@template.layout.main(content = @`

    <h3>Accounting Eintrag anlegen/bearbeitenn</h3>

    <div class="row">

        <script>
            console.log("Rendering create-accounting.jte");
            console.log("Accounting Entry: ${accountingEntry}");
        </script>

        <!-- first action is for create, second action is for update -->
        <form action="${accountingEntry.getId() == null ? "/accountings" : "/accountings/" + accountingEntry.getId()}" method="post">

          <div class="form-group row">

            <label for="accountableClass" class="col-sm-2 col-form-label">Buchung für<span class="text-danger">*</span></label>
            <div class="col-sm-10">
              <input type="text" class="form-control" name="accountableClass" id="accountableClass" value="${accountingEntry.getAccountableClass()}" required>
            </div>

            <label for="transactionType" class="col-sm-2 col-form-label">Buchungsart<span class="text-danger">*</span></label>
            <div class="col-sm-10">
              <input type="text" class="form-control" name="transactionType" id="transactionType" value="${accountingEntry.getTransactionType()}" required>
            </div>

            <label for="transactionDate" class="col-sm-2 col-form-label">Transaktionsdatum<span class="text-danger">*</span></label>
            <div class="col-sm-10">
              <input type="date" class="form-control" name="transactionDate" id="transactionDate" value="${accountingEntry.getTransactionDate().toString()}" required>
            </div>

            <label for="transactionAmount" class="col-sm-2 col-form-label">Betrag<span class="text-danger">*</span></label>
            <div class="col-sm-10">
              <input type="text" class="form-control" name="transactionAmount" id="transactionAmount" value="${accountingEntry.getTransactionAmount()}" required>
            </div>


            <label for="accountingDate" class="col-sm-2 col-form-label">Buchungsdatum<span class="text-danger">*</span></label>
            <div class="col-sm-10">
              <input type="date" class="form-control" name="accountingDate" id="accountingDate" value="${accountingEntry.getAccountingDate().toString()}" required>
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



