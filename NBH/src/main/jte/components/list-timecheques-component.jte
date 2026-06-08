<!--
  Copyright (c) 2025–2026 Maximilian Weißböck
  Licensed under the MIT License (see LICENSE file).
-->

<!--

    This reusable template displays a list of acquired time cheques.

    Usage:
    @template.components.list-timecheques-component(member = member, timeCheques = List<TimeCheque>)

    Parameters:
    - timeCheques: List<TimeCheque>

-->


@import java.util.List
@import java.time.LocalDate

@import static eu.nabahilfe.webapp.DateFormatter.*
@import static eu.nabahilfe.webapp.NumberFormatter.*
@import eu.nabahilfe.webapp.timecheques.TimeCheque

@import eu.nabahilfe.webapp.members.Member

@param Member member
@param List<TimeCheque> timeCheques


<div class="card">
    <div class="card-body">
        <p class="card-text"><strong>${member.getName()} - Gekaufte Zeitschecks</strong></p>

        <table class="table table-hover table-sm" id="memberTable">
            <thead>
                <tr>
                    <th scope="col">Datum</th>
                    <th scope="col">Erstellt von</th>
                    <th scope="col" class="text-end">h</th>
                    <th scope="col" class="text-end">&euro;</th>
                </tr>
            </thead>
            <tbody class="table-group-divider">
                @for (TimeCheque tc : timeCheques)
                    <tr>
                        <td scope="row">${dateMMMDD(tc.getTransactionDate())}</td>
                        <td scope="row">${tc.getCreatedBy().getName()}</td>
                        <td scope="row" class="text-end">${tc.getHours()}</td>
                        <td scope="row" class="text-end">${numberDE(tc.getAmount())}</td>
                    </tr>
                @endfor
            </tbody>
        </table>

    </div>
</div>