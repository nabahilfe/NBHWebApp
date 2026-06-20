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

    <h3>${transaction.getTransactionType().equals(TransactionType.INCOME.name()) ? "Einnahme" : "Ausgabe"}</h3>

    <div class="row g-4">
        <div class="col-lg-7">
            <div class="card">
                <div class="card-header ${transaction.getTransactionType().equals(TransactionType.INCOME.name()) ? "bg-primary" : "bg-primary"} text-white">
                    Details
                </div>
                <div class="card-body">
                    <dl class="row mb-0">
                        <dt class="col-sm-4">Art</dt>
                        <dd class="col-sm-8">
                            ${transaction.getTransactionType().equals(TransactionType.INCOME.name()) ? "Einnahme" : "Ausgabe"}
                        </dd>

                        <dt class="col-sm-4">Beschreibung</dt>
                        <dd class="col-sm-8">${transaction.getDescription()}</dd>

                        <dt class="col-sm-4">Transaktionsdatum</dt>
                        <dd class="col-sm-8">${dateReverseDE(transaction.getTransactionDate())}</dd>

                        <dt class="col-sm-4">Betrag</dt>
                        <dd class="col-sm-8">${numberDE(transaction.getAmount())}&nbsp;&euro;</dd>

                        <dt class="col-sm-4">Verbucht</dt>
                        <dd class="col-sm-8">
                            @if(transaction.getAccountedBy() != null)
                                Ja &ndash; <a href="/accountings/view-accounting/${transaction.getAccountedBy().getId()}">Buchung ansehen</a>
                            @else
                                <span class="text-primary">Noch nicht verbucht</span>
                            @endif
                        </dd>
                    </dl>
                </div>
            </div>
        </div>
    </div>

    <br/>

    <a href="${transaction.getTransactionType().equals(TransactionType.INCOME.name()) ? "/accountings/income" : "/accountings/expense"}"
       class="btn btn-outline-primary bi-plus"> Neue ${transaction.getTransactionType().equals(TransactionType.INCOME.name()) ? "Einnahme" : "Ausgabe"} erfassen</a>

    <br/><br/>

    @template.components.display-statusmessage(successMessage, errorMessage)

`)
