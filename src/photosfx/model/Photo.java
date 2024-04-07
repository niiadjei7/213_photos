package photosfx.model;

import java.io.Serializable;
import java.util.*;

public class Photo implements Serializable {
    private String caption;
    private List<Tag> tags;
    private Calendar date;

    public Photo(String name, Calendar date) {
        date.set(Calendar.MILLISECOND, 0);
        this.date = date;
        this.caption = "";
        this.tags = new ArrayList<>();
    }

    public List<Tag> getTags() {
        List<Tag> tagset = new ArrayList<>();
        for (Tag tag : this.tags) {
            tagset.add(tag);
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
        Tag tag = new Tag(type, value);
        this.tags.add(tag);
    }

    public void setDate(Calendar date) {
        date.set(Calendar.MILLISECOND, 0);
        this.date = date;
    }

}
