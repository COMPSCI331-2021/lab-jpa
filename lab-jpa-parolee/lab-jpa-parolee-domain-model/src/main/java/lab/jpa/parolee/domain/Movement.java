package lab.jpa.parolee.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lab.jpa.parolee.jackson.LocalDateTimeDeserializer;
import lab.jpa.parolee.jackson.LocalDateTimeSerializer;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class to represent a Parolee's movement. A Movement instance stores a
 * timestamp and a latitude/longitude position. Movement objects are immutable.
 */
public class Movement implements Comparable<Movement> {

    private LocalDateTime timestamp;

    private GeoPosition geoPosition;

    protected Movement() {
    }

    /**
     * Creates a new {@link Movement}. Demonstrates how we can tell Jackson to use this constructor rather than any
     * setters when creating new instances. We need this here because Movement objects are immutable (and therefore
     * have no setters).
     *
     * @param timestamp the time of the movement
     * @param position  the position of the movement
     */
    @JsonCreator
    public Movement(@JsonProperty("timestamp") LocalDateTime timestamp, @JsonProperty("position") GeoPosition position) {
        this.timestamp = timestamp;
        geoPosition = position;
    }

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public GeoPosition getGeoPosition() {
        return geoPosition;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Movement))
            return false;
        if (obj == this)
            return true;

        Movement rhs = (Movement) obj;
        return new EqualsBuilder().
                append(timestamp, rhs.timestamp).
                append(geoPosition, rhs.geoPosition).
                isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).
                append(timestamp).
                append(geoPosition).
                toHashCode();
    }

    @Override
    public int compareTo(Movement movement) {
        return timestamp.compareTo(movement.timestamp);
    }

    @Override
    public String toString() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        StringBuffer buffer = new StringBuffer();

        buffer.append(geoPosition);
        buffer.append(" @ ");
        buffer.append(timeFormatter.format(timestamp));
        buffer.append(" on ");
        buffer.append(dateFormatter.format(timestamp));

        return buffer.toString();
    }
}