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
@param String monthlyHoursJson
@param long totalHours
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
    <p>Auswertung der verbuchten Zeitschecks nach Kategorie und Monat.</p>

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
    <p class="fw-semibold mb-3">Insgesamt geleistete Stunden: ${totalHours}</p>

    <div class="row g-4">
        <div class="col-12 ${hasData ? "col-lg-6" : ""}">
            @if(hasData)
                <div class="p-3 text-success-emphasis border border-success-subtle rounded-3 shadow h-100"
                        style="background-color: #ECFEFF;">
                    <h4>Stunden nach Kategorie</h4>
                    <div style="position: relative; width: 100%; max-width: 700px; height: clamp(336px, 42vw, 480px); margin: auto;">
                        <canvas id="timeTransferStatsChart" data-chart="${chartDataJson}"></canvas>
                    </div>
                </div>
            @else
                <div class="p-3 text-success-emphasis border border-success-subtle rounded-3 shadow"
                        style="background-color: #ECFEFF;">
                    <h4>Stunden nach Kategorie</h4>
                    <p class="text-muted mb-0">Für ${selectedYear} sind keine Zeitschecks vorhanden.</p>
                </div>
            @endif
        </div>

        <div class="col-12 ${hasData ? "col-lg-6" : ""}">
            <div class="p-3 text-success-emphasis border border-success-subtle rounded-3 shadow h-100"
                    style="background-color: #ECFEFF;">
                <h4>Geleistete Stunden pro Monat</h4>
                <div style="position: relative; width: 100%; max-width: 900px; height: clamp(336px, 42vw, 480px); margin: auto;">
                    <canvas id="monthlyHoursChart" data-chart="${monthlyHoursJson}"></canvas>
                </div>
            </div>
        </div>
    </div>

    <script src="/webjars/chart.js/4.4.1/dist/chart.umd.js"></script>
    <script>
        document.addEventListener("DOMContentLoaded", function() {
            const chartElement = document.getElementById('timeTransferStatsChart');
            if (chartElement) {
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
                        maintainAspectRatio: false,
                        plugins: {
                            legend: {
                                onClick: function() {}
                            },
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
            }

            const monthlyChartElement = document.getElementById('monthlyHoursChart');
            const monthlyHours = JSON.parse(monthlyChartElement.getAttribute('data-chart'));
            new Chart(monthlyChartElement.getContext('2d'), {
                type: 'bar',
                data: {
                    labels: ['Jänner', 'Februar', 'März', 'April', 'Mai', 'Juni', 'Juli', 'August', 'September', 'Oktober', 'November', 'Dezember'],
                    datasets: [{
                        label: 'Geleistete Stunden',
                        data: monthlyHours,
                        backgroundColor: 'rgba(54, 162, 235, 0.6)',
                        borderColor: 'rgba(35, 115, 175, 1)',
                        borderWidth: 1
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    scales: {
                        y: {
                            beginAtZero: true,
                            title: {
                                display: true,
                                text: 'Stunden'
                            },
                            ticks: {
                                precision: 0
                            }
                        }
                    },
                    plugins: {
                        legend: {
                            display: false
                        },
                        tooltip: {
                            callbacks: {
                                label: function(context) {
                                    return context.parsed.y + ' Stunden';
                                }
                            }
                        }
                    }
                }
            });
        });
    </script>
`)
