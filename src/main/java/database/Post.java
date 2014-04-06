package database;

/**
 * Created by Mike on 05.04.14.
 */
public class Post {
    boolean isApproved,isHighlighted,isEdited,isSpam,isDeleted;
    Long parent,user,thread,id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    String date, message,forum;

    public void setHighlighted(boolean isHighlighted) {
        this.isHighlighted = isHighlighted;
    }

    public void setApproved(boolean isApproved) {
        this.isApproved = isApproved;
    }

    public void setEdited(boolean isEdited) {
        this.isEdited = isEdited;
    }

    public void setSpam(boolean isSpam) {
        this.isSpam = isSpam;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public void setThread(Long thread) {
        this.thread = thread;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setForum(String forum) {
        this.forum = forum;
    }

    public boolean isHighlighted() {
        return isHighlighted;
    }

    public boolean isEdited() {
        return isEdited;
    }

    public boolean isSpam() {
        return isSpam;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public Long getParent() {
        return parent;
    }

    public Long getUser() {
        return user;
    }

    public Long getThread() {
        return thread;
    }

    public String getMessage() {
        return message;
    }

    public Post(Long id,Long parent, boolean isApproved, boolean isHighlighted, boolean isEdited, boolean isSpam, boolean isDeleted, String date, Long thread, String message, Long user,String forum) {
        this.id = id;
        this.isApproved = isApproved;
        this.isHighlighted = isHighlighted;
        this.isEdited = isEdited;
        this.isSpam = isSpam;
        this.isDeleted = isDeleted;
        this.parent = parent;
        this.user = user;
        this.thread = thread;
        this.message = message;
        this.date = date;
        this.forum = forum;
    }

    public String getDate() {
        return date;
    }

    public String getForum() {
        return forum;
    }

    public boolean isApproved() {

        return isApproved;
    }
}
