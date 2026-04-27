package model;

import java.time.LocalDate;

public class Reservation {


    private int id;
    private int id_room;
    private int id_guest;
    private int total_nights;
    private LocalDate day_in;
    private LocalDate day_out;
    private boolean check_in = false;
    private boolean check_out = false;


    //-------------------------------------------------------------------------------------------
    //---------Constructors----------------------------------------------------------------------


    public Reservation(int id, int id_room, int id_guest, int total_nights, LocalDate day_in, LocalDate day_out) {
        this.id = id;
        this.id_room = id_room;
        this.id_guest = id_guest;
        this.total_nights = total_nights;
        this.day_in = day_in;
        this.day_out = day_out;
    }

    public Reservation(int id_room, int id_guest, int total_nights, LocalDate day_in, LocalDate day_out) {
        this.id_room = id_room;
        this.id_guest = id_guest;
        this.total_nights = total_nights;
        this.day_in = day_in;
        this.day_out = day_out;
    }

    public Reservation(){};


    //-------------------------------------------------------------------------------------------
    //---------Setters && Getters----------------------------------------------------------------


    public int getId() {
        return id;
    }

    public int getId_room() {
        return id_room;
    }

    public int getId_guest() {
        return id_guest;
    }

    public int getTotal_nights() {
        return total_nights;
    }

    public LocalDate getDay_in() {
        return day_in;
    }

    public LocalDate getDay_out() {
        return day_out;
    }

    public boolean isCheck_in() {
        return check_in;
    }

    public boolean isCheck_out() {
        return check_out;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setId_room(int id_room) {
        this.id_room = id_room;
    }

    public void setId_guest(int id_guest) {
        this.id_guest = id_guest;
    }

    public void setTotal_nights(int total_nights) {
        this.total_nights = total_nights;
    }

    public void setDay_in(LocalDate day_in) {
        this.day_in = day_in;
    }

    public void setDay_out(LocalDate day_out) {
        this.day_out = day_out;
    }

    public void setCheck_in(boolean check_in) {
        this.check_in = check_in;
    }

    public void setCheck_out(boolean check_out) {
        this.check_out = check_out;
    }


}
