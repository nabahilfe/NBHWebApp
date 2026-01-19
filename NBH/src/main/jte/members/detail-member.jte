@import java.util.List
@import java.time.LocalDate

@import static eu.nabahilfe.webapp.DateFormatter.*

@import eu.nabahilfe.webapp.members.Salutation
@import eu.nabahilfe.webapp.members.Member
@import eu.nabahilfe.webapp.members.Role
@import eu.nabahilfe.webapp.timecheques.TimeTransfer
@import eu.nabahilfe.webapp.timecheques.TimeCheque

@param Member member
@param List<Role> roles
@param List<TimeCheque> purchasedTimeCheques
@param List<TimeTransfer> timeTransfers

@param java.lang.String action

@param java.lang.String successMessage = null
@param java.lang.String errorMessage = null



@template.layout.main(content = @`

    <h3>
        ${member.getId() != null ? "Mitglied bearbeiten" : "Neues Mitglied anlegen"}
    </h3>

    <div class="row">

        <p/>

        <!-- first action is for save, second action is for update -->
        <form action="${member.getId() == null ? "/members" : "/members/" + member.getId()}" method="post">

            <div class="form-group row">
                <div class="col">
                <div class="card">
                <div class="card-body bg-success bg-opacity-10">

                    <h5>Persönliche Daten</h5>

                    <label for="salutation" class="col col-form-label">Anrede</label>
                    <div class="col">
                        <select class="form-select" name="salutation" id="salutation">
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
                        <input type="date" class="form-control" name="birthdate" id="birthdate" value="${member.getBirthdate() != null ? member.getBirthdate().toString() : ""}" required>
                    </div>

                    <label for="role" class="col col-form-label">Rolle</label>
                    <div class="col">
                        <select class="form-select" name="role" id="role">
                            <option value="">Einfaches Mitglied</option>
                            @for(Role role : roles)
                                <option value="${role.getId()}" selected="${member.getRole() != null && member.getRole().getId() == role.getId()}">
                                    ${role.getRoleName()}
                                </option>
                            @endfor
                        </select>
                    </div>

                    @if(member.getId() != null)
                        <br/>
                        <b>Zeitguthaben: ${member.getAccumulatedHours() != null ? member.getAccumulatedHours() + " h" : ""}</b>
                    @endif


                </div>
                </div>
                </div>

                <div class="col">
                <div class="card">
                <div class="card-body bg-success bg-opacity-10">

                    <h5>Kontaktdaten & Adresse</h5>

                    <label for="email" class="col col-form-label">E-Mail</label>
                    <div class="col">
                        <input type="text" class="form-control" name="email" id="email" value="${member.getEmail()}">
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


                </div>
                </div>
                </div>

            </div>



            <p/>

            <div class="form-group row">
                <div class="col-sm-10">
                    <button type="submit" class="btn btn-primary bi-floppy"> Speichern</button>
                </div>
            </div>

            <p/>

            @template.components.display-statusmessage(successMessage, errorMessage)

        </form>

    </div>

    <p/>

    @if(member.getId() != null)
        <div class="row">
            <div class="col-sm-3 mb-3 mb-sm-0">

                <!-- FIXME Das Template braucht auch den UserNamen um universell verwendbar zu sein
                @template.components.list-timecheques-component(timeCheques = purchasedTimeCheques)-->

            </div>

            <div class="col-sm-9 mb-3 mb-sm-0">

                <!-- FIXME: Daraus sollte nan eine Komponente machen!
                @template.components.list-timetransfers-component(transfers = timeTransfers)-->

            </div>
        </div>
    @endif

`)




