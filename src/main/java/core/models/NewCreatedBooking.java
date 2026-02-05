package core.models;

public class NewCreatedBooking {
    private int bookingid;
    private CreateBooking booking;

    public int getBookingid() {
        return bookingid;
    }

    public void setBookingid(int bookingid) {
        this.bookingid = bookingid;
    }

    public CreateBooking getBooking() {
        return booking;
    }

    public void setBooking(CreateBooking booking) {
        this.booking = booking;
    }
}
