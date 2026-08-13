<!--
  Copyright (c) 2025–2026 Maximilian Weißböck
  Licensed under the MIT License (see LICENSE file).
-->

@import eu.nabahilfe.webapp.GlobalModelAttributes
@param long unvalidatedCount = 0
@param long totalActiveCount = 0

@param eu.nabahilfe.webapp.security.CsrfHiddenInput csrfHiddenInput
@param eu.nabahilfe.webapp.security.ViewContext ctxt
@param GlobalModelAttributes globalModelAttributes


@template.layout.mainpage(
    globalModelAttributes = globalModelAttributes,
    csrfHiddenInput = csrfHiddenInput,
    ctxt = ctxt,
    content = @`

    <h3>Adressen validieren</h3>
    <br/>

    <div class="p-3 text-success-emphasis border border-success-subtle rounded-3 shadow mb-4" style="background-color: #ECFEFF;">

        <p id="initialInfo">
            Es wurden <b id="unvalidatedCount">${unvalidatedCount}</b> Mitglieder mit nicht validierter Adresse gefunden
            (Latitude/Longitude nicht gesetzt), von insgesamt <b>${totalActiveCount}</b> aktiven Mitgliedern.
        </p>

        <button type="button" id="startBtn" class="btn btn-success bi-geo-alt-fill"
                disabled="${unvalidatedCount == 0}"> Adressen validieren</button>

        @if (ctxt != null && ctxt.hasAnyRole("ADMIN"))
	        <button type="button" id="startAllBtn" class="btn btn-warning bi-arrow-repeat"
	                disabled="${totalActiveCount == 0}"> Alle Adressen neu validieren</button>
        @endif

        <div id="progressArea" class="mt-4" style="display:none;">

            <div class="alert alert-warning bi-exclamation-triangle-fill" role="alert">
                &nbsp;Bitte diese Seite während der Validierung nicht verlassen oder neu laden, sonst wird der Vorgang abgebrochen!
            </div>

            <div class="progress mb-2" role="progressbar" aria-label="Fortschritt" style="height: 1.5rem;">
                <div id="progressBar" class="progress-bar bg-success" style="width: 0%;">0%</div>
            </div>

            <p class="mb-1">
                Verarbeitet: <b id="processedCount">0</b> / <b id="totalCount">0</b>
                &nbsp;|&nbsp;
                <span class="text-success">Erfolgreich: <b id="successCount">0</b></span>
                &nbsp;|&nbsp;
                <span class="text-danger">Fehlgeschlagen: <b id="failedCount">0</b></span>
            </p>

            <p id="currentMemberLine" class="text-muted small mb-0"></p>

        </div>

        <div id="summaryArea" class="alert alert-success mt-4" style="display:none;" role="alert"></div>

    </div>

    <script>
        document.addEventListener("DOMContentLoaded", () => {
            const startBtn = document.getElementById("startBtn");
            const startAllBtn = document.getElementById("startAllBtn");
            const progressArea = document.getElementById("progressArea");
            const summaryArea = document.getElementById("summaryArea");
            const progressBar = document.getElementById("progressBar");
            const processedCountEl = document.getElementById("processedCount");
            const totalCountEl = document.getElementById("totalCount");
            const successCountEl = document.getElementById("successCount");
            const failedCountEl = document.getElementById("failedCount");
            const currentMemberLine = document.getElementById("currentMemberLine");

            let validationInProgress = false;

            window.addEventListener("beforeunload", (event) => {
                if (validationInProgress) {
                    event.preventDefault();
                    event.returnValue = "";
                }
            });

            function startValidation(streamUrl) {
                startBtn.disabled = true;
                startAllBtn.disabled = true;
                validationInProgress = true;
                progressArea.style.display = "block";
                summaryArea.style.display = "none";

                const eventSource = new EventSource(streamUrl);

                eventSource.addEventListener("progress", (event) => {
                    const data = JSON.parse(event.data);

                    totalCountEl.textContent = data.total;
                    processedCountEl.textContent = data.processed;
                    successCountEl.textContent = data.successCount;
                    failedCountEl.textContent = data.failedCount;

                    const pct = data.total > 0 ? Math.round((data.processed / data.total) * 100) : 100;
                    progressBar.style.width = pct + "%";
                    progressBar.textContent = pct + "%";

                    if (data.currentMemberName) {
                        const icon = data.currentSuccess ? "✅" : "❌";
                        currentMemberLine.textContent = icon + " " + data.currentMemberName;
                    }
                });

                eventSource.addEventListener("done", (event) => {
                    const data = JSON.parse(event.data);

                    validationInProgress = false;
                    summaryArea.style.display = "block";
                    summaryArea.classList.remove("alert-danger");
                    summaryArea.classList.add("alert-success");
                    summaryArea.textContent = "Fertig! " + data.processed + " Adressen verarbeitet – "
                            + data.successCount + " erfolgreich validiert, "
                            + data.failedCount + " nicht validiert.";

                    eventSource.close();
                });

                eventSource.onerror = () => {
                    validationInProgress = false;
                    summaryArea.style.display = "block";
                    summaryArea.classList.remove("alert-success");
                    summaryArea.classList.add("alert-danger");
                    summaryArea.textContent = "Die Verbindung wurde unterbrochen. Bitte Seite neu laden und erneut versuchen.";
                    eventSource.close();
                };
            }

            startBtn.addEventListener("click", () => {
                startValidation("/members/validate-addresses/stream");
            });

            startAllBtn.addEventListener("click", () => {
                if (confirm("Sollen wirklich ALLE aktiven Adressen neu validiert werden, auch die bereits validierten? Dies kann je nach Mitgliederanzahl einige Zeit dauern.")) {
                    startValidation("/members/validate-addresses/stream-all");
                }
            });
        });
    </script>

`)
