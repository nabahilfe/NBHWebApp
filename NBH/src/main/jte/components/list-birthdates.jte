<!--
  Copyright (c) 2025–2026 Maximilian Weißböck
  Licensed under the MIT License (see LICENSE file).
-->

@import java.util.List

@import eu.nabahilfe.webapp.members.MemberBirthdayForm
@import static eu.nabahilfe.webapp.system.DateFormatter.*


@param List<MemberBirthdayForm> currentMonth
@param List<MemberBirthdayForm> nextMonth

<div class="row g-3">
    <div class="col-12 col-lg-6">
        <div class="card">
            <div class="card-header bg-info bg-opacity-10 d-flex justify-content-between align-items-center">
                <strong>Geburtstage diesen Monat</strong>
                @if(currentMonth.isEmpty())
                    <button type="button" class="btn btn-sm btn-outline-primary" disabled>PDF erstellen</button>
                @else
                    <a href="/members/birthdays/pdf" class="btn btn-sm btn-outline-primary">PDF erstellen</a>
                @endif
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
        <div class="card">
            <div class="card-header bg-info bg-opacity-10 d-flex justify-content-between align-items-center">
                <strong>Geburtstage nächsten Monat</strong>
                @if(nextMonth == null || nextMonth.isEmpty())
                    <button type="button" class="btn btn-sm btn-outline-primary" disabled>PDF erstellen</button>
                @else
                    <a href="/members/birthdays/pdf?monthOffset=1" class="btn btn-sm btn-outline-primary">PDF erstellen</a>
                @endif
            </div>
            <div class="card-body p-0">
                @if(nextMonth == null || nextMonth.isEmpty())
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
                                @for(MemberBirthdayForm m : nextMonth)
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