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

<div class="card">
    <div class="card-header bg-success bg-opacity-10">
        <strong>Mitglieder im Vorstand</strong>
    </div>
    <div class="card-body p-0 pb-3">
        @if(boardMembers == null || boardMembers.isEmpty())
            <div class="p-3 text-muted">Keine Vorstandsmitglieder vorhanden.</div>
        @else
            <div class="table-responsive px-3">
                <table class="table table-hover table-sm mb-0">
                    <thead>
                        <tr>
                            <th scope="col">Name</th>
                            <th scope="col">Funktion</th>
                        </tr>
                    </thead>

                    <tbody class="table-group-divider">
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
