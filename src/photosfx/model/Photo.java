package photosfx.model;

import java.util.*;

public class Photo {
    private String photoName;
    private List<String[]> tags;
    private Calendar date;

    public Photo(String name, Calendar date) {
        this.photoName = name;
        date.set(Calendar.MILLISECOND, 0);
        this.date = date;
    }

    public String getName() {
        return this.photoName;
    }

    public List<String[]> getTags() {
        return this.tags;
    }

    public Calendar getDate() {
        return this.date;
    }

    public void setTag(String type, String value) {
        String[] tag = new String[] { type, value };
        this.tags.add(tag);
    }

    public void setDate(Calendar date) {
        date.set(Calendar.MILLISECOND, 0);
        this.date = date;
    }

}
