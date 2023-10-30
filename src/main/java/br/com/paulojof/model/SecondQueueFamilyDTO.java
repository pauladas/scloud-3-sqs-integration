package br.com.paulojof.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SecondQueueFamilyDTO {

    private String familyPersonName;

    private Integer familyPersonAge;

    private String familyPersonCar;

    private List<String> familyPersonNicknames;

    public String toString() {
        return JsonParser.objectToStringJson(this);
    }

    public static SecondQueueFamilyDTO valueOf(FirstQueueFamilyDTO firstQueueFamilyDTO) {
        return builder()
                .familyPersonName(firstQueueFamilyDTO.getName())
                .familyPersonAge(firstQueueFamilyDTO.getAge())
                .familyPersonCar(firstQueueFamilyDTO.getCar())
                .familyPersonNicknames(firstQueueFamilyDTO.getNicknames())
                .build();
    }
}
