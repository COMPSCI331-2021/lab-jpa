package lab.jpa.parolee.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lab.jpa.parolee.jackson.LocalTimeDeserializer;
import lab.jpa.parolee.jackson.LocalTimeSerializer;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.time.LocalTime;

/**
 * Class to represent a Parolee's curfew. A curfew is described by a confinement
 * address and the period of time that the curfew is in effect (typically
 * overnight).
 */
public class Curfew {

    private Address confinementAddress;

    private LocalTime startTime;

    private LocalTime endTime;

    protected Curfew() {
    }

    public Curfew(Address confinementAddress,
                  LocalTime startTime,
                  LocalTime endTime) {
        this.confinementAddress = confinementAddress;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Address getConfinementAddress() {
        return confinementAddress;
    }

    public void setConfinementAddress(Address confinementAddress) {
        this.confinementAddress = confinementAddress;
    }

    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Curfew))
            return false;
        if (obj == this)
            return true;

        Curfew rhs = (Curfew) obj;
        return new EqualsBuilder().
                append(confinementAddress, rhs.confinementAddress).
                append(startTime, rhs.startTime).
                append(endTime, rhs.endTime).
                isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).
                append(confinementAddress).
                append(startTime).
                append(endTime).
                toHashCode();
    }
}
