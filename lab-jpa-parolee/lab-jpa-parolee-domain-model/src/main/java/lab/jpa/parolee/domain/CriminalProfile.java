package lab.jpa.parolee.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Class to represent a criminal profile. A profile is essentially a series of
 * convictions.
 */
public class CriminalProfile {

    private Set<Conviction> convictions;

    public CriminalProfile() {
        convictions = new HashSet<>();
    }

    /**
     * Creates a new {@link CriminalProfile} with the given set of pre-existing convictions. Annotated with Jackson
     * creator annotations as the convictions property has no setter.
     *
     * @param convictions
     */
    @JsonCreator
    public CriminalProfile(@JsonProperty("convictions") Set<Conviction> convictions) {
        this.convictions = convictions;
    }

    public void addConviction(Conviction conviction) {
        convictions.add(conviction);
    }

    public Set<Conviction> getConvictions() {
        return Collections.unmodifiableSet(convictions);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CriminalProfile))
            return false;
        if (obj == this)
            return true;

        CriminalProfile rhs = (CriminalProfile) obj;
        return new EqualsBuilder().
                append(convictions, rhs.convictions).
                isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).
                append(convictions).
                toHashCode();
    }

    @Override
    public String toString() {
        Set<Offence> allOffences = new HashSet<>();
        Set<LocalDate> allConvictionDates = new HashSet<>();

        for (Conviction conviction : convictions) {
            for (Offence offence : conviction.getOffenceTags()) {
                allOffences.add(offence);
            }
            allConvictionDates.add(conviction.getDate());
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append("Criminal history: ");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");

        for (LocalDate date : allConvictionDates) {
            buffer.append(formatter.format(date));
            buffer.append(", ");
        }
        buffer.delete(buffer.length() - 2, buffer.length() - 1);

        buffer.append("\n    Offences: ");
        for (Offence offence : allOffences) {
            buffer.append(offence);
            buffer.append(", ");
        }
        buffer.delete(buffer.length() - 2, buffer.length() - 1);

        return buffer.toString();

    }

}
