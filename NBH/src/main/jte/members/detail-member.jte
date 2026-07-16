<!--
  Copyright (c) 2025–2026 Maximilian Weißböck
  Licensed under the MIT License (see LICENSE file).
-->

@import java.util.List
@import java.time.LocalDate

@import static eu.nabahilfe.webapp.DateFormatter.*
@import eu.nabahilfe.webapp.NbhConst

@import eu.nabahilfe.webapp.members.Salutation
@import eu.nabahilfe.webapp.members.Member
@import eu.nabahilfe.webapp.members.Role
@import eu.nabahilfe.webapp.timetransfers.TimeTransfer
@import eu.nabahilfe.webapp.timecheques.TimeCheque

@param Member member
@param List<Role> roles
@param List<TimeCheque> purchasedTimeCheques
@param List<TimeTransfer> receivedTimeTransfers
@param List<TimeTransfer> givenTimeTransfers

@param java.lang.String action

@param java.lang.String successMessage = null
@param java.lang.String errorMessage = null

@param Integer numberOfTimecheques = null

@param java.lang.String joiningDateMin = null
@param java.lang.String resignationDateMax = null

@param int selectedYear = LocalDate.now().getYear()

@param String joiningDateMinCalculated = formatISO(LocalDate.now().minusMonths(2).withDayOfMonth(1))
@param String resignationDateMaxCalculated = formatISO(LocalDate.now().plusMonths(2).with(java.time.temporal.TemporalAdjusters.lastDayOfMonth()))

@param eu.nabahilfe.webapp.security.CsrfHiddenInput csrfHiddenInput
@param eu.nabahilfe.webapp.security.ViewContext ctxt


@template.layout.mainpage(
    csrfHiddenInput = csrfHiddenInput,
    ctxt = ctxt,
    content = @`

    <h3>
        ${member.getId() != null ? "Mitglied bearbeiten" : "Neues Mitglied anlegen"}
    </h3>

    <div class="row">

        <br/>

        @if(! member.isSozialkonto())
            <!-- first action is for save, second action is for update -->
            <form action="${member.getId() == null ? "/members" : "/members/" + member.getId()}" method="post">
                ${csrfHiddenInput}

                <div class="form-group row">
                    <div class="col">
                        <div class="card shadow" style="background-color: #ECFEFF;"">
                            <div class="card-body">

                                <h5>Persönliche Daten</h5>

                                <label for="salutation" class="col col-form-label">Anrede</label>
                                <div class="col">
                                    <select class="form-select" name="salutation" id="salutation">
                                        <option value="">Auswählen</option>
                                        @for(Salutation sal : Salutation.values())
                                            <option value="${sal.name()}" selected="${member.getSalutation() != null && member.getSalutation().equals(sal.name())}">
                                                ${sal.name()}
                                            </option>
                                        @endfor
                                    </select>
                                </div>

                                <label for="title" class="col col-form-label">Titel</label>
                                <div class="col">
                                    <input type="text" class="form-control" name="title" id="title" value="${member.getTitle()}">
                                </div>

                                <label for="firstName" class="col col-form-label">Vorname<span class="text-danger">*</span></label>
                                <div class="col">
                                    <input type="text" class="form-control" name="firstName" id="firstName" value="${member.getFirstName()}" required>
                                </div>

                                <label for="lastName" class="col col-form-label">Nachname<span class="text-danger">*</span></label>
                                <div class="col">
                                    <input type="text" class="form-control" name="lastName" id="lastName" value="${member.getLastName()}" required>
                                </div>

                                <label for="birthdate" class="col col-form-label">Geburtsdatum<span class="text-danger">*</span></label>
                                <div class="col">
                                    <input type="date" class="form-control" name="birthdate" id="birthdate" value="${formatISO(member.getBirthdate())}" required>
                                </div>

                                <label for="joiningDate" class="col col-form-label">Beitrittsdatum</label>
                                <div class="col">
                                    @if(member.getId() != null)
                                        <input type="date" class="form-control" name="joiningDate" id="joiningDate"
                                               value="${member.getJoiningDate() != null ? formatISO(member.getJoiningDate()) : ""}" readonly>
                                    @else
                                        <input type="date" class="form-control" name="joiningDate" id="joiningDate"
                                               value="${member.getJoiningDate() != null ? formatISO(member.getJoiningDate()) : ""}"
                                               min="${joiningDateMinCalculated}">
                                        <div class="form-text">Maximal 2 Monate in der Vergangenheit (bis zum 1. des Monats).</div>
                                    @endif
                                </div>

                                <label for="resignationDate" class="col col-form-label">Austrittsdatum</label>
                                <div class="col">
                                    <input type="date" class="form-control" name="resignationDate" id="resignationDate"
                                           value="${member.getResignationDate() != null ? formatISO(member.getResignationDate()) : ""}"
                                           max="${resignationDateMaxCalculated}">
                                    <div class="form-text">Maximal 2 Monate in der Zukunft (bis zum Monatsende).</div>
                                </div>

                                <label for="role" class="col col-form-label">Rolle</label>
                                <div class="col">
                                    <select class="form-select" name="role" id="role">
                                        <option value=""></option>
                                        @for(Role role : roles)
                                            <option value="${role.getId()}" selected="${member.getRole() != null && member.getRole().getId() == role.getId()}">
                                                ${role.getRoleName()}
                                            </option>
                                        @endfor
                                    </select>
                                </div>

                                @if(member.getId() != null)
                                    <br/>
                                    <b>Mitgliedsnummer: ${member.getMemberNmbr()}</b> -
                                    <b>Zeitguthaben: ${member.getAccumulatedHours() != null ? member.getAccumulatedHours() : 0} h</b>
                                @endif


                            </div>
                        </div>
                    </div>

                    <div class="col">
                        <div class="card shadow" style="background-color: #ECFEFF;"">
                            <div class="card-body">

                                <h5>Kontaktdaten & Adresse</h5>

                                <label for="email" class="col col-form-label">E-Mail</label>
                                <div class="col">
                                    <input type="email" class="form-control" name="email" id="email"
                                           value="${member.getEmail() != null ? member.getEmail() : ""}"
                                           pattern="[a-zA-Z0-9._%+\-]+@[a-zA-Z0-9.\-]+\.[a-zA-Z]{2,}"
                                           title="Bitte eine gültige E-Mail-Adresse eingeben (z.B. name@beispiel.de).">
                                    <div class="form-text">Format: name@domain.tld</div>
                                </div>

                                <label for="phoneNumber" class="col col-form-label">Telefon</label>
                                <div class="col">
                                    <input type="tel" class="form-control" name="phoneNumber" id="phoneNumber"
                                           value="${member.getPhoneNumber() != null ? member.getPhoneNumber() : ""}"
                                           pattern="(\+[1-9][0-9]*( [1-9][0-9]*( [0-9]+)*)?|0[0-9 ]*)"
                                           title="Telefonnummer muss mit +Ländervorwahl (ohne 0, z.B. +49...) oder mit 0 beginnen und darf nur Ziffern und Leerzeichen enthalten.">
                                    <div class="form-text">Format: beginnt mit + (dann keine 0 für die Vorwahl) oder 0, nur Ziffern und Leerzeichen erlaubt.</div>
                                </div>

                                <label for="institution" class="col col-form-label">Institution</label>
                                <div class="col">
                                    <input type="text" class="form-control" name="institution" id="institut" value="${member.getInstitution()}">
                                </div>

                                <label for="street" class="col col-form-label">Straße<span class="text-danger">*</span></label>
                                <div class="col">
                                    <input type="text" class="form-control" name="street" id="street" value="${member.getStreet()}" required>
                                </div>

                                <label for="number" class="col col-form-label">Nummer<span class="text-danger">*</span></label>
                                <div class="col">
                                    <input type="text" class="form-control" name="number" id="number" value="${member.getNumber()}" required>
                                </div>

                                <label for="zip" class="col col-form-label">PLZ<span class="text-danger">*</span></label>
                                <div class="col">
                                    <input type="text" class="form-control" name="zip" id="zip" value="${member.getZip()}" required>
                                </div>

                                <label for="city" class="col col-form-label">Ort<span class="text-danger">*</span></label>
                                <div class="col">
                                    <input type="text" class="form-control" name="city" id="city" value="${member.getCity()}" required>
                                </div>

                                <br/>

                                <div class="form-check">
                                    <input type="hidden" name="_directDebitAuthorization" value="on"/>
                                    <input class="form-check-input" type="checkbox"
                                        id="directDebitAuthorization" name="directDebitAuthorization"
                                        checked="${member.getDirectDebitAuthorization() != null && member.getDirectDebitAuthorization()}">
                                    <label class="form-check-label" for="directDebitAuthorization">
                                      Einziehungsauftrag erteilt
                                    </label>
                                </div>


                            </div>
                        </div>
                    </div>

                </div>

<!-- FIXME: only for Vortsnad / Sysadmin - must show LocalDate!
                <br/>
                <p>
                    <i>Letztes Update: ${dateTimeDE(member.getUpdatedAt())}</i>
                </p>
-->

                <br/>

                <div class="form-group row">
                    <div class="col-sm-10">
                        <button type="submit" class="btn btn-primary bi-floppy"> Speichern</button>
                    </div>
                </div>

            </form>

        @endif

        <div>
            <br/>
            @template.components.display-statusmessage(successMessage, errorMessage)
        </div>


        <!-- Only show timecheque info if the member already exists (has an ID) -->
        @if(member.getId() != null)

            <div>

                @if(member.isSozialkonto())
                    <div class="alert alert-warning py-2 px-3" role="alert">
                        <p>
                            <h3>Sozialkonto der Nachbarschaftshilfe</h3>
                            <br>Zeitschecks können nur durch Spenden von anderen Mitgliedern hinzugefügt werden, nicht durch kaufen.
                            <br>Guthaben von ausgetreten Mitgliedern werden automatisch auf das Sozialkonto übertragen, durch direkte Zuweisung.
                            <br>Damit bleibt die Anonymität bei Austritt entsprechend DSGVO Vorgaben gewahrt.
                        </p>
                        <p>
                            <b>Zeitguthaben: ${member.getAccumulatedHours() != null ? member.getAccumulatedHours() : 0} h</b>
                        </p>
                        Das Sozialkonto kann nicht bearbeitet oder gelöscht werden.
                    </div>
                @else
                    @if(numberOfTimecheques != null && numberOfTimecheques == 0 && member.isImportedMember() == false)
                        <div class="alert alert-info py-2 px-3" role="alert">
                            Dieses Mitglied hat noch keine Zeitschecks. Erfasse gleich den ersten Gratis-Zeitscheck über den Button unten.
                        </div>
                        <form action="/timecheques/new" method="get">
                            <input type="hidden" name="memberId" value="${member.getId()}">
                            <button type="submit" class="btn btn-outline-primary bi-plus-square"> Ersten Zeitscheck erfassen</button>
                        </form>
                        <br/>
                    @endif
                @endif


                <hr/>

                <h5>Zeitschecks</h5>

                @template.components.list-timecheque-data-component(member = member, timeCheques = purchasedTimeCheques, transfersGiven = givenTimeTransfers,
                        transfersReceived = receivedTimeTransfers, selectedYear = selectedYear)

            </div>


        @endif


    </div>



    <script>
        (function() {
            // Phone number validation on blur/submit only
            const phoneInput = document.getElementById('phoneNumber');
            if (phoneInput) {
                const phonePattern = /^(\+[1-9][0-9 ]*|0[0-9 ]*)$/;

                phoneInput.addEventListener('blur', function() {
                    const val = this.value.trim();
                    this.value = val;
                    if (val && !phonePattern.test(val)) {
                        this.setCustomValidity('Telefonnummer muss mit +Ländervorwahl (ohne 0, z.B. +49 699 ...) oder mit 0 beginnen und darf nur Ziffern und Leerzeichen enthalten.');
                        this.reportValidity();
                    } else {
                        this.setCustomValidity('');
                    }
                });

                phoneInput.addEventListener('input', function() {
                    this.setCustomValidity('');
                });
            }

            // Email validation
            const emailInput = document.getElementById('email');
            if (emailInput) {
                const emailPattern = /^[a-zA-Z0-9._%+\-]+@[a-zA-Z0-9.\-]+\.[a-zA-Z]{2,}$/;

                emailInput.addEventListener('input', function() {
                    // Strip whitespace silently as the user types
                    this.value = this.value.replace(/\s/g, '');
                    this.setCustomValidity('');
                });

                emailInput.addEventListener('blur', function() {
                    if (this.value && !emailPattern.test(this.value)) {
                        this.setCustomValidity('Bitte eine gültige E-Mail-Adresse eingeben (z.B. name@beispiel.de).');
                        this.reportValidity();
                    } else {
                        this.setCustomValidity('');
                    }
                });
            }
        })();
    </script>

`)





