<!--
  Copyright (c) 2025–2026 Maximilian Weißböck
  Licensed under the MIT License (see LICENSE file).
-->

@import java.util.List
@import java.math.BigDecimal

@import static eu.nabahilfe.webapp.DateFormatter.*
@import static eu.nabahilfe.webapp.NumberFormatter.*
@import eu.nabahilfe.webapp.accountings.AccountingEntry
@import eu.nabahilfe.webapp.accountings.TransactionType

@param List<AccountingEntry> entries
@param BigDecimal total
@param List<String> distinctAccountableClasses
@param List<Integer> years
@param int selectedYear
@param int selectedMonth
@param String selectedTransactionType
@param String selectedAccountableClass

@param java.lang.String successMessage = null
@param java.lang.String errorMessage = null

@param eu.nabahilfe.webapp.security.CsrfHiddenInput csrfHiddenInput
@param eu.nabahilfe.webapp.security.ViewContext ctxt


@template.layout.mainpage(
    csrfHiddenInput = csrfHiddenInput,
    ctxt = ctxt,
    content = @`

    <h3>Umsätze</h3>

    <!-- Filter form -->
    <form method="get" action="/accountings/show-accountings" class="row g-2 align-items-end mb-3">

        <div class="col-auto">
            <label for="year" class="form-label mb-1">Jahr</label>
            <select id="year" name="year" class="form-select form-select-sm" onchange="this.form.submit()">
                @for(int y : years)
                    <option value="${y}" selected="${y == selectedYear}">${y}</option>
                @endfor
            </select>
        </div>

        <div class="col-auto">
            <label for="month" class="form-label mb-1">Monat</label>
            <select id="month" name="month" class="form-select form-select-sm" onchange="this.form.submit()">
                <option value="0" selected="${selectedMonth == 0}">-- ALLE --</option>
                <option value="1"  selected="${selectedMonth == 1}">Jänner</option>
                <option value="2"  selected="${selectedMonth == 2}">Februar</option>
                <option value="3"  selected="${selectedMonth == 3}">März</option>
                <option value="4"  selected="${selectedMonth == 4}">April</option>
                <option value="5"  selected="${selectedMonth == 5}">Mai</option>
                <option value="6"  selected="${selectedMonth == 6}">Juni</option>
                <option value="7"  selected="${selectedMonth == 7}">Juli</option>
                <option value="8"  selected="${selectedMonth == 8}">August</option>
                <option value="9"  selected="${selectedMonth == 9}">September</option>
                <option value="10" selected="${selectedMonth == 10}">Oktober</option>
                <option value="11" selected="${selectedMonth == 11}">November</option>
                <option value="12" selected="${selectedMonth == 12}">Dezember</option>
            </select>
        </div>

        <div class="col-auto">
            <label for="accountableName" class="form-label mb-1">Umsatz Art</label>
            <select id="accountableName" name="accountableName" class="form-select form-select-sm" onchange="this.form.submit()">
                <option value="" selected="${selectedAccountableClass.isEmpty()}">-- ALLE --</option>
                @for(String cls : distinctAccountableClasses)
                    <option value="${cls}" selected="${cls.equals(selectedAccountableClass)}">${cls}</option>
                @endfor
            </select>
        </div>

        <div class="col-auto">
            <label for="transactionType" class="form-label mb-1">Buchungsart</label>
            <select id="transactionType" name="transactionType" class="form-select form-select-sm" onchange="this.form.submit()">
                <option value="${TransactionType.INCOME.name()}" selected="${TransactionType.INCOME.name().equals(selectedTransactionType)}">Einnahmen</option>
                <option value="${TransactionType.EXPENSE.name()}" selected="${TransactionType.EXPENSE.name().equals(selectedTransactionType)}">Ausgaben</option>
            </select>
        </div>

    </form>

    <!-- Table -->
    <div class="table-responsive">
        <table class="table table-striped table-hover table-sm align-middle">
            <thead class="table-success">
                <tr>
                    <th>Buchungsdatum</th>
                    <th>Buchung von</th>
                    <th>Art</th>
                    <th>Mitglied</th>
                    <th>Beschreibung</th>
                    <th>Transaktionsdatum</th>
                    <th class="text-end">Betrag</th>
                </tr>
            </thead>
            <tbody>
                @if(entries.isEmpty())
                    <tr>
                        <td colspan="7" class="text-center text-muted">Keine Einträge gefunden.</td>
                    </tr>
                @else
                    @for(AccountingEntry e : entries)
                        <tr>
                            <td>${dateReverseDE(e.getAccountingDate())}</td>
                            <td>${e.getCreatedBy() != null ? e.getCreatedBy().getName() : "-"}</td>
                            <td>${e.getAccountableName()}</td>
                            <td>${e.getAccountableMember() != null ? e.getAccountableMember().getName() : "-"}</td>
                            <td>${e.getDescription() != null ? e.getDescription() : "-"}</td>
                            <td>${dateReverseDE(e.getTransactionDate())}</td>
                            <td class="text-end">&euro; ${numberDE(e.getTransactionAmount())}</td>
                        </tr>
                    @endfor
                @endif
            </tbody>
            <tfoot class="table-success fw-bold">
                <tr>
                    <td colspan="6" class="text-end">Summe:</td>
                    <td class="text-end">&euro; ${numberDE(total)}</td>
                </tr>
            </tfoot>
        </table>
    </div>

    @template.components.display-statusmessage(successMessage, errorMessage)

`)
