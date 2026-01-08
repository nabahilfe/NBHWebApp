<!--
    A reusable template to display time transfers.

    Usage:
    @template.components.timetransfers(transfers = List<TimeTransfer>)

    Parameters:
    - timeTransfers: List<TimeTransfer>

-->

@import java.util.List
@import java.time.LocalDate

@import static eu.nabahilfe.webapp.DateFormatter.*

@import eu.nabahilfe.webapp.timecheques.TimeTransfer

@param List<TimeTransfer> transfers



        <thead>
            <tr>
                <th scope="col">Datum</th>
                <th scope="col" class="text-center">Stunden</th>
                <th scope="col">von</th>
                <th scope="col">an</th>
                <th scope="col">Kategorie</th>
            </tr>
        </thead>
        <tbody class="table-group-divider">
            @for (TimeTransfer tt : transfers)
                <tr>
                    <td scope="row">${formatDE(tt.getDateOfService())}</td>
                    <td scope="row" class="text-center">${tt.getHours()}</td>
                    <td scope="row">${tt.getFromMember().getName()}</td>
                    <td scope="row">${tt.getToMember().getName()}</td>
                    <td scope="row">${tt.getOffer().getDescription()}</td>
                </tr>
            @endfor
        </tbody>
