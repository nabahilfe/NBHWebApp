<!--
    A reusable template to display time transfers.

    Usage:
    @template.components.list-timetransfers-component(member = Member, transfers = List<TimeTransfer>)

    Parameters:
    - timeTransfers: List<TimeTransfer>

-->

@import java.util.List
@import java.time.LocalDate

@import static eu.nabahilfe.webapp.DateFormatter.*
@import static eu.nabahilfe.webapp.jte.TemplateHelpers.*

@import eu.nabahilfe.webapp.timetransfers.TimeTransfer
@import eu.nabahilfe.webapp.members.Member

@param Member member
@param List<TimeTransfer> transfers


<div class="card">
    <div class="card-body">
        <h5>Gehandelte Zeitschecks</h5>
        <p class="card-text"><strong>${member.getName()}</strong></p>
        <div class="table-responsive">

            <table class="table table-hover table-sm" id="memberTable">
                <thead>
                    <tr>
                        <th scope="col">Datum</th>
                        <th scope="col">von</th>
                        <th scope="col">an</th>
                        <th scope="col">Kategorie</th>
                        <th scope="col" class="text-end">Stunden</th>
                    </tr>
                </thead>
                <tbody class="table-group-divider">
                    @for (TimeTransfer tt : transfers)
                        <tr>
                            <td scope="row">${dateDE(tt.getDateOfService())}</td>
                            <td scope="row">${boldMemberNameIfSameId(member.getId(), tt.getFromMember())}</td>
                            <td scope="row">${boldMemberNameIfSameId(member.getId(), tt.getToMember())}</td>
                            <td scope="row">${tt.getOffer().getDescription()}</td>
                            <td scope="row" class="text-end">${tt.getHours()}</td>
                        </tr>
                    @endfor
                </tbody>
            </table>
        </div>
    </div>
</div>
