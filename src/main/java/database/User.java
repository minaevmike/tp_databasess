package database;


public class User {
    private String email;
    private String username;
    private String name;
    private String about;
    private Boolean isAnonymous;
    private Long id;
    public Long getId(){
        return id;
    }
    public void setId(Long sId){
        id = sId;
    }

    public String getEmail(){
        return email;
    }
    public void setEmail(String sEmail){
        email = sEmail;
    }

    public String getUsername(){
        return username;
    }
    public void setUsername(String sUsername){
        username = sUsername;
    }

    public String getName(){
        return name;
    }
    public void setName(String sName){
        name = sName;
    }

    public String getAbout(){
        return about;
    }
    public void setAbout(String sAbout){
        about = sAbout;
    }

    public Boolean getIsAnonymous(){
        return isAnonymous;
    }
    public void setIsAnonymous(Boolean sIsAnonymous){
        isAnonymous = sIsAnonymous;
    }

    public User(Long id, String username, String email, String name, String about, Boolean isAnonymous){
        setAbout(about);
        setEmail(email);
        setIsAnonymous(isAnonymous);
        setId(id);
        setUsername(username);
        setName(name);
    }
}
