@import java.util.List

@import static eu.nabahilfe.webapp.DateFormatter.*
@import static eu.nabahilfe.webapp.NumberFormatter.*

@import eu.nabahilfe.webapp.domaintypes.AmountDomainValue
@import eu.nabahilfe.webapp.domaintypes.AmountDomainType

@param List<AmountDomainValue> amountDomainValues
@param AmountDomainType[] amountDomainTypes

@param String selectedType = null

@param java.lang.String successMessage = null
@param java.lang.String errorMessage = null

@param eu.nabahilfe.webapp.security.CsrfHiddenInput csrfHiddenInput
@param eu.nabahilfe.webapp.security.ViewContext ctxt


@template.layout.mainpage(
    csrfHiddenInput = csrfHiddenInput,
    ctxt = ctxt,
    content = @`

    <h3>Fachliche Werte (Domain Values)</h3>

    <br/>

    @template.components.display-statusmessage(successMessage, errorMessage)

    <!-- Filter by AmountDomainType -->
    <form method="GET" action="/domaintypes" class="row g-3 align-items-center mb-3">
        <div class="col-auto">
            <label for="typeFilter" class="col-form-label">Filter nach Typ:</label>
        </div>
        <div class="col-auto">
            <select name="type" id="typeFilter" class="form-select" onchange="this.form.submit()">
                <option value="">-- Alle --</option>
                @for(AmountDomainType t : amountDomainTypes)
                    @if(t.name().equals(selectedType))
                        <option value="${t.name()}" selected>${t.name()}</option>
                    @else
                        <option value="${t.name()}">${t.name()}</option>
                    @endif
                @endfor
            </select>
        </div>
    </form>

    <!-- Table -->
    <table class="table table-hover table-sm">
        <thead>
            <tr>
                <th scope="col">Code</th>
                <th scope="col" class="text-end">Betrag</th>
                <th scope="col">Gültig von</th>
                <th scope="col">Gültig bis</th>
                <th scope="col" class="text-center">Aktionen</th>
            </tr>
        </thead>
        <tbody>
            @for(AmountDomainValue adv : amountDomainValues)
                <tr>
                    <td>${adv.getCode()}</td>
                    <td class="text-end">${numberDE(adv.getAmount())} €</td>
                    <td>${dateDE(adv.getValidFrom())}</td>
                    <td>${dateDE(adv.getValidTo())}</td>
                    <td class="text-center">
                        <a href="/domaintypes/edit/${adv.getId()}" class="btn btn-sm btn-outline-primary bi-pencil"></a>
                        <form method="POST" action="/domaintypes/delete/${adv.getId()}" class="d-inline">
                            ${csrfHiddenInput}
                            <button type="submit" class="btn btn-sm btn-outline-danger bi-trash"
                                    onclick="return confirm('Wirklich löschen?')"></button>
                        </form>
                    </td>
                </tr>
            @endfor
        </tbody>
    </table>

    <a href="/domaintypes/create" class="btn btn-primary bi-plus-square"> Neuen Eintrag erstellen</a>

`)
