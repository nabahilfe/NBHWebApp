package eu.nabahilfe.webapp.jte;

import eu.nabahilfe.webapp.members.Member;

import gg.jte.Content;
import gg.jte.TemplateOutput;


public class TemplateHelpers {

    static public Content boldMemberNameIfSameId(Long memberId, Member member) {
        if (memberId != null && memberId.equals(member.getId())) {
            return new Content() {
                @Override
                public void writeTo(TemplateOutput output) {
                    output.writeContent("<strong>");
                    output.writeUserContent(member.getName());
                    output.writeContent("</strong>");
                }
            };
        }
        return output -> output.writeUserContent(member.getName());
    }
}
