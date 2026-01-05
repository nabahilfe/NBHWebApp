@import java.util.List

@import eu.nabahilfe.webapp.members.Member
@import eu.nabahilfe.webapp.members.Role
@import eu.nabahilfe.webapp.timecheques.TimeTransfer
@import eu.nabahilfe.webapp.timecheques.TimeCheque

@param Member member
@param List<Role> roles
@param List<TimeCheque> purchasedTimeCheques
@param List<TimeTransfer> timeTransfers

@param java.lang.String action

@param java.lang.String successMessage = null
@param java.lang.String errorMessage = null



@template.layout.main(content = @`



    <div class="container">
        <h1>${status}</h1>
        <p>${error}</p>
        #if(message != null && message != "")
            <p>Details: ${message}</p>
        #end
        <p><a href="/">Zurück zur Startseite</a></p>
    </div>



`)



