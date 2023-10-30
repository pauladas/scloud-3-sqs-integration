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
public class FirstQueueFamilyDTO {

    private String name;

    private Integer age;

    private String car;

    private List<String> nicknames;

    public String toString() {
        return JsonParser.objectToStringJson(this);
    }

}
