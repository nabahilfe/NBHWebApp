<!--
  Copyright (c) 2025–2026 Maximilian Weißböck
  Licensed under the MIT License (see LICENSE file).
-->

@param java.lang.String successMessage = null
@param java.lang.String errorMessage = null

@param eu.nabahilfe.webapp.security.CsrfHiddenInput csrfHiddenInput
@param eu.nabahilfe.webapp.security.ViewContext ctxt


@template.layout.mainpage(
    csrfHiddenInput = csrfHiddenInput,
    ctxt = ctxt,
    content = @`

    <h3>Einnahme erfassen</h3>
    <br/>

    <div class="row">
        <div class="col-lg-7">
            <div class="card shadow" style="background-color: #ECFEFF;">
                <div class="card-body">

                    <form action="/accountings/save-income" method="post">
                        ${csrfHiddenInput}

                        <!-- Beschreibung (immer Pflichtfeld) -->
                        <label for="description" class="col-form-label">Beschreibung<span class="text-danger">*</span></label>
                        <div class="mb-3">
                            <textarea class="form-control" name="description" id="description"
                                      rows="3" maxlength="250" required></textarea>
                            <div class="form-text">Maximal 250 Zeichen.</div>
                        </div>

                        <!-- Transaktionsdatum -->
                        <label for="transactionDate" class="col-form-label">Transaktionsdatum<span class="text-danger">*</span></label>
                        <div class="mb-3">
                            <input type="date" class="form-control" name="transactionDate" id="transactionDate"
                                   max="${java.time.LocalDate.now().toString()}" required>
                            <div class="form-text">Datum der tatsächlichen Zahlung.</div>
                        </div>

                        <!-- Betrag -->
                        <label for="transactionAmount" class="col-form-label">Betrag in €<span class="text-danger">*</span></label>
                        <div class="mb-3">
                            <input type="number" class="form-control" name="transactionAmount" id="transactionAmount"
                                   step="0.01" min="0.01" placeholder="0.00" required>
                            <div class="form-text">Mindestbetrag: 0,01 €</div>
                        </div>

                        <br/>

                        <div class="form-group row">
                            <div class="col-sm-10">
                                <button type="submit" class="btn btn-primary bi-floppy"> Einnahme speichern</button>
                                <a href="/accountings/show-accountings" class="btn btn-outline-secondary ms-2">Abbrechen</a>
                            </div>
                        </div>

                    </form>

                </div>
            </div>
        </div>
    </div>

    <br/>
    @template.components.display-statusmessage(successMessage, errorMessage)

`)