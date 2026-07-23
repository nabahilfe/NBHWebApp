<!--
  Copyright (c) 2025–2026 Maximilian Weißböck
  Licensed under the MIT License (see LICENSE file).
-->

@import eu.nabahilfe.webapp.GlobalModelAttributes
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
@param java.lang.String geoValidationDisplayName = null

@param Integer numberOfTimecheques = null

@param java.lang.String joiningDateMin = null
@param java.lang.String resignationDateMax = null

@param int selectedYear = LocalDate.now().getYear()

@param String joiningDateMinCalculated = formatISO(LocalDate.now().minusMonths(2).withDayOfMonth(1))
@param String resignationDateMaxCalculated = formatISO(LocalDate.now().plusMonths(2).with(java.time.temporal.TemporalAdjusters.lastDayOfMonth()))

@param eu.nabahilfe.webapp.security.CsrfHiddenInput csrfHiddenInput
@param eu.nabahilfe.webapp.security.ViewContext ctxt
@param GlobalModelAttributes globalModelAttributes


@template.layout.mainpage(
    globalModelAttributes = globalModelAttributes,
    csrfHiddenInput = csrfHiddenInput,
    ctxt = ctxt,
    content = @`

    <h3>${member.getId() != null ? "Mitglied bearbeiten" : "Neues Mitglied anlegen"}</h3>

    <div class="row">

        @if(! member.isSozialkonto())
            <!-- first action is for save, second action is for update -->
            <form action="${member.getId() == null ? "/members" : "/members/" + member.getId()}" method="post">
                ${csrfHiddenInput}

                <div class="form-group row g-3">
                    <div class="col-12 col-xl-6">
                        <div class="card shadow h-100" style="background-color: #ECFEFF;">
                            <div class="card-body">

                                <h5>Persönliche Daten</h5>

                                <div class="row g-2 mb-1">
                                    <div class="col-6">
                                        <label for="salutation" class="form-label">Anrede</label>
                                        <select class="form-select" name="salutation" id="salutation">
                                            <option value="">Auswählen</option>
                                            @for(Salutation sal : Salutation.values())
                                                <option value="${sal.name()}" selected="${member.getSalutation() != null && member.getSalutation().equals(sal.name())}">
                                                    ${sal.name()}
                                                </option>
                                            @endfor
                                        </select>
                                    </div>
                                    <div class="col-6">
                                        <label for="title" class="form-label">Titel</label>
                                        <input type="text" class="form-control" name="title" id="title" value="${member.getTitle()}">
                                    </div>
                                </div>

                                <div class="row g-2 mb-1">
                                    <div class="col-6">
                                        <label for="firstName" class="form-label">Vorname<span class="text-danger">*</span></label>
                                        <input type="text" class="form-control" name="firstName" id="firstName" value="${member.getFirstName()}" required>
                                    </div>
                                    <div class="col-6">
                                        <label for="lastName" class="form-label">Nachname<span class="text-danger">*</span></label>
                                        <input type="text" class="form-control" name="lastName" id="lastName" value="${member.getLastName()}" required>
                                    </div>
                                </div>

                                <div class="row g-2 mb-1">
                                    <div class="col-4">
                                        <label for="birthdate" class="form-label">Geburtsdatum<span class="text-danger">*</span></label>
                                        <input type="date" class="form-control" name="birthdate" id="birthdate" value="${formatISO(member.getBirthdate())}" required>
                                    </div>
                                    <div class="col-4">
                                        <label for="joiningDate" class="form-label">Beitrittsdatum<span class="text-danger">*</span></label>
                                        @if(member.getId() != null)
                                            <input type="date" class="form-control" name="joiningDate" id="joiningDate"
                                                   value="${member.getJoiningDate() != null ? formatISO(member.getJoiningDate()) : ""}" readonly>
                                        @else
                                            <input type="date" class="form-control" name="joiningDate" id="joiningDate"
                                                   value="${member.getJoiningDate() != null ? formatISO(member.getJoiningDate()) : ""}"
                                                   min="${joiningDateMinCalculated}">
                                            <div class="form-text">Maximal 2 Monate in der Vergangenheit</div>
                                        @endif
                                    </div>
                                    <div class="col-4">
                                        <label for="resignationDate" class="form-label">Austrittsdatum</label>
                                        <input type="date" class="form-control" name="resignationDate" id="resignationDate"
                                               value="${member.getResignationDate() != null ? formatISO(member.getResignationDate()) : ""}"
                                               max="${resignationDateMaxCalculated}">
                                        <div class="form-text">Maximal 2 Monate in der Zukunft</div>
                                    </div>
                                </div>

                                <div class="row g-2 mb-1">
                                    <div class="col-4">
                                        <label for="role" class="form-label">Rolle</label>
                                        <select class="form-select" name="role" id="role">
                                            <option value=""></option>
                                            @for(Role role : roles)
                                                <option value="${role.getId()}" selected="${member.getRole() != null && member.getRole().getId() == role.getId()}">
                                                    ${role.getRoleName()}
                                                </option>
                                            @endfor
                                        </select>
                                        <div class="form-text">Leer lassen für einfache Mitglieder</div>
                                    </div>

                                    <div class="col-2">
                                    </div>

                                    <div class="form-check col-6 d-flex flex-column justify-content-end pb-1">
                                        <div>
                                            <input type="hidden" name="_directDebitAuthorization" value="on"/>
                                            <input class="form-check-input" type="checkbox"
                                                id="directDebitAuthorization" name="directDebitAuthorization"
                                                checked="${member.getDirectDebitAuthorization() != null && member.getDirectDebitAuthorization()}">
                                            <label class="form-check-label" for="directDebitAuthorization">
                                                <strong>Einziehungsauftrag erteilt</strong>
                                            </label>
                                            <div class="form-text">Auswählen wenn ein Einziehungsauftrag erteilt wurde</div>
                                        </div>
                                    </div>

                                </div>

                                @if(member.getId() != null)
                                    <br/>
                                    <b>Mitgliedsnummer: ${member.getMemberNmbr()}</b> -
                                    <b>Zeitguthaben: ${member.getAccumulatedHours() != null ? member.getAccumulatedHours() : 0} h</b>
                                @endif


                            </div>
                        </div>
                    </div>

                    <div class="col-12 col-xl-6">
                        <div class="card shadow h-100" style="background-color: #ECFEFF;">
                            <div class="card-body">

                                <h5>Kontaktdaten & Adresse</h5>

                                <div class="row g-2 mb-1">
                                    <div class="col-8">
                                        <label for="email" class="form-label">E-Mail</label>
                                        <input type="email" class="form-control" name="email" id="email"
                                               value="${member.getEmail() != null ? member.getEmail() : ""}"
                                               pattern="[a-zA-Z0-9._%+\-]+@[a-zA-Z0-9.\-]+\.[a-zA-Z]{2,}"
                                               title="Bitte eine gültige E-Mail-Adresse eingeben (z.B. name@beispiel.de).">
                                        <div class="form-text">Format: name@domain.tld</div>
                                    </div>
                                    <div class="col-4">
                                        <label for="phoneNumber" class="form-label">Telefon</label>
                                        <input type="tel" class="form-control" name="phoneNumber" id="phoneNumber"
                                               value="${member.getPhoneNumber() != null ? member.getPhoneNumber() : ""}"
                                               pattern="(\+[1-9][0-9]*( [1-9][0-9]*( [0-9]+)*)?|0[0-9 ]*)"
                                               title="Telefonnummer muss mit +Ländervorwahl (ohne 0, z.B. +49...) oder mit 0 beginnen und darf nur Ziffern und Leerzeichen enthalten.">
                                        <div class="form-text">Beginnt mit + oder 0</div>
                                    </div>
                                </div>

                                <label for="street" class="col col-form-label"><strong>Adresse</strong>
                                    @if(member.getLatitude() != null && member.getLongitude() != null)
                                        <span class="badge bg-success bi-geo-alt-fill"> gefunden</span>
                                    @else
                                        <span class="badge bg-danger bi-geo-alt"> nicht gefunden</span>
                                    @endif
                                </label>

                                @if(geoValidationDisplayName != null && !geoValidationDisplayName.isBlank())
                                    <div class="alert alert-warning small py-1 px-2 mb-2" role="alert">
                                        <i class="bi bi-exclamation-triangle-fill"></i>
                                        <strong>Die eingegebene Adresse konnte nicht eindeutig zugeordnet werden.
                                        OpenStreetMap fand stattdessen:</strong><br>
                                        @for(String line : geoValidationDisplayName.split("\n"))
                                            ${line}<br>
                                        @endfor
                                    </div>
                                @endif

                                <div class="col">
                                    <div class="border rounded p-3 mb-1">
                                        <div class="col-6">
                                            <label for="institution" class="form-label">Institution</label>
                                            <input type="text" class="form-control" name="institution" id="institut" value="${member.getInstitution()}">
                                        </div>

                                        <div class="row g-2">
                                            <div class="col-6">
                                                <label for="street" class="form-label pt-0">Straße<span class="text-danger">*</span></label>
                                                <input type="text" class="form-control" name="street" id="street" value="${member.getStreet()}" required>
                                            </div>
                                            <div class="col-2">
                                                <label for="number" class="form-label">Nummer<span class="text-danger">*</span></label>
                                                <input type="text" class="form-control" name="number" id="number" value="${member.getNumber()}" required>
                                            </div>


                                            <div class="col-2">
                                                <label for="stair" class="form-label">Stiege</label>
                                                <input type="text" class="form-control" name="stair" id="stair" value="${member.getStair() != null ? member.getStair() : ""}">
                                            </div>

                                            <div class="col-2">
                                                <label for="door" class="form-label">Tür</label>
                                                <input type="text" class="form-control" name="door" id="door" value="${member.getDoor() != null ? member.getDoor() : ""}">
                                            </div>



                                            <div class="col-4">
                                                <label for="zip" class="form-label">PLZ<span class="text-danger">*</span></label>
                                                <input type="text" class="form-control" name="zip" id="zip" value="${member.getZip()}" required>
                                            </div>
                                            <div class="col-8">
                                                <label for="city" class="form-label">Ort<span class="text-danger">*</span></label>
                                                <input type="text" class="form-control" name="city" id="city" value="${member.getCity()}" required>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                            </div>
                        </div>
                    </div>

                </div>

<!-- FIXME: only for Vorstand / Sysadmin - must show LocalDate!
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
                    <div class="alert alert-warning" role="alert">
                        <h3>Sozialkonto der Nachbarschaftshilfe</h3>
                        Zeitschecks können nur durch Spenden von anderen Mitgliedern hinzugefügt werden, nicht durch kaufen.
                        <br>Guthaben von ausgetreten Mitgliedern werden automatisch auf das Sozialkonto übertragen, durch direkte Zuweisung.
                        <br>Damit bleibt die Anonymität bei Austritt entsprechend DSGVO Vorgaben gewahrt.
                        <br>
                        <br><b>Zeitguthaben: ${member.getAccumulatedHours() != null ? member.getAccumulatedHours() : 0} h</b>
                        <br>
                        <br>Das Sozialkonto kann nicht bearbeitet oder gelöscht werden.
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





