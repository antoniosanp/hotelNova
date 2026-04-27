package model;

public class Guest {

    private int id;
    private boolean isActive = true;
    private String  name;
    private String email;


    //-------------------------------------------------------------------------------------------
    //---------Constructors----------------------------------------------------------------------


    public Guest(int id, boolean isActive, String name, String email) {
        this.id = id;
        this.isActive = isActive;
        this.name = name;
        this.email = email;
    }

    public Guest(int id, String name, String email) {
        this(id, true, name, email);
    }

    public Guest(String name, String email) {
        this(true, name, email);
    }

    public Guest(boolean isActive, String name, String email) {
        this.isActive = isActive;
        this.name = name;
        this.email = email;
    }

    public Guest(){}


    //-------------------------------------------------------------------------------------------
    //---------Setters && Getters----------------------------------------------------------------


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getEmail() {
        return email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
