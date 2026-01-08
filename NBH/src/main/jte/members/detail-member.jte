@import java.util.List
@import java.time.LocalDate

@import static eu.nabahilfe.webapp.DateFormatter.*

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

            <label for="firstName" class="col-sm-2 col-form-label">Vorname<span class="text-danger">*</span></label>
            <div class="col-sm-10">
              <input type="text" class="form-control" name="firstName" id="firstName" value="${member.getFirstName()}" required>
            </div>

            <label for="lastName" class="col-sm-2 col-form-label">Nachname<span class="text-danger">*</span></label>
            <div class="col-sm-10">
              <input type="text" class="form-control" name="lastName" id="lastName" value="${member.getLastName()}" required>
            </div>

            <label for="birthdate" class="col-sm-2 col-form-label">Gebutsdatum<span class="text-danger">*</span></label>
            <div class="col-sm-10">
              <input type="date" class="form-control" name="birthdate" id="birthdate" value="${member.getBirthdate() != null ? member.getBirthdate().toString() : ""}" required>
            </div>

          </div>

            <p/>

          <div class="form-group row">

            <label for="street" class="col-sm-2 col-form-label">Straße<span class="text-danger">*</span></label>
            <div class="col-sm-10">
              <input type="text" class="form-control" name="street" id="street" value="${member.getStreet()}" required>
            </div>

            <label for="number" class="col-sm-2 col-form-label">Nummer<span class="text-danger">*</span></label>
            <div class="col-sm-10">
              <input type="text" class="form-control" name="number" id="number" value="${member.getNumber()}" required>
            </div>

            <label for="zip" class="col-sm-2 col-form-label">PLZ<span class="text-danger">*</span></label>
            <div class="col-sm-10">
              <input type="text" class="form-control" name="zip" id="zip" value="${member.getZip()}" required>
            </div>

            <label for="city" class="col-sm-2 col-form-label">Ort<span class="text-danger">*</span></label>
            <div class="col-sm-10">
              <input type="text" class="form-control" name="city" id="city" value="${member.getCity()}" required>
            </div>

          </div>
            <p/>
          <div class="form-group row">

            <label for="role" class="col-sm-2 col-form-label">Rolle</label>
            <div class="col-sm-10">
                <select class="form-select" name="roleId" id="role">

                    <option value="">Einfaches Mitglied</option>

                    @for(Role role : roles)
                        <option value="${role.getId()}" selected="${member.getRole() != null && member.getRole().getId() == role.getId()}">
                            ${role.getRoleName()}
                        </option>
                    @endfor
                </select>
             </div>

            <p/>

            <b>Zeitguthaben: ${member.getAccumulatedHours()} h</b>

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

         <!-- FIXME: Daraus sollte nan eine Komponente machen! -->

        <div class="card">
          <div class="card-body">
            <h5>Erworbene Zeitschecks</h5>
            <p class="card-text">Nur die letzten 10</p>

            <table class="table table-hover table-sm" id="memberTable">
              <thead>
                <tr>
                  <th scope="col">Datum</th>
                  <th scope="col" class="text-end">Stunden</th>
                </tr>
              </thead>
              <tbody class="table-group-divider">
                @for (TimeCheque tc : purchasedTimeCheques)
                <tr>
                  <td scope="row">${formatDE(tc.getOrderDate())}</td>
                  <td scope="row" class="text-end">${tc.getHours()}</td>
                </tr>
                @endfor
              </tbody>
            </table>

          </div>
        </div>
      </div>



      <div class="col-sm-9 mb-3 mb-sm-0">

        <!-- FIXME: Daraus sollte nan eine Komponente machen! -->

        @template.components.timetransfers(transfers = timeTransfers)


      </div>
    </div>
    @endif
`)



