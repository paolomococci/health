package local.health.care.dto;

import java.time.LocalDate;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @JsonInclude(Include.NON_NULL) tells Jackson to omit any fields that are null
 *                                from the JSON output. This is useful for PATCH
 *                                requests where only a subset of the fields may
 *                                be sent.
 *                                record - a concise, immutable data carrier.
 */
@JsonInclude(Include.NON_NULL)
public record PatientPatchDto(
                // Optional field - patient’s name.
                Optional<String> name,

                // Explicitly name the JSON property Format LocalDate as “yyyy-MM-dd” in JSON
                // optional birthdate.
                @JsonProperty("birthdate") @JsonFormat(pattern = "yyyy-MM-dd") Optional<LocalDate> birthdate,

                // Optional gender.
                Optional<String> gender) {
}