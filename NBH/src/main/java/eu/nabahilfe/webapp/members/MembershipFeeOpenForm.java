package eu.nabahilfe.webapp.members;

public class MembershipFeeOpenForm {

    /** Constructor for JPQL constructor expression. doNotCharge defaults to false. */
    public MembershipFeeOpenForm(Long memberId, String firstName, String lastName,
            String street, String number, String zip, String city) {
        this.memberId = memberId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.number = number;
        this.zip = zip;
        this.city = city;
        this.doNotCharge = false;
    }

    public MembershipFeeOpenForm() {}

    private Long memberId;

    private String firstName;
    private String lastName;

    private String street;
    private String number;
    private String zip;
    private String city;

    private boolean doNotCharge;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isDoNotCharge() {
        return doNotCharge;
    }

    public void setDoNotCharge(boolean doNotCharge) {
        this.doNotCharge = doNotCharge;
    }


    public String getName() {
        return lastName + " " + firstName;
    }

    public String getAddress() {
        return street + " " + number + ", " + zip + " " + city;
    }

}
