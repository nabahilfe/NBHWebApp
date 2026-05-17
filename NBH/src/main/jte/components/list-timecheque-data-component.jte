<!--
  Copyright (c) 2025–2026 Maximilian Weißböck
  Licensed under the MIT License (see LICENSE file).
-->

<!--
    A reusable template to display all time transfers.

    Usage:
    @template.components.list-timecheque-data-component(member = Member, timeCheques = List<TimeCheque>, transfersGiven = List<TimeTransfer>, transfersReceived = List<TimeTransfer>, selectedYear = int)

    Parameters:
    - timeCheques: List<TimeCheque>
    - transfersGiven: List<TimeTransfer>
    - transfersReceived: List<TimeTransfer>
    - selectedYear: int (the year to display data for, defaults to current year)

-->
@import java.util.List
@import java.time.LocalDate

@import static eu.nabahilfe.webapp.DateFormatter.*
@import static eu.nabahilfe.webapp.NumberFormatter.*
@import eu.nabahilfe.webapp.timecheques.TimeCheque
@import eu.nabahilfe.webapp.timetransfers.TimeTransfer

@import eu.nabahilfe.webapp.members.Member

@param Member member
@param List<TimeCheque> timeCheques
@param List<TimeTransfer> transfersGiven
@param List<TimeTransfer> transfersReceived
@param int selectedYear = LocalDate.now().getYear()


    <div class="row mb-3">
        <div class="col-auto d-flex align-items-center gap-2">
            <label for="yearSelect" class="form-label mb-0 fw-semibold">Jahr:</label>
            <select id="yearSelect" class="form-select form-select-sm" style="width: auto;"
                    onchange="reloadWithYear(this.value)">
                !{int currentYear = LocalDate.now().getYear();}
                @for(int y = currentYear; y >= 2020; y--)
                    <option value="${y}" selected="${y == selectedYear}">${y}</option>
                @endfor
            </select>
        </div>
    </div>

    <script>
        function reloadWithYear(year) {
            const url = new URL(window.location.href);
            url.searchParams.set('year', year);
            window.location.href = url.toString();
        }
    </script>

    <div class="row">
        <div class="col-sm-12 mb-3 mb-sm-0">
        
        

            <ul class="nav nav-tabs" id="memberDetailTabs" role="tablist">
                <li class="nav-item" role="presentation">
                    <button class="nav-link active" id="timecheques-tab" data-bs-toggle="tab" data-bs-target="#timecheques-pane" type="button" role="tab" aria-controls="timecheques-pane" aria-selected="true">
                        Zeitschecks gekauft
                    </button>
                </li>
                <li class="nav-item" role="presentation">
                    <button class="nav-link" id="transfers-tab" data-bs-toggle="tab" data-bs-target="#transfers-pane" type="button" role="tab" aria-controls="transfers-pane" aria-selected="false">
                        Zeitschecks übergeben
                    </button>
                </li>
                <li class="nav-item" role="presentation">
                    <button class="nav-link" id="received-tab" data-bs-toggle="tab" data-bs-target="#received-pane" type="button" role="tab" aria-controls="received-pane" aria-selected="false">
                        Zeitschecks erhalten
                    </button>
                </li>
            </ul>

            <div class="tab-content" id="memberDetailTabsContent">
                <div class="tab-pane fade show active" id="timecheques-pane" role="tabpanel" aria-labelledby="timecheques-tab" tabindex="0">
                    <div>
                        @template.components.list-timecheques-component(member = member, timeCheques = timeCheques)
                    </div>
                </div>
                <div class="tab-pane fade" id="transfers-pane" role="tabpanel" aria-labelledby="transfers-tab" tabindex="0">
                    <div>
                        @template.components.list-timetransfers-given-component(member = member, transfersGiven = transfersGiven)
                    </div>
                </div>
                <div class="tab-pane fade" id="received-pane" role="tabpanel" aria-labelledby="received-tab" tabindex="0">
                    <div>
                        @template.components.list-timetransfers-received-component(member = member, transfersReceived = transfersReceived)
                    </div>
                </div>
            </div>

        </div>

    </div>