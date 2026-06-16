<!--
  Copyright (c) 2025–2026 Maximilian Weißböck
  Licensed under the MIT License (see LICENSE file).
-->

@import org.springframework.data.domain.Page

@import eu.nabahilfe.webapp.NbhConst
@import eu.nabahilfe.webapp.members.Role
@import eu.nabahilfe.webapp.members.Member

@param Page<Member> memberPage

@param java.lang.String searchTerm = ""
@param java.lang.String orderBy = ""
@param java.lang.String order = ""

@param java.lang.String successMessage = null
@param java.lang.String errorMessage = null

@param String fullUsername = null
@param String username = null
@param Boolean isAuthenticated = false

@param eu.nabahilfe.webapp.security.CsrfHiddenInput csrfHiddenInput
@param eu.nabahilfe.webapp.security.ViewContext ctxt


@template.layout.mainpage(
    csrfHiddenInput = csrfHiddenInput,
    ctxt = ctxt,
    content = @`

    <h3>Ausgetretene Vereinsmitglieder</h3>
    <br/>


    <!-- Display all Member records -->
    <table class="table table-hover table-sm" id="memberTable">

        <thead>
        <tr>
          <th scope="col">Nachname </th>
          <th scope="col">Vorname</th>
          <th scope="col">Adresse</th>
          <th scope="col">Rolle</th>
          <th scope="col">E-Mail</th>
          <th scope="col">Telefon</th>

          @if (ctxt != null && ctxt.hasAnyRole("ADMIN", "BOARD_MEMBER"))
              <th scope="col" class="text-end">Guthaben</th>
              <th scope="col" class="text-center">Löschen</th>
          @endif
        </tr>
        </thead>

      <tbody class="table-group-divider">
        @for (Member member : memberPage.getContent())
            <tr>
                  @if (ctxt != null && ctxt.hasAnyRole("ADMIN", "BOARD_MEMBER"))
                      <td><a class="text-decoration-none" href="/members/${member.getId()}"><b>${member.getLastName()}</b></a></td>
                  @else
                    <td>${member.getLastName()}</td>
                  @endif

                  <td>${member.getFirstName()}</td>

                  <td>
                      <a href="https://www.google.com/maps/search/?api=1&query=${member.getAddress().replace(" ", "+")}" target="_blank"
                               rel="noopener noreferrer" class="text-decoration-none" title="In Google Maps öffnen"> ${member.getAddress()}
                      </a>
                  </td>

                  <td>${member.getRole() != null ? member.getRole().getRoleName() : ""}</td>

                  <td>
                      @if(member.getEmail() != null && !member.getEmail().isBlank())
                          <a href="mailto:${member.getEmail()}" class="text-decoration-none">${member.getEmail()}</a>
                      @endif
                  </td>

                  <td>
                      @if(member.getPhoneNumber() != null && !member.getPhoneNumber().isBlank())
                          <a href="tel:${member.getPhoneNumber().replace(" ", "")}" class="text-decoration-none"> ${member.getPhoneNumber()}
                          </a>
                      @endif
                  </td>

                  @if(ctxt != null && ctxt.hasAnyRole("ADMIN", "BOARD_MEMBER"))
                      <td class="text-end">
                        ${member.getAccumulatedHours() != null ? member.getAccumulatedHours() + " h" : ""}
                      </td>

                      @if(member.isSystemMember())
                         <!-- do not show delete button for system members -->
                          <td class="text-center"></td>
                      @else
                          <!-- show delete button  -->
                          <td class="text-center">
                            <!-- we need an ordinary botton her for the modal dismiss/confirm popup -->
                            <button type="button" class="btn btn-outline-danger btn-sm bi-x-lg"
                                data-bs-toggle="modal"
                                data-bs-target="#confirmModal"
                                data-item-id="${member.getId()}">
                            </button>
                          </td>
                     @endif

                  @endif

            </tr>
        @endfor
      </tbody>

    </table>

    <!-- Modal confirm delete dialog -->
    <div class="modal fade" id="confirmModal" tabindex="-1">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h1 class="modal-title fs-5" id="confirmModalLabel">Daten wirklich löschen?</h1>
          </div>
          <div class="modal-body">
            Die Daten werden komplett anonymisiert und können nicht wieder hergestellt werden. Noch vorhandene Zeitschecks werden auf das Sozialkonto übertragen.
            Bitte bestätige, dass du den Datensatz wirklich löschen möchtest.
            <input type="hidden" id="data-item-id-to-delet">
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
              Abbrechen
            </button>
            <button type="button" id="delete-btn" class="btn btn-danger" onclick="confirmAction()" data-bs-dismiss="modal">
              Löschen
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Hidden form so we can submit the delete -->
    <form id="hiddenForm" method="post" style="display: none;">
        ${csrfHiddenInput}
        <input type="hidden" name="memberId" id="memberIdInput">
    </form>


    <!-- Pagination controls -->
    @if(memberPage.getTotalPages() > 0)
        <nav aria-label="Page navigation example">
          <ul class="pagination">
                @for(int i = 0; i < memberPage.getTotalPages(); ++i)
                  <li class="page-item ${i == memberPage.getNumber() ? "active" : ""}">
                    <a class="page-link" href="/members?page=${i}&orderBy=${orderBy}&order=${order}&size=${NbhConst.PAGINATION_PAGE_SIZE}">${i+1}</a>
                  </li>
              @endfor
            </ul>
        </nav>
    @endif

    @template.components.display-statusmessage(successMessage, errorMessage)


    <!-- Script to handle the delete confirmation modal and action -->
    <script>
        var editModal = document.getElementById('confirmModal');

        // save the selectet id inside the modal so we have acces to upon delete confirm
        editModal.addEventListener('show.bs.modal', function (event) {
              var button = event.relatedTarget; // Button that triggered the modal
              var itemId = button.getAttribute('data-item-id');

              console.log("Item clicked:", itemId);

              // put ID into a hidden field so wec can later on have access to it
              editModal.querySelector('.modal-body #data-item-id-to-delet').value = itemId;
        });


        <!-- submit a post action to controller to delete the item -->
        async function confirmAction() {
            const itemId = document.getElementById('data-item-id-to-delet').value;
            document.getElementById('hiddenForm').action = '/members/delete/' + itemId;
            document.getElementById('hiddenForm').submit();
        }
    </script>

`)




