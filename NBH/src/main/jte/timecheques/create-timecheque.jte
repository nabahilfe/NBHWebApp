@import java.util.List
@import java.time.LocalDate

@import static eu.nabahilfe.webapp.DateFormatter.*
@import static eu.nabahilfe.webapp.jte.TemplateHelpers.*

@import eu.nabahilfe.webapp.NbhConst
@import eu.nabahilfe.webapp.members.Member
@import eu.nabahilfe.webapp.timecheques.TimeCheque

@param TimeCheque timeCheque
@param List<TimeCheque> purchasedTimeCheques

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

    <h3>Zeitscheck erstellen für</h3>
    <br/>


    <form id="tcForm" action="/timecheques" method="POST">
        ${csrfHiddenInput}
        <div class="card">
            <div class="card-body bg-success bg-opacity-10">
                <h4 class="text-primary">${timeCheque.getAssignedTo().getName()}</h4>
                <p>${timeCheque.getAssignedTo().getAddress()}</p>

                Aktuelles Zeitguthaben: <b>${timeCheque.getAssignedTo().getAccumulatedHours() == null ? 0
                        : timeCheque.getAssignedTo().getAccumulatedHours()} h</b>

                <p>
                    ${timeCheque.getHours() == NbhConst.FIRST_TIME_CHEQUE_HOURS ? "Kostenlos bei Eintritt" : "Kauf von Zeitschecks"}:
                    <b>${timeCheque.getHours()} h</b>
                </p>

                <div class="row g-3 align-items-center">
                    <div class="col-auto">
                        <label for="orderDate" class="col-form-label">Erstellungsdatum:</label>
                    </div>
                    <div class="col-auto">
                        <input type="date" class="form-control" name="orderDate" id="orderDate" value="${formatISO(timeCheque.getTransactionDate())}" required>
                    </div>
                </div>
            </div>
        </div>
    </form>


    @if (errorMessage == null)
        <br/>
        <!-- we need an ordinary botton her for the modal dismiss/confirm popup -->
        <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#confirmModal"
            data-item-id="${timeCheque.getAssignedTo().getId()}">Erstellen️️</button>
    @endif


    <br/>
    @template.components.display-statusmessage(successMessage, errorMessage)

    <br/>
    <div class="card">
        <div class="card-body">
            <h5>Erworbene Zeitschecks von ${ctxt.getFullUsername()}</h5>

            <table class="table table-hover table-sm" id="memberTable">
                <thead>
                    <tr>
                        <th scope="col">Datum</th>
                        <th scope="col">erstellt von</th>
                        <th scope="col" class="text-end">Stunden</th>
                    </tr>
                </thead>
                <tbody class="table-group-divider">
                    @for (TimeCheque tc : purchasedTimeCheques)
                    <tr>
                        <td>${dateDE(tc.getTransactionDate())}</td>
                        <td>${tc.getCreatedBy().getName()}</td>
                        <td class="text-end">${tc.getHours()}</td>
                    </tr>
                    @endfor
                </tbody>
            </table>

        </div>
    </div>


    <!-- Modal confirm delete dialog -->
    <div class="modal fade" id="confirmModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h1 class="modal-title fs-5" id="confirmModalLabel">Zeitschek zuweisen?</h1>
                </div>
                <div class="modal-body">
                    Zeitschek mit <b>${timeCheque.getHours()} h </b>wird an <b>${timeCheque.getAssignedTo().getName()}</b> zugewiesen.<br/>
                    Das kann nicht mehr rückgängig gemacht werden.
                    <input type="hidden" id="data-item-id">
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                        Abbrechen
                    </button>
                    <button type="button" id="confirm-btn" class="btn btn-primary" onclick="confirmAction()">
                        Zuweisen
                    </button>
                </div>
            </div>
        </div>
    </div>


    <script>
        var modalPanel = document.getElementById('confirmModal');

        // save the selectet id inside the modal so we have acces to upon delete confirm
        modalPanel.addEventListener('show.bs.modal', function (event) {
            var button = event.relatedTarget; // Button that triggered the modal
            var itemId = button.getAttribute('data-item-id');
            // put ID into a hidden field so wec can later on have access to it
            modalPanel.querySelector('.modal-body #data-item-id').value = itemId;
        });


        <!-- submit a post action to controller to handle the item -->
        function confirmAction() {
            const itemId = document.getElementById('data-item-id').value;
            const form = document.getElementById('tcForm');
            form.action = '/timecheques';

            // Use requestSubmit() so browser runs HTML5 form validation (required, etc.).
            // Fallback: create a temporary hidden submit button and click it (older browsers).
            if (typeof form.requestSubmit === 'function') {
                form.requestSubmit();
            } else {
                const tmp = document.createElement('button');
                tmp.type = 'submit';
                tmp.style.display = 'none';
                form.appendChild(tmp);
                tmp.click();
                form.removeChild(tmp);
            }
        }

    </script>

`)

