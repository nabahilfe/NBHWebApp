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

@template.layout.main(
    searchTerm = searchTerm,
    content = @`

  <!-- Hidden form to handle sorting -->
  <form id="sortForm" method="get" action="/members">
      <input type="hidden" name="orderBy" id="orderBy">
      <input type="hidden" name="order" id="order">
  </form>

    <h3>Unsere Vereinsmitglieder</h3>
    <br/>
    <!-- Display all Member records -->
    <table class="table table-hover table-sm" id="memberTable">

        <thead>
        <tr>
          <th scope="col" data-col="firstName" data-order="${orderBy.equals("firstName") ? order : ""}">
            Vorname ${orderBy.equals("firstName") ? (order.equals("asc") ? "↑" : "↓") : ""}</th>
          <th scope="col" data-col="lastName" data-order="${orderBy.equals("lastName") ? order : ""}">
            Nachname ${orderBy.equals("lastName") ? (order.equals("asc") ? "↑" : "↓") : ""}</th>
          <th scope="col">Adresse</th>
          <th scope="col" data-col="role.roleName" data-order="${orderBy.equals("role.roleName") ? order : ""}">
            Rolle ${orderBy.equals("role.roleName") ? (order.equals("asc") ? "↑" : "↓") : ""}</th>
          <th scope="col" class="text-center">Edit</th>
          <th scope="col" class="text-center">Delete</th>
          <th scope="col" class="text-end" data-col="accumulatedHours" data-order="${orderBy.equals("accumulatedHours") ? order : ""}">
            Guthaben ${orderBy.equals("accumulatedHours") ? (order.equals("asc") ? "↑" : "↓") : ""}</th>
          <th scope="col" class="text-center">Zeitscheck</th>
        </tr>
      </thead>

        <tbody class="table-group-divider">
        @for (Member member : memberPage.getContent())
        <tr>
          <td>${member.getFirstName()}</td>
          <td><b>${member.getLastName()}</b></td>
          <td>${member.getAddress()}</td>
          <td>${member.getRole() != null ? member.getRole().getRoleName() : ""}</td>
          <td class="text-center">
            <a class="btn btn-outline-primary btn-sm bi-pencil"
                href="/members/${member.getId()}"
                role="button"></a>
          </td>
          <td class="text-center">
              <!-- we need an ordinary botton her for the modal dismiss/confirm popup -->
            <button type="button" class="btn btn-outline-danger btn-sm bi-x-lg"
                data-bs-toggle="modal"
                data-bs-target="#confirmModal"
                data-item-id="${member.getId()}"></button>
          </td>
          <td class="text-end">
            ${member.getAccumulatedHours() > 0 ? member.getAccumulatedHours() + " h" : ""}
          </td>
          <td class="text-center">
            <a class="btn btn-outline-primary btn-sm bi-plus-lg" href="/timecheques/new?memberId=${member.getId()}"
                role="button"></a>
          </td>
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
            Die Daten werden endgültig gelöscht und können nicht wieder hergestellt werden.
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
        <input type="hidden" name="memberId" id="memberIdInput">
    </form>

    <!-- add an item -->
    <form action="/members/new" method="get">
        <button type="submit" class="btn btn-primary bi-person-add"> Mitglied hinzufügen</button>
    </form>

    <p/>

    <!-- Pagination controls -->
    @if(memberPage.getTotalPages() > 0)
        <nav aria-label="Page navigation example">
          <ul class="pagination">
                @for(int i = 0; i < memberPage.getTotalPages(); ++i)
                  <li class="page-item"><a class="page-link"
                    href="/members?page=${i}&orderBy=${orderBy}&order=${order}&size=${NbhConst.PAGINATION_PAGE_SIZE}">${i+1}</a>
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


    <script>
    document.addEventListener("DOMContentLoaded", () => {
        const form = document.getElementById("sortForm");
        const orderByInput = document.getElementById("orderBy");
        const orderInput = document.getElementById("order");

            // Add click listeners to table headers
        document.querySelectorAll("#memberTable th[data-col]").forEach(th => {
          th.style.cursor = "pointer";
          th.addEventListener("click", () => {
              const col = th.dataset.col; // eg "Vorname"

              // Toggle: If currently asc for this column, then desc – otherwise new asc
              const currentOrder = th.dataset.order;
              const newOrder = currentOrder === "asc" ? "desc" : "asc";

              // rest all headers
              document.querySelectorAll("#memberTable th").forEach(h => {
                h.dataset.order = "";
                h.textContent = h.textContent.replace(/[↕↑↓]/g, ''); // Pfeile entfernen
              });

              // Set new order and visual feedback
              th.dataset.order = newOrder;
              th.textContent += newOrder === "asc" ? " ↑" : " ↓"; // Visuelles Feedback

              // Set hidden form values to sort order
              orderByInput.value = col;
              orderInput.value = newOrder;

              form.submit(); // Seite neu laden mit sortierten Daten
          });
        });
    });
  </script>


`)


