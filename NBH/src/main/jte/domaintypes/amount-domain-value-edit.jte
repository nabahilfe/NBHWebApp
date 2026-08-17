<!--
  Copyright (c) 2025–2026 Maximilian Weißböck
  Licensed under the MIT License (see LICENSE file).
-->

@import eu.nabahilfe.webapp.GlobalModelAttributes
@import java.time.LocalDate
@import java.util.List
@import java.util.Map

@import eu.nabahilfe.webapp.domaintypes.AmountDomainValue
@import eu.nabahilfe.webapp.domaintypes.AmountDomainType

@param AmountDomainValue amountDomainValue
@param AmountDomainType[] amountDomainTypes
@param List<LocalDate> validFromOptions = null
@param LocalDate computedValidFrom = null
@param Map<String, String> validFromByCode = null

@param java.lang.String successMessage = null
@param java.lang.String errorMessage = null

@param eu.nabahilfe.webapp.security.CsrfHiddenInput csrfHiddenInput
@param eu.nabahilfe.webapp.security.ViewContext ctxt
@param GlobalModelAttributes globalModelAttributes


@template.layout.mainpage(
    globalModelAttributes = globalModelAttributes,
    csrfHiddenInput = csrfHiddenInput,
    ctxt = ctxt,
    content = @`

    <div class="container d-flex justify-content-center">
        <div class="row">
            <div class="col-auto">
                <div class="card shadow" style="background-color: #ECFEFF; min-width: 400px;">
                    <div class="card-body">

                        <h4 class="card-title">
                            @if(amountDomainValue.getId() == null)
                                Neuen Gebühren-Satz erstellen
                            @else
                                Gebühren-Satz bearbeiten
                            @endif
                        </h4>
                        <p class="card-text text-secondary">
                            Gebühren-Satz für eine bestimmte Kategorie festlegen. Typ (Code) wählen und den Betrag eingeben.<br/>
                            Der Gebühren-Satz wird ab dem angegebenen Datum gültig sein.
                            <ul>
                                <li>Der eingebene Wert ist für das aktuelle Jahr nicht mehr änderbar!</li>
                                <li>Das Gültig-ab-Datum wird automatisch basierend auf bestehenden Sätzen berechnet.</li>
                            </ul>

                            <strong>Wichtiger Hinweis</strong>
                            <ul>
                                <li>Der Betrag für Zeitschecks ist <strong>pro Stunde</strong> anzugeben!</li>
                                <li>Der Betrag für den Mitgliedsbeitrag ist <strong>pro Jahr</strong> anzugeben!</li>
                            </ul>
                        </p>
                        <hr/>

                        <form method="POST" action="/domaintypes/save">
                            ${csrfHiddenInput}

                            @if(amountDomainValue.getId() != null)
                                <input type="hidden" name="id" value="${amountDomainValue.getId()}">
                                <input type="hidden" name="version" value="${amountDomainValue.getVersion()}">
                            @endif

                            <!-- Code / AmountDomainType -->
                            <div class="mb-3">
                                <label for="code" class="form-label">Typ (Code)</label>
                                @if(amountDomainValue.getId() == null)
                                    <select name="code" id="code" class="form-select" required onchange="updateValidFrom(this.value)">
                                        <option value="">-- Bitte wählen --</option>
                                        @for(AmountDomainType t : amountDomainTypes)
                                            @if(t.name().equals(amountDomainValue.getCode()))
                                                <option value="${t.name()}" selected>${t.getDescription()}</option>
                                            @else
                                                <option value="${t.name()}">${t.getDescription()}</option>
                                            @endif
                                        @endfor
                                    </select>
                                @else
                                    <input type="text" class="form-control" value="${AmountDomainType.valueOf(amountDomainValue.getCode()).getDescription()}" readonly>
                                    <input type="hidden" name="code" value="${amountDomainValue.getCode()}">
                                @endif
                            </div>

                            <!-- Betrag -->
                            <div class="mb-3">
                                <label for="amount" class="form-label">Betrag (€)</label>
                                <input type="number" step="0.01" min="0" name="amount" id="amount"
                                       class="form-control"
                                       value="${amountDomainValue.getAmount() != null ? amountDomainValue.getAmount().toPlainString() : ""}"
                                       required>
                            </div>

                            <!-- Gültig ab -->
                            <div class="mb-3">
                                <label for="validFrom" class="form-label">Gültig immer ab 1. Jänner</label>
                                @if(amountDomainValue.getId() == null)
                                    <input type="text" id="validFromDisplay" class="form-control"
                                           value="${computedValidFrom != null ? computedValidFrom.toString() : ""}" readonly>
                                    <input type="hidden" id="validFromHidden" name="validFrom"
                                           value="${computedValidFrom != null ? computedValidFrom.toString() : ""}">
                                    @if(validFromByCode != null)
                                    <script>
                                        var validFromByCode = {
                                            @for(Map.Entry<String,String> entry : validFromByCode.entrySet())
                                                "${entry.getKey()}": "${entry.getValue()}",
                                            @endfor
                                        };
                                        function updateValidFrom(code) {
                                            var date = validFromByCode[code] || "";
                                            document.getElementById("validFromDisplay").value = date;
                                            document.getElementById("validFromHidden").value = date;
                                        }
                                    </script>
                                    @endif
                                @else
                                    <input type="text" class="form-control" value="${amountDomainValue.getValidFrom().toString()}" readonly>
                                    <input type="hidden" name="validFrom" value="${amountDomainValue.getValidFrom().toString()}">
                                @endif
                            </div>

                            @if(amountDomainValue.getId() != null)
                                <input type="hidden" name="validTo" value="${amountDomainValue.getValidTo().toString()}">
                            @endif

                            <div class="d-flex gap-2 mt-3">
                                <button type="submit" class="btn btn-primary">Speichern</button>
                                <a href="/domaintypes" class="btn btn-secondary">Abbrechen</a>
                            </div>

                        </form>

                        <br/>

                        @template.components.display-statusmessage(successMessage, errorMessage)


                    </div>
                </div>
            </div>
        </div>
    </div>

`)