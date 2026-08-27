<!--
  Copyright (c) 2025–2026 Maximilian Weißböck
  Licensed under the MIT License (see LICENSE file).
-->

@import eu.nabahilfe.webapp.GlobalModelAttributes
@import eu.nabahilfe.webapp.members.Member
@import java.util.List

@param List<Member> registeredMembers

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

    <div class="container">
        <h3>Registrierte Mitglieder</h3>
        <p>Diese Mitglieder sind aktuell registriert und können die App verwenden.</p>

        <table class="table table-bordered table-hover table-sm shadow h-100" id="registeredMembersTable">
            <thead class="table-light">
                <tr>
                    <th scope="col">#</th>
                    <th scope="col">Mitglied</th>
                    <th scope="col">E-Mail</th>
                </tr>
            </thead>
            <tbody>
                @for (int i = 0; i < registeredMembers.size(); i++)
                    <tr>
                        <td>${i + 1}</td>
                        <td>${registeredMembers.get(i).getName()}</td>
                        <td>${registeredMembers.get(i).getEmail()}</td>
                    </tr>
                @endfor
            </tbody>
        </table>

    </div>

    @template.components.display-statusmessage(successMessage, errorMessage)

`)