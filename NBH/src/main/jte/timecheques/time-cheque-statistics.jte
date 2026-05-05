@import java.util.List
@import java.math.BigDecimal

@param int selectedYear
@param List<Object[]> stats

@param java.lang.String successMessage = null
@param java.lang.String errorMessage = null

@param eu.nabahilfe.webapp.security.CsrfHiddenInput csrfHiddenInput
@param eu.nabahilfe.webapp.security.ViewContext ctxt

@template.layout.mainpage(
    csrfHiddenInput = csrfHiddenInput,
    ctxt = ctxt,
    content = @`

    <div class="container">
        <h3>Zeitscheck Statistik personalisiert</h3>
        <br/>

        <h4>Zeitschecks erhalten für erbrachte Leistungen</h4>



        <h4>Zeitschecks gekauft bzw. erhalten bei Eintritt</h4>
        <br/>


        @template.components.display-statusmessage(successMessage, errorMessage)

        <!-- Year selector -->
        <form method="GET" action="/timecheques/statistics" class="row g-3 align-items-center mb-4">
            <div class="col-auto">
                <label for="year" class="col-form-label">Jahr:</label>
            </div>
            <div class="col-auto">
                <input type="number" id="year" name="year" class="form-control"
                       value="${selectedYear}"
                       min="2000" max="2999"
                       style="width: 120px;"
                       required>
            </div>
            <div class="col-auto">
                <button type="submit" class="btn btn-primary">Anzeigen</button>
            </div>
        </form>

        <!-- Statistics table -->
        @if(stats == null || stats.isEmpty())
            <div class="alert alert-info">Keine Zeitschecks für das Jahr ${selectedYear} gefunden.</div>
        @else
            <table class="table table-hover table-sm">
                <thead class="table-light">
                    <tr>
                        <th scope="col">#</th>
                        <th scope="col">Mitglied</th>
                        <th scope="col" class="text-end">Zeitschecks</th>
                        <th scope="col" class="text-end">Stunden gesamt</th>
                    </tr>
                </thead>
                <tbody>
                    !{int[] counter = {1};}
                    @for(Object[] row : stats)
                        <tr>
                            <td>${counter[0]}</td>
                            <td>${(String) row[0]}</td>
                            <td class="text-end">${((Number) row[2]).longValue()}</td>
                            <td class="text-end">${((Number) row[1]).longValue()} h</td>
                        </tr>
                        !{counter[0]++;}
                    @endfor
                </tbody>
                <tfoot class="table-light fw-bold">
                    <tr>
                        <td colspan="2">Gesamt</td>
                        <td class="text-end">
                            !{long totalCount = stats.stream().mapToLong(r -> ((Number) r[2]).longValue()).sum();}
                            ${totalCount}
                        </td>
                        <td class="text-end">
                            !{long totalHours = stats.stream().mapToLong(r -> ((Number) r[1]).longValue()).sum();}
                            ${totalHours} h
                        </td>
                    </tr>
                </tfoot>
            </table>
        @endif
    </div>

`)
