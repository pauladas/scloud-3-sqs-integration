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
public class ThirdQueueDTO {

    private ThirdQueuePersonDTO person;

    private List<ThirdQueueFamilyDTO> family;

    public String toString() {
        return JsonParser.objectToStringJson(this);
    }

    public static ThirdQueueDTO valueOf(SecondQueueDTO secondQueueDTO) {
        ThirdQueuePersonDTO personDTO = ThirdQueuePersonDTO.valueOf(secondQueueDTO);
        List<ThirdQueueFamilyDTO> familyList = secondQueueDTO.getPersonsFamily().stream()
                .map(ThirdQueueFamilyDTO::valueOf)
                .toList();
        return new ThirdQueueDTO(personDTO, familyList);
    }

}
