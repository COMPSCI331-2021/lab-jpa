package lab.jpa.parolee.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Class to represent a geographic location in terms of latitude and longitude.
 * GeoPosition objects are immutable.
 */
public class GeoPosition {

    private double latitude;

    private double longitude;

    protected GeoPosition() {
    }

    /**
     * Creates a new {@link GeoPosition}. Demonstrates how we can tell Jackson to use this constructor rather than any
     * setters when creating new instances. We need this here because GeoPosition objects are immutable (and therefore
     * have no setters).
     *
     * @param lat the latitude
     * @param lng the longitude
     */
    @JsonCreator
    public GeoPosition(@JsonProperty("latitude") double lat, @JsonProperty("longitude") double lng) {
        latitude = lat;
        longitude = lng;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GeoPosition))
            return false;
        if (obj == this)
            return true;

        GeoPosition rhs = (GeoPosition) obj;
        return new EqualsBuilder().
                append(latitude, rhs.latitude).
                append(longitude, rhs.longitude).
                isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).
                append(latitude).
                append(longitude).
                toHashCode();
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append("(");
        buffer.append(latitude);
        buffer.append(",");
        buffer.append(longitude);
        buffer.append(")");

        return buffer.toString();
    }
}
