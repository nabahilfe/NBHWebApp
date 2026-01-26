package eu.nabahilfe.webapp.members;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


/**
 * Converts a String (typically an id value from a path variable or form field)
 * into a Member entity by loading it from the MemberRepository.
 *
 * This allows Spring's data binding / ConversionService to resolve controller
 * parameters or form properties declared as `Member` when the incoming value
 * is a String id.
 */

// TODO: check is this used anywhere?

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