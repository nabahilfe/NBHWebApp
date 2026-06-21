<!--
  Copyright (c) 2025–2026 Maximilian Weißböck
  Licensed under the MIT License (see LICENSE file).
-->

@import java.util.List
@import java.time.LocalDate

@import static eu.nabahilfe.webapp.DateFormatter.*
@import static eu.nabahilfe.webapp.NumberFormatter.*
@import static eu.nabahilfe.webapp.jte.TemplateHelpers.*

@import eu.nabahilfe.webapp.NbhConst
@import eu.nabahilfe.webapp.members.Member
@import eu.nabahilfe.webapp.timecheques.TimeCheque

@param TimeCheque timeCheque
@param List<TimeCheque> purchasedTimeCheques

@param Float pricePerHour

@param java.lang.String successMessage = null
@param java.lang.String errorMessage = null


@param eu.nabahilfe.webapp.security.CsrfHiddenInput csrfHiddenInput
@param eu.nabahilfe.webapp.security.ViewContext ctxt


@template.layout.mainpage(
    csrfHiddenInput = csrfHiddenInput,
    ctxt = ctxt,
    content = @`


    <div class="container d-flex justify-content-center">

            <div class="row">
                <div class="col-auto">
                    <div class="card shadow" style="background-color: #ECFEFF;">
                        <div class="card-body">

                            <form id="tcForm" action="/timecheques" method="POST">
                                ${csrfHiddenInput}

                                <h4 class="card-title text-primary">Zeitschecks erstellen für</h4>
                                <hr/>
                                <h4>${timeCheque.getAssignedTo().getName()}</h4>
                                <h5>${timeCheque.getAssignedTo().getAddress()}</h5>
                                <hr/>

                                Aktuelles Zeitguthaben: <b>${timeCheque.getAssignedTo().getAccumulatedHours() == null ? 0
                                        : timeCheque.getAssignedTo().getAccumulatedHours()} h</b>

                                <br/>

                                @if(timeCheque.getHours() == NbhConst.FIRST_TIME_CHEQUE_HOURS)

                                    Kostenlos bei Eintritt: <b>${timeCheque.getHours()} h</b>
                                    <input type="hidden" name="hours" value="${timeCheque.getHours()}">

                                @else

                                    Kauf von Zeitschecks:
                                    <select name="hours" id="hoursSelected" class="form-select d-inline-block w-auto" onchange="calculatePrice()">
                                        <option value="10" selected>10 h</option>
                                        <option value="20">20 h</option>
                                    </select>

                                    <br/>
                                    Kosten: <b><span id="calculatedPrice"></span> €</b>

                                @endif

                                <div class="row g-3 align-items-center">
                                    <div class="col-auto">
                                        <label for="orderDate" class="col-form-label">Erstellungsdatum:</label>
                                        <b>${dateReverseDE(timeCheque.getTransactionDate())}</b>
                                        <input type="hidden" name="orderDate" value="${formatISO(timeCheque.getTransactionDate())}">
                                    </div>
                                </div>

                                @if (errorMessage == null)
                                    <!-- we need an ordinary botton her for the modal dismiss/confirm popup -->
                                    <hr/>
                                    <div class="text-center">
                                        Zeitschecks für <b>${timeCheque.getAssignedTo().getName()}</b>
                                        <br/>
                                        <br/>
                                        <button type="button" class="btn btn-primary bi-plus-square" data-bs-toggle="modal" data-bs-target="#confirmModal"
                                                data-item-id="${timeCheque.getAssignedTo().getId()}">
                                                @if (timeCheque.getHours() != NbhConst.FIRST_TIME_CHEQUE_HOURS)
                                                    Zeitschecks kaufen
                                                @else
                                                    Zeitschecks zuweisen
                                                @endif
                                        </button>
                                    </div>
                                @endif
                            </form>

                        </div>
                    </div>

                </div>

            </div>

    </div>



    <br/>

    @template.components.display-statusmessage(successMessage, errorMessage)

    <br/>

    <div class="card">

       @template.components.list-timecheques-component(member = timeCheque.getAssignedTo(),
               timeCheques = purchasedTimeCheques, fullDate = Boolean.TRUE)

    </div>


    <!-- Modal confirm delete dialog -->
    <div class="modal fade" id="confirmModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h1 class="modal-title fs-5" id="confirmModalLabel">Zeitscheck zuweisen?</h1>
                </div>
                <div class="modal-body">
                    Zeitscheck mit <b><span id="modalHours"></span> h </b>wird an <b>${timeCheque.getAssignedTo().getName()}</b> zugewiesen.<br/>
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
        const pricePerHour = ${pricePerHour};
        const selectedHours = document.getElementById('hoursSelected');

        function calculatePrice() {
            const hours = parseInt(document.getElementById('hoursSelected').value);
            const priceSpan = document.getElementById('calculatedPrice');
            if (priceSpan) {
                priceSpan.textContent = (hours * pricePerHour).toLocaleString('de-DE', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
            }
        }
        document.addEventListener('DOMContentLoaded', calculatePrice);

        var modalPanel = document.getElementById('confirmModal');

        // save the selectet id inside the modal so we have acces to upon delete confirm
        modalPanel.addEventListener('show.bs.modal', function (event) {
            var button = event.relatedTarget; // Button that triggered the modal
            var itemId = button.getAttribute('data-item-id');
            // put ID into a hidden field so wec can later on have access to it
            modalPanel.querySelector('.modal-body #data-item-id').value = itemId;
            modalPanel.querySelector('.modal-body #modalHours').textContent = selectedHours.value;
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


