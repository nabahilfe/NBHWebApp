<!--
  Copyright (c) 2025–2026 Maximilian Weißböck
  Licensed under the MIT License (see LICENSE file).
-->

@import java.time.LocalDate

@import static eu.nabahilfe.webapp.DateFormatter.*
@import static eu.nabahilfe.webapp.NumberFormatter.*

@import eu.nabahilfe.webapp.accountings.AccountingEntry
@import eu.nabahilfe.webapp.accountings.TransactionType

@param AccountingEntry accountingEntry

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

    <h3>Buchungsdatensatz anlegen</h3>

    <div class="row">
        <div class="col-lg-7">

            <!-- Read-only details -->
            <div class="card shadow mb-3" style="background-color: #ECFEFF;">
                <div class="card-header fw-bold">Buchungsdetails</div>
                <div class="card-body">
                    <dl class="row mb-0">
                        <dt class="col-sm-4">Buchung für</dt>
                        <dd class="col-sm-8">${accountingEntry.getAccountableName()}</dd>

                        <dt class="col-sm-4">Buchungsart</dt>
                        <dd class="col-sm-8">
                            @if(accountingEntry.getTransactionDisplayType().equals(TransactionType.INCOME.getCode()))
                                <span class="badge bg-success">Einnahme</span>
                            @else
                                <span class="badge bg-danger">Ausgabe</span>
                            @endif
                        </dd>





                        <dt class="col-sm-4">Transaktionsdatum</dt>
                        <dd class="col-sm-8">${dateReverseDE(accountingEntry.getTransactionDate())}</dd>

                        <dt class="col-sm-4">Betrag</dt>
                        <dd class="col-sm-8">€ ${numberDE(accountingEntry.getTransactionAmount())}</dd>

                        <dt class="col-sm-4">Von Mitglied</dt>
                        <dd class="col-sm-8">${accountingEntry.getAccountableMember().getNameAndAddress()}</dd>
                    </dl>
                </div>
            </div>

            <!-- Booking form -->
            <div class="card shadow" style="background-color: #ECFEFF;">
                <div class="card-header fw-bold">Buchung</div>
                <div class="card-body">
                    <form action="${accountingEntry.getId() == null ? "/accountings/save-accounting" : "/accountings/view-accounting/" + accountingEntry.getId()}" method="post">
                        ${csrfHiddenInput}

                        <label for="accountingDate" class="col-form-label">Buchungsdatum<span class="text-danger">*</span></label>
                        <div class="mb-3">
                            <input type="date" class="form-control" name="accountingDate" id="accountingDate"
                                   max="${java.time.LocalDate.now().toString()}" required>
                            <div class="form-text">Datum, an dem die Transaktion am Konto verbucht wurde.</div>
                        </div>

                        <div class="form-group row">
                            <div class="col-sm-10">
                                <button type="submit" class="btn btn-primary bi-floppy"> Speichern</button>
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