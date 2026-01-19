<!--

    This template displays a list of acquired time cheques.

    Usage:
    @template.components.list-timecheques-component(timeCheques = List<TimeCheque>)

    Parameters:
    - timeCheques: List<TimeCheque>

-->


@import java.util.List
@import java.time.LocalDate

@import static eu.nabahilfe.webapp.DateFormatter.*
@import eu.nabahilfe.webapp.timecheques.TimeCheque

@param List<TimeCheque> timeCheques


<div class="card">
    <div class="card-body">
        <h5>Template: Erworbene Zeitschecks</h5>
        <p class="card-text">FIXME: USERNAME</p>

        <table class="table table-hover table-sm" id="memberTable">
            <thead>
                <tr>
                    <th scope="col">Datum</th>
                    <th scope="col">Erstellt von</th>
                    <th scope="col" class="text-end">Stunden</th>
                </tr>
            </thead>
            <tbody class="table-group-divider">
                @for (TimeCheque tc : timeCheques)
                    <tr>
                        <td scope="row">${formatDE(tc.getTransactionDate())}</td>
                        <td scope="row">${tc.getCreatedBy().getName()}</td>
                        <td scope="row" class="text-end">${tc.getHours()}</td>
                    </tr>
                @endfor
            </tbody>
        </table>

    </div>
</div>