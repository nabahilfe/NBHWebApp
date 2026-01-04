package eu.nabahilfe.webapp.members;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberSearchController {

    private final MemberRepository memberRepository;

    public MemberSearchController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @GetMapping("/search")
    public List<MemberDTO> search(@RequestParam String q) {
        return memberRepository
            .findAllByLastNameContainingIgnoreCaseOrFirstNameContainingIgnoreCase(q, q)
            .stream()
            .map(m -> new MemberDTO(m.getId(), m.getNameAndAddress()))
            .toList();
    }

    record MemberDTO(Long id, String nameAndAdress) {

    }
}
