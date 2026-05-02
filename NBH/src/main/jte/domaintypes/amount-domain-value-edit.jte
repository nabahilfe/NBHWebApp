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

@template.layout.mainpage(
    csrfHiddenInput = csrfHiddenInput,
    ctxt = ctxt,
    content = @`

    <div class="container d-flex justify-content-center">
        <div class="row">
            <div class="col-auto">
                <div class="card shadow" style="min-width: 400px;">
                    <div class="card-body">

                        @template.components.display-statusmessage(successMessage, errorMessage)

                        <h4 class="card-title text-primary">
                            @if(amountDomainValue.getId() == null)
                                Neuen Domain-Wert erstellen
                            @else
                                Domain-Wert bearbeiten
                            @endif
                        </h4>
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
                                                <option value="${t.name()}" selected>${t.name()}</option>
                                            @else
                                                <option value="${t.name()}">${t.name()}</option>
                                            @endif
                                        @endfor
                                    </select>
                                @else
                                    <input type="text" class="form-control" value="${amountDomainValue.getCode()}" readonly>
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

                            <!-- Gültig von -->
                            <div class="mb-3">
                                <label for="validFrom" class="form-label">Gültig von (1. Januar)</label>
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

                            <!-- Gültig bis: fixed for new entries, readonly display -->
                            <div class="mb-3">
                                <label class="form-label">Gültig bis</label>
                                <input type="text" class="form-control"
                                       value="${amountDomainValue.getId() == null ? "9999-12-31" : amountDomainValue.getValidTo().toString()}"
                                       readonly>
                                @if(amountDomainValue.getId() != null)
                                    <input type="hidden" name="validTo" value="${amountDomainValue.getValidTo().toString()}">
                                @endif
                            </div>

                            <div class="d-flex gap-2 mt-3">
                                <button type="submit" class="btn btn-primary">Speichern</button>
                                <a href="/domaintypes" class="btn btn-secondary">Abbrechen</a>
                            </div>

                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

`)