@import java.util.List


@import eu.nabahilfe.webapp.NbhConst
@import eu.nabahilfe.webapp.members.Member
@import eu.nabahilfe.webapp.timeexchange.TimeCheque

@param TimeCheque timeCheque
@param List<TimeCheque> purchasedTimeCheques

@param java.lang.String successMessage = null
@param java.lang.String errorMessage = null

@template.layout.main(content = @`

    <h3 class="text-primary">Zeitscheck erstellen für</h3>

    <br/>

    <div class="card">

      <form id="tcForm" action="/timecheques" method="POST">

          <div class="card-header">
              <h4 class="text-primary">${timeCheque.getAssignedTo().getName()}</h4>
              <p>${timeCheque.getAssignedTo().getAddress()}</p>
          </div>

          <ul class="list-group list-group-flush">
            <li class="list-group-item">
              Aktuelles Guthaben: <b>${timeCheque.getAssignedTo().getAccumulatedHours()} h</b>
            </li>

            <li class="list-group-item ">
              ${timeCheque.getHours() == NbhConst.FIRST_TIME_CHEQUE_HOURS ? "Kostenlos bei Eintritt" : "Kauf von Zeitschecks"}:
              <b>${timeCheque.getHours()} h</b>
            </li>

            <li class="list-group-item ">
              <div class="row g-3 align-items-center">
                <div class="col-auto">
                  <label for="orderDate" class="col-form-label">Bestelldatum</label>
                </div>
                <div class="col-auto">
                  <input type="date" class="form-control" name="orderDate" id="orderDate" value="" required>
                </div>
              </div>
            </li>
          </ul>

          <div class="card-body">
              <!-- we need an ordinary botton her for the modal dismiss/confirm popup -->
              <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#confirmModal"
                  data-item-id="${timeCheque.getAssignedTo().getId()}">Erstellen️️</button>
          </div>

       </form>

     </div>

  <br/>

    <div class="card">
          <div class="card-body">
            <h5 class="card-title text-primary">Bereits erworbene Zeitschecks</h5>
            <p class="card-text">Nur die letzten 10</p>

            <table class="table table-striped table-bordered table-hover table-sm mt-4" id="timeChequeTable">
              <thead class="table-dark">
                <tr>
                  <th scope="col">Datum</th>
                  <th scope="col">erstellt von</th>
                  <th scope="col" class="text-end">Stunden</th>
                </tr>
              </thead>
              <tbody>
                @for (TimeCheque tc : purchasedTimeCheques)
                <tr>
                  <th scope="row">${tc.getOrderDateString()}</th>
                  <th scope="row">${tc.getCreatedBy().getName()}</th>
                  <th scope="row" class="text-end">${tc.getHours()}</th>
                </tr>
                @endfor
              </tbody>
            </table>

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