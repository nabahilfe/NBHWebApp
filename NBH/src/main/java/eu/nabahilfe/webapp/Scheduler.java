package eu.nabahilfe.webapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import eu.nabahilfe.webapp.email.EmailService;

import eu.nabahilfe.webapp.members.MemberRepository;

@Component
public class Scheduler {
	
	@Autowired EmailService emailService;
	MemberRepository memberRepository;
	
	public Scheduler(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}
	
	@Scheduled(cron = "0 0 2 * * *")
	public void sendTimeChecksToBookEmail() {
		/*
		for(Member member : memberRepository.findAllByRole(Role)) {
			
		}
		emailService.sendTimeChecksToBookEmail();
		*/
	}
	

}
