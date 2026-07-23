<!--
  Copyright (c) 2025–2026 Maximilian Weißböck
  Licensed under the MIT License (see LICENSE file).
-->

@import eu.nabahilfe.webapp.GlobalModelAttributes
@import java.util.TreeMap

@param TreeMap<Integer, long[]> memberStats

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
        <h3>Mitgliederentwicklung</h3>

        @template.components.display-statusmessage(successMessage, errorMessage)

        @if(memberStats == null || memberStats.isEmpty())
            <div class="alert alert-info">Keine Mitgliederdaten gefunden.</div>
        @else
            <table class="table table-bordered table-hover table-sm shadow h-100">
                <thead class="table-light">
                    <tr>
                        <th scope="col">Jahr</th>
                        <th scope="col" class="text-end">Eintritte</th>
                        <th scope="col" class="text-end">Austritte</th>
                        <th scope="col" class="text-end">Saldo</th>
                    </tr>
                </thead>
                <tbody>
                    !{long cumulative = 0;}
                    @for(java.util.Map.Entry<Integer, long[]> entry : memberStats.entrySet())
                        !{long joined = entry.getValue()[0];}
                        !{long resigned = entry.getValue()[1];}
                        !{long saldo = joined - resigned;}
                        !{cumulative += saldo;}
                        <tr>
                            <td>${entry.getKey()}</td>
                            <td class="text-end text-success">${joined}</td>
                            <td class="text-end text-danger">${resigned}</td>
                            <td class="text-end ${saldo >= 0 ? "text-success" : "text-danger"}">${saldo >= 0 ? "+" : ""}${saldo}</td>
                        </tr>
                    @endfor
                </tbody>
                <tfoot class="table-light fw-bold">
                    <tr>
                        <td>Gesamt</td>
                        <td class="text-end text-success">
                            !{long totalJoined = memberStats.values().stream().mapToLong(v -> v[0]).sum();}
                            ${totalJoined}
                        </td>
                        <td class="text-end text-danger">
                            !{long totalResigned = memberStats.values().stream().mapToLong(v -> v[1]).sum();}
                            ${totalResigned}
                        </td>
                        <td class="text-end">
                            !{long totalSaldo = totalJoined - totalResigned;}
                            <span class="${totalSaldo >= 0 ? "text-success" : "text-danger"}">${totalSaldo >= 0 ? "+" : ""}${totalSaldo}</span>
                        </td>
                    </tr>
                </tfoot>
            </table>
        @endif
    </div>

`)