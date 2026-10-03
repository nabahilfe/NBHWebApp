<!--
  Copyright (c) 2025–2026 Maximilian Weißböck
  Licensed under the MIT License (see LICENSE file).
-->

@import eu.nabahilfe.webapp.system.GlobalModelAttributes
@import java.util.List

@param java.lang.String successMessage = null
@param java.lang.String errorMessage = null
@param List<Integer> years
@param int selectedYear
@param String chartDataJson
@param boolean hasData

@param eu.nabahilfe.webapp.security.CsrfHiddenInput csrfHiddenInput
@param eu.nabahilfe.webapp.security.ViewContext ctxt
@param GlobalModelAttributes globalModelAttributes

@template.layout.mainpage(
    globalModelAttributes = globalModelAttributes,
    csrfHiddenInput = csrfHiddenInput,
    ctxt = ctxt,
    content = @`

    <h3>Statistik Zeitschecks</h3>

    <div class="row">
        @template.components.display-statusmessage(successMessage, errorMessage)
    </div>

    <form method="get" action="/charts/timecheques/distribution" class="row g-2 align-items-end mb-3">
        <div class="col-auto">
            <label for="year" class="form-label mb-1">Jahr</label>
            <select id="year" name="year" class="form-select form-select-sm" onchange="this.form.submit()">
                @for(int year : years)
                    <option value="${year}" selected="${year == selectedYear}">${year}</option>
                @endfor
            </select>
        </div>
    </form>

    @if(hasData)
        <div class="row" style="margin-top: 20px;">
            <div style="width: 100%; max-width: 700px; margin: auto;">
                <canvas id="timeTransferStatsChart" data-chart="${chartDataJson}"></canvas>
            </div>
        </div>
    @else
        <p class="text-muted">Für ${selectedYear} sind keine Zeitschecks vorhanden.</p>
    @endif

    <script src="/webjars/chart.js/4.4.1/dist/chart.umd.js"></script>
    @if(hasData)
        <script>
            document.addEventListener("DOMContentLoaded", function() {
                const chartElement = document.getElementById('timeTransferStatsChart');
                const chartData = JSON.parse(chartElement.getAttribute('data-chart'));
                const colors = chartData.map((entry, index) => 'hsl(' + ((index * 137.508) % 360) + ' 62% 58%)');

                new Chart(chartElement.getContext('2d'), {
                    type: 'pie',
                    data: {
                        labels: chartData.map(entry => entry.offer),
                        datasets: [{
                            data: chartData.map(entry => entry.hours),
                            backgroundColor: colors,
                            borderColor: '#ffffff',
                            borderWidth: 2
                        }]
                    },
                    options: {
                        responsive: true,
                        plugins: {
                            tooltip: {
                                callbacks: {
                                    label: function(context) {
                                        return context.label + ': ' + context.parsed + ' Stunden';
                                    }
                                }
                            }
                        }
                    }
                });
            });
        </script>
    @endif
`)
