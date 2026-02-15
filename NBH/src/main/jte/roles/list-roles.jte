@import eu.nabahilfe.webapp.members.Role

@param java.util.List<Role> roles

@param java.lang.String successMessage = null
@param java.lang.String errorMessage = null

@param String fullUsername = null
@param String username = null
@param Boolean isAuthenticated = false


@template.layout.main(
    isAuthenticated = isAuthenticated,
    username = username,
    fullUsername = fullUsername,
    content = @`

    <h3>Rollen und Berechtigungen für Mitglieder
        <button type="button" class="btn btn-link" data-bs-toggle="modal" data-bs-target="#helpModal">
            Erklärung zu den Rollen und Berechtigungen
        </button>
    </h3>

    <br/>

    <!-- Modal help dialog -->
    <div class="modal fade" id="helpModal" tabindex="-1">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title fs-5">Rollen und Berechtigungen</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <p>Die <b>Rollen</b> bieten einerseitzs die Möglichkeit <b>Bezeichnungen für Funktionen</b> zu vergeben,
                andererseits sind damit auch <b>bestimmte Berechtigungen</b> vebunden.</p>
            <ul>
              <li><b>Vorstand</b> - kann Mitglieder verwalten, Kassaführung einsehen.</li>
              <li><b>Schriftführer</b> - kann Protokolle erstellen und verwalten.</li>
              <li><b>Kassier</b> - kann Zahlungen erfassen und die Kassaführung verwalten.</li>
              <li><b>Prüfer</b> - kann Zahlungen und Kassaführung einsehen.</li>
              <li><b>Zeitschecks</b> - kann Zeitschecks erfassen und verwalten.</li>
              <li><b>Administrator</b> - kann alles einsehen, erfassen und verwalten.</li>
              <li><b>Sonstiges</b> - wie ein einfaches Mitglied, nur eigene Daten einsehen, Mitglieder suchen, Stunden übertragen.</li>
             </ul>
             <p>Einer <b>Rolle</b> können auch <b>mehrere Berechtigungen</b> zugeordnet werden, z.B. Obmann/Obfrau auch die Berechtigung für Zeitschecks,
                oder ein Schriftführer könnte auch Administrator sein.</p>
             <p>Die Funktionen <b>Zeitschecks</b> und <b>Administrator</b> müssen immer mit einer Vereins-Funktion wie Vorstand, Schriftführer oder Kassier kombiniert werden.</p>
             <b>Die Funktionen <i>Prüfer</i> und <i>Sonstiges</i> lassen sich <i>nicht</i> mit anderen Berechtigungen kombinieren!</b>
          </div>
        </div>
      </div>
    </div>




    <!-- Display all Role records -->
    <table class="table table-hover table-sm" id="memberTable">
      <thead>
        <tr>
          <th scope="col">Rolle und Berechtigungen</th>
          <th scope="col" class="text-center">Vorstand</th>
          <th scope="col" class="text-center">Schriftführer</th>
          <th scope="col" class="text-center">Kassier</th>
          <th scope="col" class="text-center">Prüfer</th>
          <th scope="col" class="text-center">Zeitschecks️</th>
          <th scope="col" class="text-center">Administrator</th>
          <th scope="col" class="text-center">Sonstiges</th>
          <th scope="col" class="text-center">Edit</th>
          <th scope="col" class="text-center">Delete</th>
        </tr>
      </thead>

      <tbody class="table-group-divider">
        @for (Role role : roles)
        <tr>
          <td scope="row">${role.getRoleName()}</td>
          <td class="text-center">@if(role.getIsBoardMember())<i class="bi bi-check-lg"></i>@endif</td>
          <td class="text-center">@if(role.getIsSecretary())<i class="bi bi-check-lg"></i>@endif</td>
          <td class="text-center">@if(role.getIsTreasurer())<i class="bi bi-check-lg"></i>@endif</td>
          <td class="text-center">@if(role.getIsAuditor())<i class="bi bi-check-lg"></i>@endif</td>
          <td class="text-center">@if(role.getIsTimeKeeper())<i class="bi bi-check-lg"></i>@endif</td>
          <td class="text-center">@if(role.getIsAdmin())<i class="bi bi-check-lg"></i>@endif</td>
          <td class="text-center">@if(role.getIsMiscellaneous())<i class="bi bi-check-lg"></i>@endif</td>
          <td class="text-center">
            <a class="btn btn-outline-primary btn-sm bi-pencil"
               href="/roles/${role.getId()}"
               role="button"></a>
          </td>
          <td class="text-center">
            <!-- we need an ordinary botton her for the modal dismiss/confirm popup -->
            <button type="button" class="btn btn-outline-danger btn-sm bi-x-lg" data-bs-toggle="modal" data-bs-target="#confirmDeleteModal"
                data-item-id="${role.getId()}"></button>
          </td>
        </tr>
        @endfor
      </tbody>
    </table>

    <!-- Modal confirm delete dialog -->
    <div class="modal fade" id="confirmDeleteModal" tabindex="-1">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h1 class="modal-title fs-5" id="confirmDeleteModalLabel">Daten löschen?</h1>
          </div>
          <div class="modal-body">
            Die Daten werden endgültig gelöscht und können nicht wieder hergestellt werden.
            <input type="hidden" id="data-item-id-to-delet">
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
              Abbrechen
            </button>
            <button type="button" id="delete-btn" class="btn btn-danger" onclick="deleteAction()" data-bs-dismiss="modal">
              Löschen
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Hidden form so we can submit the delete -->
    <form id="deleteForm" method="post" style="display: none;">
        <input type="hidden" name="roleId" id="roleIdInput">
    </form>

    <!-- add an item -->
    <form action="/roles/new" method="get">
        <button class="btn btn-outline-primary bi-plus-circle" type="submit"> Rolle hinzufügen</button>
    </form>

    <br/>

    @template.components.display-statusmessage(successMessage, errorMessage)

    <script>

        var deleteModal = document.getElementById('confirmDeleteModal');

        // save the selectet id inside the modal so we have acces to upon delete confirm
        deleteModal.addEventListener('show.bs.modal', function (event) {
              var button = event.relatedTarget; // Button that triggered the modal
              var itemId = button.getAttribute('data-item-id');

              console.log("Item clicked:", itemId);

              // put ID into a hidden field so wec can later on have access to it
              deleteModal.querySelector('.modal-body #data-item-id-to-delet').value = itemId;
        });


        <!-- submit a post action to controller to delete the item -->
        async function deleteAction() {
            const itemId = document.getElementById('data-item-id-to-delet').value;
            document.getElementById('deleteForm').action = '/roles/delete/' + itemId;
            document.getElementById('deleteForm').submit();
        }

    </script>

`)


