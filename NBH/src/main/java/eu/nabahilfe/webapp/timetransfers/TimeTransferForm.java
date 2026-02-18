package eu.nabahilfe.webapp.timetransfers;

import java.time.LocalDate;

public class TimeTransferForm {

    String userFromName;
    Long userFromId;

    Long offerId;

    LocalDate serviceDate;

    String hoursSelected;

    String userToName;
    Long userToId;

    public TimeTransferForm() {
        super();
    }

    public String getUserFromName() {
        return userFromName == null ? "" : userFromName;
    }

    public void setUserFromName(String userFromName) {
        this.userFromName = userFromName;
    }

    public String getUserFromId() {
        return userFromId == null ? "" : userFromId.toString();
    }

    public void setUserFromId(Long userFromId) {
        this.userFromId = userFromId;
    }

    public String getOfferId() {
        return offerId == null ? "" : offerId.toString();
    }

    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }

    public String getUserToName() {
        return userToName == null ? "" : userToName;
    }

    public void setUserToName(String userToName) {
        this.userToName = userToName;
    }

    public String getUserToId() {
        return userToId == null ? "" : userToId.toString();
    }

    public void setUserToId(Long userToId) {
        this.userToId = userToId;
    }

    public LocalDate getServiceDate() {
                return serviceDate;
    }

    public void setServiceDate(LocalDate serviceDate) {
        this.serviceDate = serviceDate;
    }

    public String getHoursSelected() {
        return hoursSelected == null ? "" : hoursSelected;
    }

    public void setHoursSelected(String hoursSelected) {
        this.hoursSelected = hoursSelected;
    }


    @Override
    public String toString() {
        return "TimeTransferForm [userFromName=" + userFromName + ", userFromId=" + userFromId + ", offerId=" + offerId
                + ", serviceDate=" + serviceDate + ", hoursSelected=" + hoursSelected + ", userToName=" + userToName
                + ", userToId=" + userToId + "]";
    }





}
