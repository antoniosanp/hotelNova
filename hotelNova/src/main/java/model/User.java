package model;

public class User {

    private int id;
    private String email;
    private String passsword;
    private String rol;
    private String name;

    //--------------------------------------------------------------------------------
    //--------------Constructors------------------------------------------------------
    public User(int id, String email, String passsword, String rol, String name) {
        this.id = id;
        this.email = email;
        this.passsword = passsword;
        this.rol = rol;
        this.name = name;
    }

    public User(String email, String passsword, String rol, String name) {
        this.email = email;
        this.passsword = passsword;
        this.rol = rol;
        this.name = name;
    }

    public User(){}

    //---------------------------------------------------------------------------------
    //------Setters && Getters---------------------------------------------------------


    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPasssword() {
        return passsword;
    }

    public String getRol() {
        return rol;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasssword(String passsword) {
        this.passsword = passsword;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public void setName(String name) {
        this.name = name;
    }
}
