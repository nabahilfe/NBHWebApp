@import java.util.List
@import java.time.LocalDate

@import static eu.nabahilfe.webapp.DateFormatter.*

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

                            <!-- Hidden mirrors to submit disabled values -->
                            <input type="hidden" id="firstName-hidden">
                            <input type="hidden" id="lastName-hidden">
                            <input type="hidden" id="institution-hidden">
                            <input type="hidden" id="role-hidden">
                            <input type="hidden" id="title-hidden">
                            <input type="hidden" id="email-hidden">
                        </div>
                    </div>
                </div>

            </div>

            <br/>
            <p>
                <i>Letztes Update: ${dateTimeDE(member.getUpdatedAt())}</i>
            </p>

            <br/>

            <div class="form-group row">
                <div class="col-sm-10">
                    <button type="submit" class="btn btn-primary bi-floppy"> Speichern</button>
                </div>
            </div>



            <br/>

            @template.components.display-statusmessage(successMessage, errorMessage)

        </form>

    </div>


    @if(member.getId() != null)

        @if(member.getSalutation() != null && member.getSalutation().equals("SOZIALKONTO"))
            <div class="alert alert-warning py-2 px-3" role="alert">
                Sozialkonto der Nachbarschaftshilfe. Zeitschecks können nur durch Spenden von anderen Mitgliedern hinzugefügt werden, nicht durch kaufen.
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
            @endif
        @endif

        <br/>

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
                            @template.components.list-timecheques-component(member = member, timeCheques = purchasedTimeCheques)
                        </div>
                    </div>
                    <div class="tab-pane fade" id="transfers-pane" role="tabpanel" aria-labelledby="transfers-tab" tabindex="0">
                        <div>
                            @template.components.list-timetransfers-given-component(member = member, transfers = givenTimeTransfers)
                        </div>
                    </div>
                    <div class="tab-pane fade" id="received-pane" role="tabpanel" aria-labelledby="received-tab" tabindex="0">
                        <div>
                            @template.components.list-timetransfers-received-component(member = member, transfers = receivedTimeTransfers)
                        </div>
                    </div>
                </div>


            </div>
        </div>
    @endif

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

            const firstNameHidden = document.getElementById('firstName-hidden');
            const lastNameHidden = document.getElementById('lastName-hidden');
            const institutionHidden = document.getElementById('institution-hidden');
            const roleHidden = document.getElementById('role-hidden');
            const titleHidden = document.getElementById('title-hidden');
            const emailHidden = document.getElementById('email-hidden');

            const isExisting = ${member.getId() != null};
            const savedSalutation = '${member.getSalutation() != null ? member.getSalutation() : ""}';

            // Preserve user-entered values when toggling Sozialkonto on/off
            const snapshot = {
                title: title.value,
                firstName: firstName.value,
                lastName: lastName.value,
                email: email.value,
                institution: institution.value,
                role: role.value,
                directDebitAuthorization: directDebitAuthorization.checked
            };

            const mirror = (field, hidden, enabled) => {
                if (enabled) {
                    hidden.removeAttribute('name');
                    hidden.value = '';
                    field.disabled = false;
                } else {
                    hidden.name = field.name;
                    hidden.value = field.value;
                    field.disabled = true;
                }
            };

            const applySocialLock = (enable) => {
                if (enable) {
                    snapshot.title = title.value;
                    snapshot.firstName = firstName.value;
                    snapshot.lastName = lastName.value;
                    snapshot.email = email.value;
                    snapshot.institution = institution.value;
                    snapshot.role = role.value;
                    snapshot.directDebitAuthorization = directDebitAuthorization.checked;

                    title.value = '';
                    institution.value = 'Nachbarschaftshilfe';
                    firstName.value = 'SOZIALKONTO';
                    lastName.value = 'NACHBARSCHAFTSHILFE';
                    role.value = '';

                    mirror(title, titleHidden, false);
                    mirror(institution, institutionHidden, false);
                    mirror(firstName, firstNameHidden, false);
                    mirror(lastName, lastNameHidden, false);
                    mirror(role, roleHidden, false);

                    directDebitAuthorization.disabled = true;
                    directDebitAuthorization.checked = false;
                } else {
                    mirror(title, titleHidden, true);
                    mirror(institution, institutionHidden, true);
                    mirror(firstName, firstNameHidden, true);
                    mirror(lastName, lastNameHidden, true);
                    mirror(role, roleHidden, true);

                    title.value = snapshot.title;
                    institution.value = snapshot.institution;
                    firstName.value = snapshot.firstName;
                    lastName.value = snapshot.lastName;
                    role.value = snapshot.role;

                    directDebitAuthorization.disabled = false;
                    directDebitAuthorization.checked = snapshot.directDebitAuthorization;
                }
            };

            const enforceSalutationOptions = () => {
                const options = Array.from(salutation.options);
                if (!isExisting) {
                    return;
                }

                if (savedSalutation === 'SOZIALKONTO') {
                    options.forEach(opt => {
                        opt.disabled = opt.value !== 'SOZIALKONTO';
                        opt.hidden = opt.value !== 'SOZIALKONTO';
                    });
                    salutation.value = 'SOZIALKONTO';
                    salutation.disabled = true;
                } else {
                    options
                        .filter(opt => opt.value === 'SOZIALKONTO')
                        .forEach(opt => opt.remove());
                }
            };

            const handleSalutationChange = () => {
                const isSocial = salutation.value === 'SOZIALKONTO';
                applySocialLock(isSocial);
            };

            enforceSalutationOptions();
            handleSalutationChange();
            salutation.addEventListener('change', handleSalutationChange);
        })();
    </script>

`)




