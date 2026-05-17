<!--
  Copyright (c) 2025–2026 Maximilian Weißböck
  Licensed under the MIT License (see LICENSE file).
-->

@import java.time.LocalDate

@import static eu.nabahilfe.webapp.DateFormatter.*
@import static eu.nabahilfe.webapp.NumberFormatter.*
@import eu.nabahilfe.webapp.accountings.AccountingEntry

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

    <h3>Buchung</h3>

    <div class="row g-4">
        <div class="col-lg-7">
            <div class="card">
                <div class="card-header bg-primary text-white">Buchungsdetails</div>
                <div class="card-body">
                    <dl class="row mb-0">
                        <dt class="col-sm-4">Buchung f&uuml;r</dt>
                        <dd class="col-sm-8">${accountingEntry.getAccountableClassDisplayName()}</dd>

                        <dt class="col-sm-4">Mitglied</dt>
                        <dd class="col-sm-8">${accountingEntry.getAccountableMember() != null ? accountingEntry.getAccountableMember().getNameAndAddress() : "-"}</dd>

                        <dt class="col-sm-4">Buchungsart</dt>
                        <dd class="col-sm-8">${accountingEntry.getTransactionDisplayType()}</dd>

                        <dt class="col-sm-4">Transaktionsdatum</dt>
                        <dd class="col-sm-8">${dateReverseDE(accountingEntry.getTransactionDate())}</dd>

                        <dt class="col-sm-4">Betrag</dt>
                        <dd class="col-sm-8">${numberDE(accountingEntry.getTransactionAmount())}&nbsp;&euro;</dd>

                        <dt class="col-sm-4">Buchungsdatum</dt>
                        <dd class="col-sm-8">${dateReverseDE(accountingEntry.getAccountingDate())}</dd>

                        <dt class="col-sm-4">Beschreibung</dt>
                        <dd class="col-sm-8">${accountingEntry.getDescription() != null && !accountingEntry.getDescription().isEmpty() ? accountingEntry.getDescription() : "-"}</dd>
                    </dl>
                </div>
            </div>
        </div>
    </div>


    <br/>

    <form action="/timecheques/unaccounted" method="get">
        <button type="submit" class="btn btn-outline-primary bi-list-check"> Offene Zeitschecks anzeigen</button>
    </form>

    <br/>

    @template.components.display-statusmessage(successMessage, errorMessage)

`)