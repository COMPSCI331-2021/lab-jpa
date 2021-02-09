package lab.jpa.parolee.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lab.jpa.parolee.jackson.LocalDateDeserializer;
import lab.jpa.parolee.jackson.LocalDateSerializer;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Class to represent a particular criminal conviction. A conviction is made up
 * of one or more CriminalProfile.Offence tags, the date of conviction, and a
 * description of the conviction. Convictions are immutable.
 */
public class Conviction {

    private Set<Offence> offenceTags;

    private LocalDate date;

    private String description;

    public Conviction() {
        this(null, null);
    }

    @JsonCreator
    public Conviction(@JsonProperty("date") LocalDate convictionDate,
                      @JsonProperty("description") String description,
                      @JsonProperty("offenceTags") Offence... offenceTags) {
        date = convictionDate;
        this.description = description;
        this.offenceTags = new HashSet<>(Arrays.asList(offenceTags));
    }

    public Set<Offence> getOffenceTags() {
        return Collections.unmodifiableSet(offenceTags);
    }

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Conviction))
            return false;
        if (obj == this)
            return true;

        Conviction rhs = (Conviction) obj;
        return new EqualsBuilder().
                append(date, rhs.date).
                append(description, rhs.description).
                append(offenceTags, rhs.offenceTags).
                isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).
                append(date).
                append(description).
                append(offenceTags).
                toHashCode();
    }
}
