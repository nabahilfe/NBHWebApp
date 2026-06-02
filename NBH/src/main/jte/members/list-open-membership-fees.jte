<!--
  Copyright (c) 2025–2026 Maximilian Weißböck
  Licensed under the MIT License (see LICENSE file).
-->

@import java.util.List

@import eu.nabahilfe.webapp.members.MembershipFeeOpenForm

@param MembershipFeeOpenForm formRowData
@param List<MembershipFeeOpenForm> membershipFeesOpen

@param java.lang.String successMessage = null
@param java.lang.String errorMessage = null

@param String fullUsername = null
@param String username = null
@param Boolean isAuthenticated = false


@param eu.nabahilfe.webapp.security.CsrfHiddenInput csrfHiddenInput
@param eu.nabahilfe.webapp.security.ViewContext ctxt


@template.layout.mainpage(
    csrfHiddenInput = csrfHiddenInput,
    ctxt = ctxt,
    content = @`

    <h3>Noch nicht verbuchte Mitgliedsbeiträge für das aktuelle Jahr</h3>

    <br/>

    <!-- Display all MembershipFee records -->

    <table class="table table-hover table-sm" id="memberTable">
        <thead>
            <tr>
                <th scope="col">Name</th>
                <th scope="col">Adresse</th>
                <th scope="col" class="text-center">Beitrag erstellen</th>
            </tr>
        </thead>

        <tbody class="table-group-divider">
            @for (MembershipFeeOpenForm mf : membershipFeesOpen)
                <tr>
                    <td>${mf.getName()}</td>
                    <td>${mf.getAddress()}</td>
                    <td class="text-center">
                        <a class="btn btn-outline-primary btn-sm bi-plus-lg"
                            href="/membership-fees/new?memberId=${mf.getMemberId()}" role="button"
                            title="Mitgliedsbeitrag erstellen"></a>
                    </td>
                </tr>
            @endfor
        </tbody>
    </table>
    <br/>

    @template.components.display-statusmessage(successMessage, errorMessage)

`)