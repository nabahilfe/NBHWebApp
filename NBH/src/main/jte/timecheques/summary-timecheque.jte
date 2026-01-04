@import java.util.List

@import eu.nabahilfe.webapp.members.Member
@import eu.nabahilfe.webapp.timeexchange.TimeCheque

@param Member member
@param List<TimeCheque> purchasedTimeCheques

@param java.lang.String successMessage = null
@param java.lang.String errorMessage = null

@template.layout.main(content = @`

    <h3 class="text-primary">Aktuelles Zeitguthaben</h3>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js" integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI" crossorigin="anonymous"></script>

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

    <div class="card">
        <div class="card-body">
            <h5 class="card-title text-primary">Bereits erworbene Zeitschecks</h5>
            <p class="card-text">Nur die letzten 10 werden angezeigt</p>

            <table class="table table-striped table-bordered table-hover table-sm mt-4" id="timeChequeTable">
                <thead class="table-dark">
                    <tr>
                        <th scope="col">Datum</th>
                        <th scope="col">erstellt von</th>
                        <th scope="col" class="text-end">Stunden</th>
                    </tr>
                </thead>
                <tbody>
                    @for (TimeCheque tc : purchasedTimeCheques)
                    <tr>
                        <th scope="row">${tc.getOrderDateString()}</th>
                        <th scope="row">${tc.getCreatedBy().getName()}</th>
                        <th scope="row" class="text-end">${tc.getHours()}</th>
                    </tr>
                    @endfor
                </tbody>
            </table>

        </div>
    </div>

`)


