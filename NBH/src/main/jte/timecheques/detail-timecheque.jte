@import eu.nabahilfe.webapp.NbhConst
@import eu.nabahilfe.webapp.members.Member
@import eu.nabahilfe.webapp.timeexchange.TimeCheque

@param TimeCheque timeCheque

@param java.lang.String successMessage = null
@param java.lang.String errorMessage = null

@template.layout.main(content = @`

    <h3 class="text-primary">Zeitscheck erstellen für</h3>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js" integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI" crossorigin="anonymous"></script>

    <div class="card text-center">
        <div class="card-header">
            <h4 class="text-primary">${timeCheque.getAssignedTo().getName()}</h4>
            <p>${timeCheque.getAssignedTo().getAddress()}</p>
        </div>
        <ul class="list-group list-group-flush">
            <li class="list-group-item">${timeCheque.getHours() == NbhConst.FIRST_TIME_CHEQUE_HOURS ? "Kostenlos bei Eintritt" : "Kauf von Zeitschecks"}</li>
            <li class="list-group-item">${timeCheque.getHours()} h</li>
        </ul>
        <div class="card-body">
            <!-- we need an ordinary botton her for the modal dismiss/confirm popup -->
            <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#confirmModal"
                data-item-id="${timeCheque.getAssignedTo().getId()}">Erstellen️️</button>
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
            <button type="button" class="btn btn-secondary" onclick="window.location.href='/members'" data-bs-dismiss="modal">
                Abbrechen
            </button>
            <button type="button" id="confirm-btn" class="btn btn-primary" onclick="confirmAction()" data-bs-dismiss="modal">
                Zuweisen
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Hidden form so we can submit the action -->
    <form id="hiddenForm" method="POST" style="display: none;">
        <input type="hidden" name="timeChequeHidden">
    </form>

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
            document.getElementById('hiddenForm').action = '/timecheques';
            document.getElementById('hiddenForm').submit();
        }

    </script>


`)


