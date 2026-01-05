@import eu.nabahilfe.webapp.members.Role

@param eu.nabahilfe.webapp.members.Role role

@param java.lang.String name

@param java.lang.String successMessage = null
@param java.lang.String errorMessage = null


@template.layout.main(content = @`

    <h3 class="text-primary">
        ${role.getId() != null ? "Rolle bearbeiten" : "Neue Rolle anlegen"}
    </h3>

    <div class="row">

        <p/>

        <!-- first action is for save, second action is for update -->
        <form action="${role.getId() == null ? "/roles" : "/roles/" + role.getId()}" method="post">

          <div class="form-group row">
            <label for="roleName" class="col-sm-2 col-form-label">Rollenbezeichnung <span class="text-danger">*</span></label>
            <div class="col-sm-10">
              <input type="text" class="form-control"
                      name="roleName"
                      id="roleName"
                      aria-describedby="roleNameHelp" placeholder="Bezeichnung der Rolle"
                      value="${role.getRoleName()}" required>
              <small id="roleNameHelp" class="form-text text-muted">Obmann, Obfrau, Kassier, Rechnungsprüfer, ...</small>
            </div>
          </div>

          <p/>

          <div class="form-group row">

            <div class="col-sm-2">Zuordnung</div>

            <div class="col-sm-10">

              <div class="form-check">
                <input type="hidden" name="_isBoardMember" value="on"/>
                <input class="form-check-input" type="checkbox"
                    id="isBoardMember" name="isBoardMember"
                    checked="${role.getIsBoardMember()}">
                <label class="form-check-label" for="isBoardMember">
                  Vorstand - kann Mitglieder verwalten, Kassaführung einsehen
                </label>
              </div>

              <div class="form-check">
                <input type="hidden" name="_isSecretary" value="on"/>
                <input class="form-check-input" type="checkbox"
                    id="isSecretary" name="isSecretary"
                    checked="${role.getIsSecretary()}">
                <label class="form-check-label" for="isSecretary">
                  Schriftführer - kann Protokolle erstellen und verwalten
                </label>
              </div>

              <div class="form-check">
                <input type="hidden" name="_isTreasurer" value="on"/>
                <input class="form-check-input" type="checkbox"
                    id="isTreasurer" name="isTreasurer"
                    checked="${role.getIsTreasurer()}">
                <label class="form-check-label" for="isTreasurer">
                  Kassier - Buchhaltung und Kassaführung verwalten
                </label>
              </div>

              <div class="form-check">
                <input type="hidden" name="_isAuditor" value="on"/>
                <input class="form-check-input" type="checkbox"
                    id="isAuditor" name="isAuditor"
                    checked="${role.getIsAuditor()}">
                <label class="form-check-label" for="isAuditor">
                  Rechnugsprüfer - kann Buchhaltung und Kassaführung einsehen
                </label>
              </div>

              <div class="form-check">
                <input type="hidden" name="_isTimeKeeper" value="on"/>
                <input class="form-check-input" type="checkbox"
                    id="isTimeKeeper" name="isTimeKeeper"
                    checked="${role.getIsTimeKeeper()}">
                <label class="form-check-label" for="isTimeKeeper">
                  Zeitschecks verwalten - kann Zeitschecks erstellen und Stunden verbuchen
                </label>
              </div>

              <div class="form-check">
                <input type="hidden" name="_isAdmin" value="on"/>
                <input class="form-check-input" type="checkbox"
                    id="isAdmin" name="isAdmin"
                    checked="${role.getIsAdmin()}">
                <label class="form-check-label" for="isAdmin">
                  Administrator - kann alles verwalten außer Kassaführung, Zeitschecks, Stundenbuchungen
                </label>
              </div>

              <div class="form-check">
                <input type="hidden" name="_isMiscellaneous" value="on"/>
                <input class="form-check-input" type="checkbox"
                    id="isMiscellaneous" name="isMiscellaneous"
                    checked="${role.getIsMiscellaneous()}">
                <label class="form-check-label" for="isMiscellaneous">
                  Sonstiges - z.B. Ehrenmitglied, Rechte wie ein einfaches Mitglied
                </label>
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

`)

