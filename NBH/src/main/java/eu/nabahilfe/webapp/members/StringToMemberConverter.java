package eu.nabahilfe.webapp.members;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class StringToMemberConverter implements Converter<String, Member> {

    private final MemberRepository memberRepository;

    public StringToMemberConverter(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public Member convert(String source) {
        if (source == null || source.isBlank()) {
            return null;
        }
        return memberRepository.findById(Long.valueOf(source)).orElse(null);
    }
}
