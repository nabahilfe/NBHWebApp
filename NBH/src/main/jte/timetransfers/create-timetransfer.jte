<!--
  Copyright (c) 2025–2026 Maximilian Weißböck
  Licensed under the MIT License (see LICENSE file).
-->

@import eu.nabahilfe.webapp.GlobalModelAttributes
@import java.util.List

@import static eu.nabahilfe.webapp.DateFormatter.*

@import eu.nabahilfe.webapp.org.Offer
@import eu.nabahilfe.webapp.timetransfers.TimeTransferForm

@param List<Offer> offers
@param TimeTransferForm ttf = null

@param java.lang.String successMessage = null
@param java.lang.String errorMessage = null

@param String fullUsername = null
@param String username = null
@param Boolean isAuthenticated = false


@param eu.nabahilfe.webapp.security.CsrfHiddenInput csrfHiddenInput
@param eu.nabahilfe.webapp.security.ViewContext ctxt
@param GlobalModelAttributes globalModelAttributes


@template.layout.mainpage(
    globalModelAttributes = globalModelAttributes,
    csrfHiddenInput = csrfHiddenInput,
    ctxt = ctxt,
    content = @`

    <h3>Zeitscheck verbuchen</h3>

    <!--
      Template: Detail form for recording a TimeTransfer (time cheque).
      Parameters:
        - offers: List<Offer> (available offer options)
        - ttf: TimeTransferForm (optional; null for creating a new transfer)
        - successMessage, errorMessage: strings for status messages
      Renders: HTML form with autocomplete fields for members, an offer selector,
               service date, hours selector and submit/reset buttons.
    -->


    <br/>

    <!-- Main form: POST to /timetransfers/staff; server processes the submitted fields -->
    <form action="/timetransfers/staff" method="post">
        ${csrfHiddenInput}
        <input type="hidden" name="fromself" id="fromself" value="false">

        <div class="row">
          <div class="col-sm-6 mb-2">
            <div class="card shadow h-100" style="background-color: #ECFEFF;">
              <div class="card-body">

                <h5>Zeitscheck von</h5>
                <p class="card-text text-success">Gibt Zeitscheck ab</p>

                <!--
                  Payer input (userFrom): visible text input for autocomplete search
                  - userFromName: typed search string (autocomplete source)
                  - userFromId: hidden input holding the selected user's ID for submission
                  - userFromResults: UL element used to show matching results
                -->
                <input type="text" id="userFromName" class="form-control" placeholder="Zeitscheck von..."
                    autocomplete="off" value="${ttf == null ? "" : ttf.getUserFromName()}">

                <input type="hidden" name="userFromId" id="userFromId"
                    value="${ttf == null ? "" : ttf.getUserFromId()}">

                <ul id="userFromResults" class="results list-group"></ul>

                <!-- Offer selection: options populated from the provided offers list -->
                <label for="offer" class="col-sm-2 col-form-label">Kategorie</label>
                <div class="col-sm-12">
                    <select class="form-select" name="offerId" id="offerId">
                        <option value="">Auswählen</option>
                        @for(Offer offer : offers)
                            @if((ttf != null && ttf.getOfferId() != null) && ttf.getOfferId().equals(offer.getId().toString()))
                                <option value="${offer.getId()}" selected>
                                    ${offer.getCode()} ${offer.getDescription()}
                                </option>
                            @else
                                <option value="${offer.getId()}">
                                    ${offer.getCode()} ${offer.getDescription()}
                                </option>
                            @endif
                        @endfor
                    </select>
                </div>

                <div class="row g-2">
                    <!-- Service date: required date input (yyyy-mm-dd). Prefilled when editing. -->
                    <div class="col-6">
                        <label for="serviceDate" class="col-form-label">Datum</label>
                        <input type="date" class="form-control" name="serviceDate" id="serviceDate"
                            value="${ttf == null ? "" : formatISO(ttf.getServiceDate())}" required>
                    </div>

                    <!-- Hours selector: choose 1..5 hours. Preselect when ttf provided. -->
                    <div class="col-6">
                        <label for="stunden" class="col-form-label">Anzahl Stunden</label>
                        <select class="form-select" name="hoursSelected" id="hoursSelect">
                            @for(int i = 1; i <= 5; i++)
                                @if((ttf != null && ttf.getHoursSelected() != null) && ttf.getHoursSelected().equals(String.valueOf(i)))
                                    <option value="${i}" selected>@if(i == 1) 1 Stunde @else ${i} Stunden @endif</option>
                                @else
                                    <option value="${i}">@if(i == 1) 1 Stunde @else ${i} Stunden @endif</option>
                                @endif
                            @endfor
                        </select>
                    </div>
                </div>

                <!-- Optional note/description -->
                <label for="note" class="col-form-label">Beschreibung</label>
                <div class="col-sm-12">
                    <textarea class="form-control" id="note" name="note" rows="2" placeholder="Beschreibung (optional)">${ttf == null ? "" : ttf.getNote()}</textarea>
                </div>

              </div>

            </div>

          </div>

          <div class="col-sm-6 mb-1">
            <div class="card shadow h-100" style="background-color: #ECFEFF;">
              <div class="card-body">

                <h5>Zeitscheck an</h5>
                <p class="card-text text-success">Erhält Zeitscheck</p>

                <!--
                  Payee input (userTo): same structure as userFrom
                  - userToName: visible autocomplete input
                  - userToId: hidden selected user id for submission
                  - userToResults: UL showing matching search results
                -->
                <input type="text" id="userToName" class="form-control" placeholder="Zeitscheck an..."
                    autocomplete="off" value="${ttf == null ? "" : ttf.getUserToName()}">

                <input type="hidden" name="userToId" id="userToId"
                    value="${ttf == null ? "" : ttf.getUserToId()}">

                <ul id="userToResults" class="results list-group"></ul>

              </div>
            </div>

          </div>

        </div>

        <br/>

        <!-- Form actions: submit posts the transfer; reset clears client-side inputs -->
        <div class="d-flex flex-wrap gap-2">
            <button id="submitBtn" class="btn btn-primary bi-card-checklist" type="submit"> Zeitscheck verbuchen</button>
            <button class="btn btn-secondary" type="button" onclick="resetInputs()">Eingabe löschen</button>
        </div>

        <br/>

        <!-- Quick links to booking reports -->
        <div class="d-flex flex-column flex-sm-row gap-2">
            <a href="/timecheques/booking-report" class="btn btn-outline-primary bi-people">
                &nbsp; Fremd-verbuchte Zeitschecks anzeigen
            </a>
            <a href="/timecheques/self-booking-report" class="btn btn-outline-primary bi-person-check">
                &nbsp; Selbst-verbuchte Zeitschecks anzeigen
            </a>
        </div>

    </form>

    <br/>

    @template.components.display-statusmessage(successMessage, errorMessage)


    <script>

        /*
          setupAutocomplete(inputId, hiddenId, resultId)
          - Binds an input listener on the visible text input.
          - Performs a fetch to /api/members/search?q=... when at least 2 characters are entered.
          - Populates the provided UL with clickable LI items. Clicking an item sets the
            visible input to a readable label and the hidden input to the selected user ID.
          - Dispatches a 'change' event on the hidden input so other listeners (validation) run.
        */
        function setupAutocomplete(inputId, hiddenId, resultId) {
            const input = document.getElementById(inputId);
            const hidden = document.getElementById(hiddenId);
            const results = document.getElementById(resultId);

            input.addEventListener("input", async () => {
                const q = input.value.trim();
                results.innerHTML = "";
                hidden.value = "";

                if (q.length < 2) return;

                const response = await fetch("/api/members/search?q=" + encodeURIComponent(q));
                const users = await response.json();

                users.forEach(user => {
                    const li = document.createElement("li");
                    li.classList.add("list-group-item");
                    li.classList.add("bg-dark-subtle");
                    li.textContent = user.nameAndAdress;
                    li.onclick = () => {
                        input.value = user.nameAndAdress;
                        hidden.value = user.id;
                        hidden.dispatchEvent(new Event('change'));
                        results.innerHTML = "";
                    };
                    results.appendChild(li);
                });
            });
        }

        // Initialize autocompletes for payer and payee
        setupAutocomplete("userFromName", "userFromId", "userFromResults");
        setupAutocomplete("userToName", "userToId", "userToResults");

        const userFrom = document.getElementById("userFromId");
        const userTo = document.getElementById("userToId");
        const offer = document.getElementById("offerId");
        const dateOfService = document.getElementById("serviceDate");

        const submitButton = document.getElementById("submitBtn");
        submitButton.disabled = true;

        document.addEventListener('DOMContentLoaded', checkFields);

        /*
          checkFields()
          - Validates that all required fields (userFromId, userToId, offerId, dateOfService)
            have non-empty values and enables/disables the submit button accordingly.
          - Called on relevant change/input events and once on DOMContentLoaded.
        */
        function checkFields() {
            if (userFrom.value.trim() !== "" && userTo.value.trim() !== ""
                    && offer.value.trim() !== "" && dateOfService.value.trim() !== "") {
                submitButton.disabled = false;
            } else {
                submitButton.disabled = true;
            }
        }

        userFrom.addEventListener("change", checkFields);
        userTo.addEventListener("change", checkFields);
        offer.addEventListener("change", checkFields);
        dateOfService.addEventListener("input", checkFields);

        /*
          resetInputs()
          - Clears visible inputs and hidden ids, resets selects to defaults and disables submit.
          - Note: UL result containers must be cleared via innerHTML = "" because they are
            list elements and do not have a value property.
        */
        function resetInputs() {
            document.getElementById("userFromName").value = "";
            document.getElementById("userFromResults").innerHTML = "";
            document.getElementById("userFromId").value = "";
            document.getElementById("serviceDate").value = "";

            document.getElementById("userToName").value = "";
            document.getElementById("userToId").value = "";
            document.getElementById("userToResults").innerHTML = "";

            document.getElementById("hoursSelect").value = "1";
            document.getElementById("offerId").value = "";

            submitButton.disabled = true;
        }

    </script>

`)






