package model;

public class Guest {

    private int id;
    private String  name;
    private String email;


    //-------------------------------------------------------------------------------------------
    //---------Constructors----------------------------------------------------------------------


    public Guest(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public Guest(String name, String email) {
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

    public String getEmail() {
        return email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
