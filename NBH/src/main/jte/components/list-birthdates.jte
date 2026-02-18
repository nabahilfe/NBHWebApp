@import java.util.List
@import java.time.LocalDate

@import eu.nabahilfe.webapp.members.MemberBirthdayForm
@import static eu.nabahilfe.webapp.DateFormatter.*


@param List<MemberBirthdayForm> currentMonth
@param List<MemberBirthdayForm> nexttMonth

<div class="row g-3">
    <div class="col-12 col-lg-6">
        <div class="card h-100">
            <div class="card-header bg-success bg-opacity-10">
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
                                    <th>Nachname</th>
                                    <th>Vorname</th>
                                    <th>Geburtsdatum</th>
                                    <th>Alter</th>
                                </tr>
                            </thead>
                            <tbody>
                                @for(MemberBirthdayForm m : currentMonth)
                                    <tr>
                                        <td>${m.getLastName()}</td>
                                        <td>${m.getFirstName()}</td>
                                        <td>${dateDE(m.getBirthdate())}</td>
                                        <td>${m.getAge()}</td>
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
                                    <th>Nachname</th>
                                    <th>Vorname</th>
                                    <th>Geburtsdatum</th>
                                    <th>Alter</th>
                                </tr>
                            </thead>
                            <tbody>
                                @for(MemberBirthdayForm m : nexttMonth)
                                    <tr>
                                        <td>${m.getLastName()}</td>
                                        <td>${m.getFirstName()}</td>
                                        <td>${dateDE(m.getBirthdate())}</td>
                                        <td>${m.getAge()}</td>
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
