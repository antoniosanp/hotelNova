package model;

import java.time.LocalDateTime;


public class Room {

    private int id;
    private String room_type;
    private int room_number;
    private int room_capacity;
    private double room_price;
    private String room_state;
    private boolean isActive = true;
    private LocalDateTime createdAt;

    //-------------------------------------------------------------------------------------------
    //---------Constructors----------------------------------------------------------------------

    public Room(int id, String room_type, int room_number, int room_capacity, double room_price, String room_state) {
        this.id = id;
        this.room_type = room_type;
        this.room_number = room_number;
        this.room_capacity = room_capacity;
        this.room_price = room_price;
        this.room_state = room_state;
    }

    public Room(int room_number, String room_type, int room_capacity, double room_price, String room_state) {
        this.room_number = room_number;
        this.room_type = room_type;
        this.room_capacity = room_capacity;
        this.room_price = room_price;
        this.room_state = room_state;
    }

    public Room(){}


    //-------------------------------------------------------------------------------------------
    //---------Setters && Getters----------------------------------------------------------------


    public int getId() {
        return id;
    }

    public int getRoom_number() {
        return room_number;
    }

    public String getRoom_type() {
        return room_type;
    }

    public int getRoom_capacity() {
        return room_capacity;
    }

    public double getRoom_price() {
        return room_price;
    }

    public String getRoom_state() {
        return room_state;
    }

    public boolean isActive() {
        return isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRoom_number(int room_number) {
        this.room_number = room_number;
    }

    public void setRoom_type(String room_type) {
        this.room_type = room_type;
    }

    public void setRoom_capacity(int room_capacity) {
        this.room_capacity = room_capacity;
    }

    public void setRoom_price(double room_price) {
        this.room_price = room_price;
    }

    public void setRoom_state(String room_state) {
        this.room_state = room_state;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
