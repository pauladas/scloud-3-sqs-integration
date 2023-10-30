package br.com.paulojof.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ThirdQueuePersonDTO {

    private String name;

    private Integer age;

    private String car;

    public String toString() {
        return JsonParser.objectToStringJson(this);
    }

    public static ThirdQueuePersonDTO valueOf(SecondQueueDTO secondQueueDTO) {
        return builder()
                .name(secondQueueDTO.getPersonName())
                .age(secondQueueDTO.getPersonAge())
                .car(secondQueueDTO.getPersonCar())
                .build();
    }

}
