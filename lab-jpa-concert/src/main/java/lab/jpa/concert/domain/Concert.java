package lab.jpa.concert.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Class to represent a Concert.
 */
public class Concert implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String title;
    private LocalDateTime date;

    public Concert() {
    }

    public Concert(Long id, String title, LocalDateTime date) {
        this.id = id;
        this.title = title;
        this.date = date;
    }

    public Concert(String title, LocalDateTime date) {
        this(null, title, date);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Concert: id ");
        buffer.append(id);
        buffer.append(", title ");
        buffer.append(title);
        buffer.append(", date ");

        String formattedDate = this.date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        buffer.append(formattedDate);

        return buffer.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Concert))
            return false;
        if (other == this)
            return true;

        Concert rhs = (Concert) other;
        return new EqualsBuilder().
                append(id, rhs.getId()).
                append(title, rhs.getTitle()).
                isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).
                append(getClass().getName()).
                append(id).
                append(title).
                toHashCode();
    }
}