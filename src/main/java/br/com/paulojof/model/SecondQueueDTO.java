package br.com.paulojof.model;

import java.util.List;

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
public class SecondQueueDTO {

    private String personName;

    private Integer personAge;

    private String personCar;

    private List<SecondQueueFamilyDTO> personsFamily;

    public String toString() {
        return JsonParser.objectToStringJson(this);
    }

    public static SecondQueueDTO valueOf(FirstQueueDTO dto) {
        List<SecondQueueFamilyDTO> familyDTOList = dto.getFamily().stream()
                .map(SecondQueueFamilyDTO::valueOf)
                .toList();
        return builder()
                .personName(dto.getName())
                .personAge(dto.getAge())
                .personCar(dto.getCar())
                .personsFamily(familyDTOList)
                .build();
    }

}
