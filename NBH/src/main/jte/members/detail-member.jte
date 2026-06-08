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

                                <label for="birthdate" class="col col-form-label">Gebutsdatum<span class="text-danger">*</span></label>
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
                                               min="${joiningDateMin != null ? joiningDateMin : ""}"
                                               placeholder="JJJJ-MM-01">
                                        <div class="form-text">Muss immer der 1. eines Monats sein, max. 3 Monate in der Vergangenheit.</div>
                                    @endif
                                </div>

                                <label for="resignationDate" class="col col-form-label">Austrittsdatum</label>
                                <div class="col">
                                    <input type="date" class="form-control" name="resignationDate" id="resignationDate"
                                           value="${member.getResignationDate() != null ? formatISO(member.getResignationDate()) : ""}"
                                           max="${resignationDateMax != null ? resignationDateMax : ""}">
                                    <div class="form-text">Muss immer der letzte Tag eines Monats sein, max. 3 Monate in der Zukunft.</div>
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
                                           pattern="[+0][0-9 ]*"
                                           title="Telefonnummer muss mit + oder 0 beginnen und darf nur Ziffern und einzelne Leerzeichen enthalten.">
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
                            Sozialkonto der Nachbarschaftshilfe. Zeitschecks können nur durch Spenden von anderen Mitgliedern hinzugefügt werden, nicht durch kaufen.
                        </p>
                        <p>
                            <b>Zeitguthaben: ${member.getAccumulatedHours() != null ? member.getAccumulatedHours() : 0} h</b>
                        </p>
                        Das Sozialkonto kann nicht bearbeitet oder gelöscht werden.
                    </div>
                @else
                    @if(numberOfTimecheques != null && numberOfTimecheques == 0 && member.getIsImportedMember() == false)
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

                <h5>Zeitscheck-Käufe und Zeitscheck-Transfers</h5>

                @template.components.list-timecheque-data-component(member = member, timeCheques = purchasedTimeCheques, transfersGiven = givenTimeTransfers,
                        transfersReceived = receivedTimeTransfers, selectedYear = selectedYear)

            </div>


        @endif


    </div>



    <script>
        (function() {
            // Phone number validation: must start with + or 0, only digits and single spaces
            const phoneInput = document.getElementById('phoneNumber');
            if (phoneInput) {
                phoneInput.addEventListener('input', function() {
                    let val = this.value;
                    // Remove any character that is not +, digit or space
                    val = val.replace(/[^+0-9 ]/g, '');
                    // Ensure it starts with + or 0 only
                    if (val.length > 0 && val[0] !== '+' && val[0] !== '0') {
                        val = val.substring(1);
                    }
                    // + only allowed at position 0
                    if (val.length > 0) val = val[0] + val.substring(1).replace(/\+/g, '');
                    // Collapse consecutive spaces into a single space
                    val = val.replace(/ {2,}/g, ' ');
                    this.value = val;
                });

                phoneInput.addEventListener('blur', function() {
                    const pattern = /^[+0][0-9 ]*$/;
                    if (this.value && !pattern.test(this.value)) {
                        this.setCustomValidity('Telefonnummer muss mit + oder 0 beginnen und darf nur Ziffern und einzelne Leerzeichen enthalten.');
                        this.reportValidity();
                    } else {
                        this.setCustomValidity('');
                    }
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

            const title = document.getElementById('title');
            const firstName = document.getElementById('firstName');
            const lastName = document.getElementById('lastName');
            const email = document.getElementById('email');
            const institution = document.getElementById('institut');
            const role = document.getElementById('role');
            const directDebitAuthorization = document.getElementById('directDebitAuthorization');
            const birthdate = document.getElementById('birthdate');
            const joiningDate = document.getElementById('joiningDate');
            const resignationDate = document.getElementById('resignationDate');
            const street = document.getElementById('street');
            const numberField = document.getElementById('number');
            const zip = document.getElementById('zip');
            const city = document.getElementById('city');

            const isExisting = ${member.getId() != null};
            const savedSalutation = '${member.getSalutation() != null ? member.getSalutation() : ""}';

            const applySocialLock = (enable) => {
                if (enable) {
                    // apply fixed Sozialkonto values; readOnly keeps them in the POST
                    if (title)       { title.value = '';                                        title.readOnly = true; }
                    if (firstName)   { firstName.value = '${NbhConst.SOZIALKONTO_FIRST_NAME}';  firstName.readOnly = true; }
                    if (lastName)    { lastName.value  = '${NbhConst.SOZIALKONTO_LAST_NAME}';   lastName.readOnly  = true; }
                    if (institution) { institution.value = '${NbhConst.SOZIALKONTO_LAST_NAME}'; institution.readOnly = true; }
                    if (role)        { role.value = ''; role.disabled = true; }

                    if (directDebitAuthorization) { directDebitAuthorization.disabled = true; directDebitAuthorization.checked = false; }

                    if (birthdate) { birthdate.value = '2000-01-01'; birthdate.readOnly = true; }
                    if (joiningDate) {
                        const now = new Date();
                        const mm = String(now.getMonth() + 1).padStart(2, '0');
                        joiningDate.value = now.getFullYear() + '-' + mm + '-01';
                        joiningDate.readOnly = true;
                        joiningDate.removeAttribute('min');
                    }
                    if (resignationDate) { resignationDate.value = ''; resignationDate.readOnly = true; }
                    if (phoneInput)  { phoneInput.readOnly  = true; }
                    if (email)       { email.readOnly       = true; }
                    if (street)      { street.value = '-';      street.readOnly      = true; }
                    if (numberField) { numberField.value = '-'; numberField.readOnly = true; }
                    if (zip)         { zip.value = '-';         zip.readOnly         = true; }
                    if (city)        { city.value = '-';        city.readOnly        = true; }

                } else {
                    // just unlock — never touch .value; the server-rendered values are already correct
                    if (title)       title.readOnly = false;
                    if (firstName)   firstName.readOnly = false;
                    if (lastName)    lastName.readOnly  = false;
                    if (institution) institution.readOnly = false;
                    if (role)        { role.disabled = false; }

                    if (directDebitAuthorization) directDebitAuthorization.disabled = false;

                    if (birthdate)       birthdate.readOnly       = false;
                    if (joiningDate)     joiningDate.readOnly     = false;
                    if (resignationDate) resignationDate.readOnly = false;
                    if (phoneInput)      phoneInput.readOnly      = false;
                    if (email)           email.readOnly           = false;
                    if (street)          street.readOnly          = false;
                    if (numberField)     numberField.readOnly     = false;
                    if (zip)             zip.readOnly             = false;
                    if (city)            city.readOnly            = false;
                }
            };

            const enforceSalutationOptions = () => {
                const options = Array.from(salutation.options);
                if (!isExisting) {
                    return;
                }

                if (savedSalutation === '${NbhConst.SOZIALKONTO_SALUTATION}') {
                    options.forEach(opt => {
                        opt.disabled = opt.value !== '${NbhConst.SOZIALKONTO_SALUTATION}';
                        opt.hidden = opt.value !== '${NbhConst.SOZIALKONTO_SALUTATION}';
                    });
                    salutation.value = '${NbhConst.SOZIALKONTO_SALUTATION}';
                    salutation.disabled = true;
                } else {
                    options
                        .filter(opt => opt.value === '${NbhConst.SOZIALKONTO_SALUTATION}')
                        .forEach(opt => opt.remove());
                }
            };

            const handleSalutationChange = () => {
                const isSocial = salutation.value === '${NbhConst.SOZIALKONTO_SALUTATION}';
                applySocialLock(isSocial);
            };

            enforceSalutationOptions();
            handleSalutationChange();
            salutation.addEventListener('change', handleSalutationChange);
        })();
    </script>

`)





