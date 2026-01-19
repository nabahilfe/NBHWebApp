@import java.util.List
@import java.math.BigDecimal
@import java.time.LocalDate

@import static eu.nabahilfe.webapp.DateFormatter.*

@import eu.nabahilfe.webapp.NbhConst
@import eu.nabahilfe.webapp.timecheques.TimeCheque

@param List<TimeCheque> timeCheques

@param java.lang.String successMessage = null
@param java.lang.String errorMessage = null

@template.layout.main(content = @`

   <h3>Nicht verbuchte Zeitschecks</h3>


    <!-- Display all TimeCheque records -->

   <table class="table table-hover table-sm" id="memberTable">
      <thead>
        <tr>
          <th scope="col">Bestelldatum</th>
          <th scope="col">für Mitglied</th>
          <th scope="col">erstellt von</th>
          <th scope="col" class="text-end">Stunden</th>
          <th scope="col" class="text-end">Betrag</th>
          <th scope="col" class="text-center">Buchen</th>
        </tr>
      </thead>

      <tbody class="table-group-divider">
        @for (TimeCheque tc : timeCheques)
        <tr>
          <td scope="row">${formatDE(tc.getTransactionDate())}</td>
          <td scope="row">${tc.getAssignedTo().getNameAndAddress()}</td>
          <td scope="row">${tc.getCreatedBy().getName()}</td>
          <td scope="row" class="text-end">${tc.getHours()}</td>
          <td scope="row" class="text-end">€ ${tc.getAmount()}</td>
          <td scope="row" class="text-center">
            <a class="btn btn-outline-primary btn-sm bi-plus-lg"
               href="/accountings/book-accountable/?accId=${tc.getId()}&accAmount=${tc.getAmount()}"
               role="button"></a>
          </td>
        </tr>
        @endfor
      </tbody>
    </table>
    <p/>

    @template.components.display-statusmessage(successMessage, errorMessage)

`)
