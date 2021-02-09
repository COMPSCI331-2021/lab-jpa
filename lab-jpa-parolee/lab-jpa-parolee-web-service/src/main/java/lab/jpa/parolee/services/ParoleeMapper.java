package lab.jpa.parolee.services;

import lab.jpa.parolee.domain.Parolee;

/**
 * Helper class to convert between domain-model and DTO objects representing
 * Parolees.
 */
public class ParoleeMapper {

    static Parolee toDomainModel(lab.jpa.parolee.dto.Parolee dtoParolee) {
        Parolee fullParolee = new Parolee(dtoParolee.getId(),
                dtoParolee.getLastName(),
                dtoParolee.getFirstName(),
                dtoParolee.getGender(),
                dtoParolee.getDateOfBirth(),
                dtoParolee.getHomeAddress(),
                dtoParolee.getCurfew());
        return fullParolee;
    }

    static lab.jpa.parolee.dto.Parolee toDto(Parolee parolee) {
        lab.jpa.parolee.dto.Parolee dtoParolee =
                new lab.jpa.parolee.dto.Parolee(
                        parolee.getId(),
                        parolee.getLastName(),
                        parolee.getFirstName(),
                        parolee.getGender(),
                        parolee.getDateOfBirth(),
                        parolee.getHomeAddress(),
                        parolee.getCurfew(),
                        parolee.getLastKnownPosition());
        return dtoParolee;

    }
}
