<!--
    Displays a list of board members (Vorstand, Kassier, Schriftführer)
    with their full name and role name.

    Usage:
    @template.components.list-board-members(boardMembers = boardMembers)

    Parameters:
    - boardMembers: List<Member> – result of MemberRepository.findBoardMembers()
-->

@import java.util.List
@import eu.nabahilfe.webapp.members.Member

@param List<Member> boardMembers

<div class="p-3 text-success-emphasis border border-success-subtle rounded-3 shadow p-3 mb-5">

    <h2>Vertretungsbefugte Organe</h2>

    <div class="card-text">

        @if(boardMembers == null || boardMembers.isEmpty())
            <div class="p-3 text-muted">Keine Vorstandsmitglieder vorhanden.</div>
        @else
            <div class="table-responsive">
                <table class="table table-hover table-sm mb-0">
                    <thead>
                        <tr>
                            <th scope="col">Name</th>
                            <th scope="col">Funktion</th>
                        </tr>
                    </thead>

                    <tbody class="table-group-divider" style="background-color: #ECFEFF;">
                        @for(Member m : boardMembers)
                            <tr>
                                <td>${m.getName()}</td>
                                <td>${m.getRole() != null ? m.getRole().getRoleName() : ""}</td>
                            </tr>
                        @endfor
                    </tbody>
                </table>
            </div>
        @endif

    </div>

</div>
