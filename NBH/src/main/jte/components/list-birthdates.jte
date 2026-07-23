<!--
  Copyright (c) 2025–2026 Maximilian Weißböck
  Licensed under the MIT License (see LICENSE file).
-->

@import eu.nabahilfe.webapp.GlobalModelAttributes
@import java.util.List
@import java.time.LocalDate

@import eu.nabahilfe.webapp.members.MemberBirthdayForm
@import static eu.nabahilfe.webapp.DateFormatter.*


@param List<MemberBirthdayForm> currentMonth
@param List<MemberBirthdayForm> nexttMonth

<div class="row g-3">
    <div class="col-12 col-lg-6">
        <div class="card h-100">
            <div class="card-header bg-info bg-opacity-10">
                <strong>Geburtstage diesen Monat</strong>
            </div>
            <div class="card-body p-0">
                @if(currentMonth == null || currentMonth.isEmpty())
                    <div class="p-3 text-muted">Keine Geburtstage.</div>
                @else
                    <div class="table-responsive">
                        <table class="table mb-0">
                            <thead>
                                <tr>
                                    <th>Name</th>
                                    <th class="text-center">Geburtsdatum</th>
                                    <th class="text-center">Alter</th>
                                </tr>
                            </thead>
                            <tbody>
                                @for(MemberBirthdayForm m : currentMonth)
                                    <tr>
                                        <td>${m.getName()}</td>
                                        <td class="text-center">${dateReverseDE(m.getBirthdate())}</td>
                                        <td class="text-center">${m.getAge()}</td>
                                    </tr>
                                @endfor
                            </tbody>
                        </table>
                    </div>
                @endif
            </div>
        </div>
    </div>

    <div class="col-12 col-lg-6">
        <div class="card h-100">
            <div class="card-header bg-info bg-opacity-10">
                <strong>Geburtstage nächsten Monat</strong>
            </div>
            <div class="card-body p-0">
                @if(nexttMonth == null || nexttMonth.isEmpty())
                    <div class="p-3 text-muted">Keine Geburtstage.</div>
                @else
                    <div class="table-responsive">
                        <table class="table mb-0">
                            <thead>
                                <tr>
                                    <th>Name</th>
                                    <th class="text-center">Geburtsdatum</th>
                                    <th class="text-center">Alter</th>
                                </tr>
                            </thead>
                            <tbody>
                                @for(MemberBirthdayForm m : nexttMonth)
                                    <tr>
                                        <td>${m.getName()}</td>
                                        <td class="text-center">${dateReverseDE(m.getBirthdate())}</td>
                                        <td class="text-center">${m.getAge()}</td>
                                    </tr>
                                @endfor
                            </tbody>
                        </table>
                    </div>
                @endif
            </div>
        </div>
    </div>
</div>