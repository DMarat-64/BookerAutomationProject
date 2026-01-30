package core.models;

public class CreatedBooking {
    public String firstname;
    public String lastname;
    public int totalprice;
    public boolean depositpaid;
    public Bookingdates bookingdates;
    public String additionalneeds;

    public CreatedBooking() {}

    public CreatedBooking(String firstname, String lastname, int totalprice, boolean depositpaid,
                          Bookingdates bookingdates, String additionalneeds) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.totalprice = totalprice;
        this.depositpaid = depositpaid;
        this.bookingdates = bookingdates;
        this.additionalneeds = additionalneeds;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(int totalprice) {
        this.totalprice = totalprice;
    }

    public boolean isDepositpaid() {
        return depositpaid;
    }

    public void setDepositpaid(boolean depositpaid) {
        this.depositpaid = depositpaid;
    }

    public String getAdditionalneeds() {
        return additionalneeds;
    }

    public void setAdditionalneeds(String additionalneeds) {
        this.additionalneeds = additionalneeds;
    }

    public Bookingdates getBookingdates() {
        return bookingdates;
    }

    public void setBookingdates(Bookingdates bookingdates) {
        this.bookingdates = bookingdates;
    }

    public class Bookingdates{
        public String checkin;
        public String checkout;

        public String getCheckin() {
            return checkin;
        }

        public void setCheckin(String checkin) {
            this.checkin = checkin;
        }

        public String getCheckout() {
            return checkout;
        }

        public void setCheckout(String checkout) {
            this.checkout = checkout;
        }
    }
}