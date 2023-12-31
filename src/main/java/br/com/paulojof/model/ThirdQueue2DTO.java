package br.com.paulojof.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ThirdQueue2DTO {

    private Object person;

    private List<String> family;

    public String toString() {
        return JsonParser.objectToStringJson(this);
    }

    public static ThirdQueue2DTO valueOf(SecondQueueDTO secondQueueDTO) {
        ThirdQueuePersonDTO personDTO = ThirdQueuePersonDTO.valueOf(secondQueueDTO);
        List<String> familyStringList = secondQueueDTO.getPersonsFamily().stream()
                .map(ThirdQueueFamilyDTO::valueOf)
                .map(ThirdQueueFamilyDTO::toString)
                .toList();
        return new ThirdQueue2DTO(personDTO, familyStringList);
    }

}
