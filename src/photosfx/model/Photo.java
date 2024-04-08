package photosfx.model;

import java.io.Serializable;
import java.util.*;

public class Photo implements Serializable {
    private String caption;
    private List<Tag> tags;
    private Calendar date;
    private String pathInDisk;

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

    public String getDate() {
        int month = this.date.get(Calendar.MONTH) + 1;
        int day = this.date.get(Calendar.DAY_OF_MONTH);
        int year = this.date.get(Calendar.YEAR);
        int hour = this.date.get(Calendar.HOUR_OF_DAY);
        int minute = this.date.get(Calendar.MINUTE);

        return String.format("%02d/%02d/%d %02d:%02d", month, day, year, hour, minute);
    }

    public Calendar getCalDate() {
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

    public void setPath(String path) {
        this.pathInDisk = path;
    }

    public String getPathInDisk() {
        return this.pathInDisk;
    }

    public void deleteTag(Tag tag) {
        this.tags.remove(tag);
    }

}
