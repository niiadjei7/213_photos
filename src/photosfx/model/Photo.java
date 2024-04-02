package photosfx.model;

import java.io.Serializable;
import java.util.*;

public class Photo implements Serializable {
    private String caption;
    private List<String[]> tags;
    private Calendar date;

    public Photo(String name, Calendar date) {
        date.set(Calendar.MILLISECOND, 0);
        this.date = date;
        this.caption = "";
        this.tags = new ArrayList<>();
    }

    public String getTags() {
        String tagset = "";
        for (String[] tag : this.tags) {
            tagset += "(" + tag[0] + "," + tag[1] + ")";
            tagset += ", ";
        }
        return tagset;
    }

    public Calendar getDate() {
        return this.date;
    }

    public String getCaption() {
        return this.caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
        return;
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
