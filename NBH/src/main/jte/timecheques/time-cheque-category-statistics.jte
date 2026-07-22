<!--
  Copyright (c) 2025–2026 Maximilian Weißböck
  Licensed under the MIT License (see LICENSE file).
-->

@import java.util.List
@import java.time.LocalDate

@param int selectedYear
@param List<Object[]> categoryStats

@param java.lang.String successMessage = null
@param java.lang.String errorMessage = null

@param eu.nabahilfe.webapp.security.CsrfHiddenInput csrfHiddenInput
@param eu.nabahilfe.webapp.security.ViewContext ctxt

@template.layout.mainpage(
    csrfHiddenInput = csrfHiddenInput,
    ctxt = ctxt,
    content = @`

    <div class="container">
        <h3>Zeitschecks nach Kategorie</h3>
        <br/>


        <!-- Year selector -->
        !{int currentYear = LocalDate.now().getYear();}
        <div class="row g-3 align-items-center mb-4">
            <div class="col-auto">
                <label for="yearSelect" class="col-form-label">Jahr:</label>
            </div>
            <div class="col-auto">
                <select id="yearSelect" class="form-select" style="width: auto;"
                        onchange="reloadWithYear(this.value)">
                    <option value="${currentYear + 1}" selected="${selectedYear == currentYear + 1}">${currentYear + 1}</option>
                    @for(int y = currentYear; y >= currentYear - 6; y--)
                        <option value="${y}" selected="${y == selectedYear}">${y}</option>
                    @endfor
                    <option value="0" selected="${selectedYear == 0}">Alle Jahre</option>
                </select>
            </div>
        </div>

        <script>
            function reloadWithYear(year) {
                const url = new URL(window.location.href);
                url.searchParams.set('year', year);
                window.location.href = url.toString();
            }
        </script>


        !{String yearLabel = selectedYear == 0 ? "alle Jahre" : String.valueOf(selectedYear);}


        <h4>Erbrachte Leistungen nach Kategorie — ${yearLabel}</h4>
        <br/>

        @if(categoryStats == null || categoryStats.isEmpty())
            <div class="alert alert-info">Keine Leistungen für ${yearLabel} gefunden.</div>
        @else
            <table class="table table-bordered table-hover table-sm shadow h-100">
                <thead class="table-light">
                    <tr>
                        <th scope="col">#</th>
                        <th scope="col">Code</th>
                        <th scope="col">Kategorie</th>
                        <th scope="col" class="text-end">Anzahl</th>
                        <th scope="col" class="text-end">Stunden gesamt</th>
                    </tr>
                </thead>
                <tbody>
                    !{int[] counter = {1};}
                    @for(Object[] row : categoryStats)
                        <tr>
                            <td>${counter[0]}</td>
                            <td>${(String) row[0]}</td>
                            <td>${(String) row[1]}</td>
                            <td class="text-end">${((Number) row[3]).longValue()}</td>
                            <td class="text-end">${((Number) row[2]).longValue()} h</td>
                        </tr>
                        !{counter[0]++;}
                    @endfor
                </tbody>
                <tfoot class="table-light fw-bold">
                    <tr>
                        <td colspan="3">Gesamt</td>
                        <td class="text-end">
                            !{long totalCount = categoryStats.stream().mapToLong(r -> ((Number) r[3]).longValue()).sum();}
                            ${totalCount}
                        </td>
                        <td class="text-end">
                            !{long totalHours = categoryStats.stream().mapToLong(r -> ((Number) r[2]).longValue()).sum();}
                            ${totalHours} h
                        </td>
                    </tr>
                </tfoot>
            </table>
        @endif

        <br/>
        @template.components.display-statusmessage(successMessage, errorMessage)

    </div>

`)