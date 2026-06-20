<!--
  Copyright (c) 2025–2026 Maximilian Weißböck
  Licensed under the MIT License (see LICENSE file).
-->

@import static eu.nabahilfe.webapp.DateFormatter.*
@import static eu.nabahilfe.webapp.NumberFormatter.*
@import eu.nabahilfe.webapp.accountings.Transaction
@import eu.nabahilfe.webapp.accountings.TransactionType

@param Transaction transaction

@param java.lang.String successMessage = null
@param java.lang.String errorMessage = null

@param eu.nabahilfe.webapp.security.CsrfHiddenInput csrfHiddenInput
@param eu.nabahilfe.webapp.security.ViewContext ctxt


@template.layout.mainpage(
    csrfHiddenInput = csrfHiddenInput,
    ctxt = ctxt,
    content = @`

    <h3>Sonstige Buchung anlegen</h3>
    <br/>

    <div class="row">
        <div class="col-lg-7">

            <!-- Read-only transaction details -->
            <div class="card shadow mb-3" style="background-color: #ECFEFF;">
                <div class="card-header fw-bold">Transaktionsdetails</div>
                <div class="card-body">
                    <dl class="row mb-0">
                        <dt class="col-sm-4">Art</dt>
                        <dd class="col-sm-8">
                            @if(transaction.getTransactionType().equals(TransactionType.INCOME.name()))
                                <span class="badge bg-success">Einnahme</span>
                            @else
                                <span class="badge bg-danger">Ausgabe</span>
                            @endif
                        </dd>

                        <dt class="col-sm-4">Beschreibung</dt>
                        <dd class="col-sm-8">${transaction.getDescription()}</dd>

                        <dt class="col-sm-4">Transaktionsdatum</dt>
                        <dd class="col-sm-8">${dateReverseDE(transaction.getTransactionDate())}</dd>

                        <dt class="col-sm-4">Betrag</dt>
                        <dd class="col-sm-8">€ ${numberDE(transaction.getAmount())}</dd>
                    </dl>
                </div>
            </div>

            <!-- Booking form -->
            <div class="card shadow" style="background-color: #ECFEFF;">
                <div class="card-header fw-bold">Buchung</div>
                <div class="card-body">
                    <form action="/accountings/save-transaction-booking" method="post">
                        ${csrfHiddenInput}
                        <input type="hidden" name="transactionId" value="${transaction.getId()}">

                        <label for="accountingDate" class="col-form-label">Buchungsdatum<span class="text-danger">*</span></label>
                        <div class="mb-3">
                            <input type="date" class="form-control" name="accountingDate" id="accountingDate"
                                   value="${java.time.LocalDate.now().toString()}" required>
                            <div class="form-text">Datum, an dem die Transaktion im Konto verbucht wurde.</div>
                        </div>

                        <div class="form-group row">
                            <div class="col-sm-10">
                                <button type="submit" class="btn btn-primary bi-floppy"> Buchen</button>
                                <a href="/accountings/misc-unaccounted" class="btn btn-outline-secondary ms-2">Abbrechen</a>
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
