<!-- src/main/jte/system/stats/api-requests.jte -->
@param java.util.List<eu.nabahilfe.webapp.system.stats.ApiRequestStatistic> statistics
@param eu.nabahilfe.webapp.system.GlobalModelAttributes globalModelAttributes
@param eu.nabahilfe.webapp.security.CsrfHiddenInput csrfHiddenInput
@param eu.nabahilfe.webapp.security.ViewContext ctxt

@template.layout.mainpage(
    globalModelAttributes = globalModelAttributes,
    csrfHiddenInput = csrfHiddenInput,
    ctxt = ctxt,
    content = @`
    <h3>API Systemanfragen</h3>
    <p class="text-body-secondary">Letzte 30 Tage (einschlie&#223;lich heute)</p>

    <div class="table-responsive">
        <table id="api-request-statistics" class="table table-bordered table-hover table-sm shadow h-100">
            <thead class="table-light">
                <tr>
                    <th scope="col" aria-sort="none">
                        <button type="button" class="btn btn-link text-body fw-bold text-decoration-none p-0 text-nowrap" data-column="0" title="Nach requestDate sortieren">
                            requestDate <i class="bi bi-arrow-down-up" aria-hidden="true"></i>
                        </button>
                    </th>
                    <th scope="col" aria-sort="none">
                        <button type="button" class="btn btn-link text-body fw-bold text-decoration-none p-0 text-nowrap" data-column="1" title="Nach httpMethod sortieren">
                            httpMethod <i class="bi bi-arrow-down-up" aria-hidden="true"></i>
                        </button>
                    </th>
                    <th scope="col" aria-sort="none">
                        <button type="button" class="btn btn-link text-body fw-bold text-decoration-none p-0 text-nowrap" data-column="2" title="Nach urlPattern sortieren">
                            urlPattern <i class="bi bi-arrow-down-up" aria-hidden="true"></i>
                        </button>
                    </th>
                    <th scope="col" class="text-end" aria-sort="none">
                        <button type="button" class="btn btn-link text-body fw-bold text-decoration-none p-0 text-nowrap" data-column="3" title="Nach statusCode sortieren">
                            statusCode <i class="bi bi-arrow-down-up" aria-hidden="true"></i>
                        </button>
                    </th>
                    <th scope="col" class="text-end" aria-sort="descending">
                        <button type="button" class="btn btn-link text-body fw-bold text-decoration-none p-0 text-nowrap" data-column="4" title="Nach requestCount sortieren">
                            requestCount <i class="bi bi-sort-down" aria-hidden="true"></i>
                        </button>
                    </th>
                </tr>
            </thead>
            <tbody>
                @if(statistics.isEmpty())
                    <tr><td colspan="5" class="text-center text-body-secondary">Keine Systemanfragen in den letzten 30 Tagen vorhanden.</td></tr>
                @else
                    @for(var statistic : statistics)
                        <tr data-statistic>
                            <td class="text-nowrap">${statistic.getRequestDate().toString()}</td>
                            <td>${statistic.getHttpMethod()}</td>
                            <td class="text-break">${statistic.getUrlPattern()}</td>
                            <td class="text-end">${statistic.getStatusCode()}</td>
                            <td class="text-end">${statistic.getRequestCount()}</td>
                        </tr>
                    @endfor
                @endif
            </tbody>
        </table>
    </div>

    <script>
        document.addEventListener('DOMContentLoaded', function () {
            const table = document.getElementById('api-request-statistics');
            const tbody = table.querySelector('tbody');
            const headers = table.querySelectorAll('thead th');
            const collator = new Intl.Collator('de', { numeric: true });
            let sortedColumn = 4;
            let ascending = false;

            table.querySelectorAll('button[data-column]').forEach(function (button) {
                button.addEventListener('click', function () {
                    const column = Number(button.dataset.column);
                    ascending = column === sortedColumn ? !ascending : true;
                    sortedColumn = column;
                    const rows = Array.from(tbody.querySelectorAll('tr[data-statistic]'));

                    rows.sort(function (firstRow, secondRow) {
                        const firstValue = firstRow.cells[column].textContent.trim();
                        const secondValue = secondRow.cells[column].textContent.trim();
                        let comparison;
                        if (column >= 3) {
                            const firstNumber = BigInt(firstValue || '0');
                            const secondNumber = BigInt(secondValue || '0');
                            comparison = firstNumber < secondNumber ? -1 : firstNumber > secondNumber ? 1 : 0;
                        } else {
                            comparison = collator.compare(firstValue, secondValue);
                        }
                        return ascending ? comparison : -comparison;
                    });

                    tbody.append(...rows);
                    headers.forEach(function (header, index) {
                        const active = index === column;
                        header.setAttribute('aria-sort', active ? (ascending ? 'ascending' : 'descending') : 'none');
                        header.querySelector('i').className = active
                            ? (ascending ? 'bi bi-sort-up' : 'bi bi-sort-down')
                            : 'bi bi-arrow-down-up';
                    });
                });
            });
        });
    </script>
`)