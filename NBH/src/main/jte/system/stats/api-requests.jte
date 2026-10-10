<!-- src/main/jte/system/stats/api-requests.jte -->
@param java.util.List<eu.nabahilfe.webapp.system.stats.ApiRequestStatistic> statistics
@param java.lang.String dailyTotalsJson
@param java.lang.String filteredDailyTotalsJson
@param eu.nabahilfe.webapp.system.GlobalModelAttributes globalModelAttributes
@param eu.nabahilfe.webapp.security.CsrfHiddenInput csrfHiddenInput
@param eu.nabahilfe.webapp.security.ViewContext ctxt

@template.layout.mainpage(
    globalModelAttributes = globalModelAttributes,
    csrfHiddenInput = csrfHiddenInput,
    ctxt = ctxt,
    content = @`
    <h3>API Aufrufe</h3>
    <button type="button" id="toggle-root-requests" class="btn btn-success active btn-sm mb-2"
            aria-pressed="true">
        Aufrufe ohne Pfad sind ausgeblendet
    </button>

    <h4>Requests pro Tag</h4>
    <p class="text-body-secondary">Letzte 30 Tage (einschlie&#223;lich heute)</p>
    <div class="position-relative mb-4" style="height: 416px; width: 100%;">
        <canvas id="daily-api-requests-chart" data-chart="${dailyTotalsJson}"
            data-filtered-chart="${filteredDailyTotalsJson}"
                role="img" aria-label="Summierte Systemanfragen pro Tag in den letzten 30 Tagen"></canvas>
    </div>

    <h4>Requests per URL Pattern</h4> 
    
    <p class="text-body-secondary">Letzte 7 Tage (einschlie&#223;lich heute)</p>
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
                    <tr><td colspan="5" class="text-center text-body-secondary">Keine Systemanfragen in den letzten 7 Tagen vorhanden.</td></tr>
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

    <script src="/webjars/chart.js/4.4.1/dist/chart.umd.js"></script>
    <script>
        document.addEventListener('DOMContentLoaded', function () {
            const chartElement = document.getElementById('daily-api-requests-chart');
            const dailyTotals = JSON.parse(chartElement.dataset.chart);
            const filteredDailyTotals = JSON.parse(chartElement.dataset.filteredChart);
            const chart = new Chart(chartElement.getContext('2d'), {
                type: 'bar',
                data: {
                    labels: dailyTotals.map(entry => entry.requestDate),
                    datasets: [{
                        label: 'Requests',
                        data: filteredDailyTotals.map(entry => entry.requestCount),
                        backgroundColor: 'rgba(54, 162, 235, 0.6)',
                        borderColor: 'rgba(35, 115, 175, 1)',
                        borderWidth: 1
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    scales: {
                        x: {
                            title: { display: true, text: 'Datum' },
                            ticks: {
                                maxRotation: 45,
                                minRotation: 0,
                                callback: function (value) {
                                    const date = this.getLabelForValue(value);
                                    return date.slice(8, 10) + '.' + date.slice(5, 7) + '.';
                                }
                            }
                        },
                        y: {
                            beginAtZero: true,
                            title: { display: true, text: 'Requests' },
                            ticks: { precision: 0 }
                        }
                    },
                    plugins: {
                        legend: { display: false }
                    }
                }
            });
            const table = document.getElementById('api-request-statistics');
            const tbody = table.querySelector('tbody');
            const rootRequestToggle = document.getElementById('toggle-root-requests');
            const headers = table.querySelectorAll('thead th');
            const collator = new Intl.Collator('de', { numeric: true });
            let sortedColumn = 4;
            let ascending = false;

            function applyRootRequestFilter() {
                const hideRootRequests = rootRequestToggle.getAttribute('aria-pressed') === 'true';
                rootRequestToggle.textContent = hideRootRequests
                    ? 'Aufrufe ohne Pfad sind ausgeblendet'
                    : 'Aufrufe ohne Pfad werden angezeigt';
                rootRequestToggle.classList.toggle('btn-success', hideRootRequests);
                rootRequestToggle.classList.toggle('btn-primary', !hideRootRequests);
                rootRequestToggle.classList.toggle('active', hideRootRequests);
                tbody.querySelectorAll('tr[data-statistic]').forEach(function (row) {
                    row.hidden = hideRootRequests && row.cells[2].textContent.trim() === '/';
                });
                const displayedTotals = hideRootRequests ? filteredDailyTotals : dailyTotals;
                chart.data.datasets[0].data = displayedTotals.map(entry => entry.requestCount);
                chart.update();
            }

            applyRootRequestFilter();
            rootRequestToggle.addEventListener('click', function () {
                const isPressed = rootRequestToggle.getAttribute('aria-pressed') === 'true';
                rootRequestToggle.setAttribute('aria-pressed', String(!isPressed));
                applyRootRequestFilter();
            });

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