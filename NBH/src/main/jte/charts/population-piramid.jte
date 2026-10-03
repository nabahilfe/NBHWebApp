<!--
  Copyright (c) 2025–2026 Maximilian Weißböck
  Licensed under the MIT License (see LICENSE file).
-->

@import eu.nabahilfe.webapp.system.GlobalModelAttributes
@import java.time.LocalDate

@import static eu.nabahilfe.webapp.system.DateFormatter.*

@param java.lang.String successMessage = null
@param java.lang.String errorMessage = null

@param eu.nabahilfe.webapp.security.CsrfHiddenInput csrfHiddenInput
@param eu.nabahilfe.webapp.security.ViewContext ctxt
@param GlobalModelAttributes globalModelAttributes
<!-- Die Liste chartData muss genau 9 Zahlen enthalten. Jede Zahl steht für die Gesamtzahl der Personen in dem jeweiligen Jahrzehnt. -->
@param java.util.List<Integer> chartData 

@template.layout.mainpage(
    globalModelAttributes = globalModelAttributes,
    csrfHiddenInput = csrfHiddenInput,
    ctxt = ctxt,
    content = @`

    <h3>Alterspyramide</h3>

    <div class="row">
        @template.components.display-statusmessage(successMessage, errorMessage)
    </div>

    <div class="row" style="margin-top: 20px;">
        <div style="width: 100%; max-width: 600px; margin: auto;">
            <!-- Das data-Attribut transportiert die Java-Liste sicher als Text-String zum Client -->
            <canvas id="christmasTreeChart" data-chart="${chartData != null ? chartData.toString() : "[]"}"></canvas>
        </div>
    </div>

    <!-- Laden der Chart.js Library -->
    <script src="/webjars/chart.js/4.4.1/dist/chart.umd.js"></script>

    <script>
        document.addEventListener("DOMContentLoaded", function() {
            const chartElement = document.getElementById('christmasTreeChart');
            const ctx = chartElement.getContext('2d');
            const decades = ['80+', '70-79', '60-69', '50-59', '40-49', '30-39', '20-29', '10-19', '0-9'];

            // Holt die Daten standardkonform aus dem HTML-Attribut und parst sie als JS-Array
            const realData = JSON.parse(chartElement.getAttribute('data-chart'));
            const centeredData = realData.map(value => [-value / 2, value / 2]);

            new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: decades,
                    datasets: [{
                        label: 'Personen gesamt',
                        data: centeredData,
                        backgroundColor: 'rgba(40, 167, 69, 0.85)',
                        borderColor: 'rgba(30, 126, 52, 1)',
                        borderWidth: 1,
                        barPercentage: 0.85,
                        categoryPercentage: 0.9
                    }]
                },
                options: {
                    indexAxis: 'y',
                    responsive: true,
                    scales: {
                        x: {
                            ticks: {
                                callback: function(value) {
                                    return Math.abs(value * 2);
                                }
                            }
                        },
                        y: {
                            position: 'center',
                            grid: {
                                drawOnChartArea: false
                            }
                        }
                    },
                    plugins: {
                        legend: { display: false },
                        tooltip: {
                            callbacks: {
                                label: function(context) {
                                    return ' Personen gesamt: ' + realData[context.dataIndex];
                                }
                            }
                        }
                    }
                }
            });
        });
    </script>
`)
