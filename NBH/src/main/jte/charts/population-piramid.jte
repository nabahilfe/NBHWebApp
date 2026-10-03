<!--
  Copyright (c) 2025–2026 Maximilian Weißböck
  Licensed under the MIT License (see LICENSE file).
-->

@import eu.nabahilfe.webapp.system.GlobalModelAttributes
@import java.time.LocalDate
@import java.util.List
@import eu.nabahilfe.webapp.charts.AgeDecadeData

@import static eu.nabahilfe.webapp.system.DateFormatter.*

@param java.lang.String successMessage = null
@param java.lang.String errorMessage = null

@param eu.nabahilfe.webapp.security.CsrfHiddenInput csrfHiddenInput
@param eu.nabahilfe.webapp.security.ViewContext ctxt
@param GlobalModelAttributes globalModelAttributes

<!-- Die Liste chartData muss genau 10 Records enthalten.
 Jeder Record enthält die Anzahl der Männer und Frauen in dem jeweiligen Jahrzehnt. -->
@param List<AgeDecadeData> chartData

@template.layout.mainpage(
    globalModelAttributes = globalModelAttributes,
    csrfHiddenInput = csrfHiddenInput,
    ctxt = ctxt,
    content = @`

    <h3>Alterspyramide unserer Mitglieder</h3>

    <div class="row">
        @template.components.display-statusmessage(successMessage, errorMessage)
    </div>

    <div class="row" style="margin-top: 20px;">
        <div style="width: 100%; max-width: 650px; margin: auto;">
            <!-- Daten werden getrennt und sicher als JSON-Arrays in die Attribute geschrieben -->
            <canvas id="agePyramidChart" 
                    data-male="${chartData != null ? chartData.stream().map(d -> String.valueOf(d.male())).toList().toString() : "[]"}"
                    data-female="${chartData != null ? chartData.stream().map(d -> String.valueOf(d.female())).toList().toString() : "[]"}">
            </canvas>
        </div>
    </div>

    <!-- Laden der Chart.js Library -->
    <script src="/webjars/chart.js/4.4.1/dist/chart.umd.js"></script>

    <script>
        document.addEventListener("DOMContentLoaded", function() {
            const chartElement = document.getElementById('agePyramidChart');
            const ctx = chartElement.getContext('2d');
            const decades = ['90+', '80-89', '70-79', '60-69', '50-59', '40-49', '30-39', '20-29', '10-19', '0-9'];

            // Daten aus den Attributen laden. Männer-Werte werden für die linke Chart-Seite negativ gesetzt.
            const rawMale = JSON.parse(chartElement.getAttribute('data-male'));
            const maleData = rawMale.map(value => -value);
            const femaleData = JSON.parse(chartElement.getAttribute('data-female'));

            new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: decades,
                    datasets: [
                        {
                            label: 'Männer',
                            data: maleData,
                            backgroundColor: 'rgba(54, 162, 235, 0.45)', // Blau
                            borderColor: 'rgba(54, 162, 235, 1)',
                            borderWidth: 1,
                            barPercentage: 0.85,
                            categoryPercentage: 0.9
                        },
                        {
                            label: 'Frauen',
                            data: femaleData,
                            backgroundColor: 'rgba(255, 99, 132, 0.45)', // Rosa/Rot
                            borderColor: 'rgba(255, 99, 132, 1)',
                            borderWidth: 1,
                            barPercentage: 0.85,
                            categoryPercentage: 0.9
                        }
                    ]
                },
                options: {
                    indexAxis: 'y', // Horizontale Balken
                    responsive: true,
                    scales: {
                        x: {
                            stacked: true, // Stapelt die Datensätze gespiegelt auf der Nulllinie
                            ticks: {
                                color: '#111111',
                                callback: function(value) {
                                    return Math.abs(value); // Minuszeichen auf der linken Achsenseite ausblenden
                                }
                            },
                            title: {
                                display: true,
                                text: 'Anzahl der Personen'
                            }
                        },
                        y: {
                            stacked: true,
                            position: 'center', // Y-Achse (Stamm) bleibt exakt in der Mitte
                            ticks: {
                                color: '#111111'
                            },
                            grid: {
                                drawOnChartArea: false
                            }
                        }
                    },
                    plugins: {
                        legend: {
                            display: true, // Legende oben anzeigen, da wir nun 2 Farben haben
                            position: 'top'
                        },
                        tooltip: {
                            callbacks: {
                                label: function(context) {
                                    let label = context.dataset.label || '';
                                    if (label) {
                                        label += ': ';
                                    }
                                    // Zeigt im Tooltip beim Hovern den echten positiven Wert an
                                    label += Math.abs(context.parsed.x);
                                    return label;
                                }
                            }
                        }
                    }
                }
            });
        });
    </script>
`)
