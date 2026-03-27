@import java.util.List
@import java.time.LocalDate

@import static eu.nabahilfe.webapp.DateFormatter.*
@import static eu.nabahilfe.webapp.NumberFormatter.*

@import eu.nabahilfe.webapp.members.Member
@import eu.nabahilfe.webapp.timecheques.TimeCheque

@param Member member
@param List<TimeCheque> purchasedTimeCheques

@param java.lang.String successMessage = null
@param java.lang.String errorMessage = null

@param String fullUsername = null
@param String username = null
@param Boolean isAuthenticated = false


@param eu.nabahilfe.webapp.security.CsrfHiddenInput csrfHiddenInput
@param eu.nabahilfe.webapp.security.ViewContext ctxt


@template.layout.mainpage(
    csrfHiddenInput = csrfHiddenInput,
    ctxt = ctxt,
    content = @`

    <h3>Aktuelles Zeitguthaben</h3>

    <div class="card">
        <div class="card-body bg-success bg-opacity-25">
            <h4>${member.getName()}</h4>
            <h5>${member.getAddress()}</h5>
            <hr/>
            <b>Aktuelles Zeitguthaben: ${member.getAccumulatedHours()} h</b>
        </div>
    </div>

    <br/>

    @template.components.display-statusmessage(successMessage, errorMessage)



    <!-- FIXME: sollte durch eine Komponente ersetzt werden! -->

    <div class="card">
        <div class="card-body">
            <h5>Erworbene Zeitschecks</h5>

            <table class="table table-hover table-sm" id="memberTable">
                <thead>
                    <tr>
                        <th scope="col">Datum</th>
                        <th scope="col">erstellt von</th>
                        <th scope="col" class="text-end">Stunden</th>
                        <th scope="col" class="text-end">Betrag</th>
                    </tr>
                </thead>
                <tbody class="table-group-divider">
                    @for (TimeCheque tc : purchasedTimeCheques)
                    <tr>
                        <td>${dateDE(tc.getTransactionDate())}</td>
                        <td>${tc.getCreatedBy().getName()}</td>
                        <td class="text-end">${tc.getHours()}</td>
                        <td class="text-end">${numberDE(tc.getAmount())}&nbsp;&euro;</td>
                    </tr>
                    @endfor
                </tbody>
            </table>

        </div>
    </div>

    <br/>

    <form action="/members/${member.getId()}" method="get">
        <button type="submit" class="btn btn-outline-primary bi-arrow-left"> Zurück zum Mitglied</button>
    </form>


`)



