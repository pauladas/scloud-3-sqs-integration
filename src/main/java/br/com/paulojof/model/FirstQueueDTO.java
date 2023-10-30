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
public class FirstQueueDTO {

    private String name;

    private Integer age;

    private String car;

    private List<FirstQueueFamilyDTO> family;

    public String toString() {
        return JsonParser.objectToStringJson(this);
    }

}
