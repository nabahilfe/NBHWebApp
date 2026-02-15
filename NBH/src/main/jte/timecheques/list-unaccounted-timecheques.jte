@import java.util.List
@import java.math.BigDecimal
@import java.time.LocalDate

@import static eu.nabahilfe.webapp.DateFormatter.*
@import static eu.nabahilfe.webapp.NumberFormatter.*

@import eu.nabahilfe.webapp.NbhConst
@import eu.nabahilfe.webapp.timecheques.TimeCheque
@import eu.nabahilfe.webapp.accountings.TransactionType
@import eu.nabahilfe.webapp.accountings.AccountableRowSelectionForm

@param AccountableRowSelectionForm formRowData
@param List<TimeCheque> timeCheques

@param java.lang.String successMessage = null
@param java.lang.String errorMessage = null

@param String fullUsername = null
@param String username = null
@param Boolean isAuthenticated = false


@template.layout.main(
    isAuthenticated = isAuthenticated,
    username = username,
    fullUsername = fullUsername,
    content = @`

    <h3>Nicht verbuchte Zeitschecks</h3>

    <br/>

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
                <form method="post" action="/accountings/new-accountable">
                    <tr>
                        <td scope="row">${dateDE(tc.getTransactionDate())}</td>
                        <td scope="row">${tc.getAssignedTo().getNameAndAddress()}</td>
                        <td scope="row">${tc.getCreatedBy().getName()}</td>
                        <td scope="row" class="text-end">${tc.getHours()}</td>
                        <td scope="row" class="text-end">€ ${numberDE(tc.getAmount())}</td>
                        <td>
                            <input type="hidden" name="accountableClassName" value="${tc.getAccountableClassName()}">
                            <input type="hidden" name="accountableId" value="${tc.getAccountableId()}">
                            <input type="hidden" name="accountableMemberId" value="${tc.getAccountableMemberId()}">
                            <input type="hidden" name="transactionType" value="${tc.getTransactionType()}">
                            <input type="hidden" name="transactionDate" value="${tc.getTransactionISODate()}">
                            <input type="hidden" name="transactionAmount" value="${tc.getTransactionAmount()}">
                            <div class="text-center">
                                <button type="submit" class="btn btn-outline-primary btn-sm bi-plus-lg"></button>
                            </div>
                        </td>
                    </tr>
                </form>
            @endfor
        </tbody>
    </table>
    <br/>

    @template.components.display-statusmessage(successMessage, errorMessage)

`)