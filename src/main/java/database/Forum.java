package database;

/**
 * Created by Mike on 06.04.14.
 */
public class Forum {
    String name;
    String short_name;
    Long user;
    Long id;

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public Forum(Long id, String name, String short_name, Long user) {
        this.name = name;
        this.short_name = short_name;
        this.user = user;
        this.id = id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
