package database;


public class Thread {
    Long id;

    public Thread(Long id, Long user,Long forum, String title, String date, String message, String slug, Boolean isDeleted, Boolean isClosed) {
        this.id = id;
        this.user = user;
        this.forum = forum;
        this.title = title;
        this.date = date;
        this.message = message;
        this.slug = slug;
        this.isDeleted = isDeleted;
        this.isClosed = isClosed;
    }

    public Long getForum() {
        return forum;
    }

    public void setForum(Long forum) {
        this.forum = forum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Boolean getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(Boolean isClosed) {
        this.isClosed = isClosed;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    Long user;
    String userN;
    Long forum;
    String forumN;

    public Thread(Long id, String userN, String forumN, String date, String title, String message, String slug, Boolean isDeleted, Boolean isClosed) {
        this.id = id;
        this.userN = userN;
        this.forumN = forumN;
        this.date = date;
        this.title = title;
        this.message = message;
        this.slug = slug;
        this.isDeleted = isDeleted;
        this.isClosed = isClosed;
    }

    String title;
    String date;
    String message;
    String slug;
    Boolean isDeleted;
    Boolean isClosed;


    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }
}
