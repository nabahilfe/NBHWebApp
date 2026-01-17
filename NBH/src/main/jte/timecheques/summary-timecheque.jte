@import java.util.List
@import java.time.LocalDate

@import static eu.nabahilfe.webapp.DateFormatter.*

@import eu.nabahilfe.webapp.members.Member
@import eu.nabahilfe.webapp.timecheques.TimeCheque

@param Member member
@param List<TimeCheque> purchasedTimeCheques

@param java.lang.String successMessage = null
@param java.lang.String errorMessage = null

@template.layout.main(content = @`

    <h3>Aktuelles Zeitguthaben</h3>

    <div class="card">
        <div class="card-header">
            <h4 class="text-primary">${member.getName()}</h4>
            <p>${member.getAddress()}</p>
        </div>
        <ul class="list-group list-group-flush">
          <li class="list-group-item">
              Aktuelles Zeitguthaben: <b>${member.getAccumulatedHours()} h</b>
            </li>
        </ul>
    </div>

    <p/>

    @template.components.display-statusmessage(successMessage, errorMessage)

    <!-- FIXME: sollte durch eine Komponente ersetzt werden! -->

    <div class="card">
        <div class="card-body">
            <h5>Erworbene Zeitschecks von FIXME: Username</h5>

            <table class="table table-hover table-sm" id="memberTable">
                <thead>
                    <tr>
                        <th scope="col">Datum</th>
                        <th scope="col">erstellt von</th>
                        <th scope="col" class="text-end">Stunden</th>
                    </tr>
                </thead>
                <tbody class="table-group-divider">
                    @for (TimeCheque tc : purchasedTimeCheques)
                    <tr>
                        <td>${formatDE(tc.getTransactionDate())}</td>
                        <td>${tc.getCreatedBy().getName()}</td>
                        <td class="text-end">${tc.getHours()}</td>
                    </tr>
                    @endfor
                </tbody>
            </table>

        </div>
    </div>

`)


