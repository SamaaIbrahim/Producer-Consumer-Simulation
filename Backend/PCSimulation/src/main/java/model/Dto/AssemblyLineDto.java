package model.Dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.Impl.AssemblyLine;

import java.util.concurrent.LinkedBlockingDeque;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssemblyLineDto {
    private String id;

    @JsonIgnore
    public AssemblyLine getAssemblyLine() {
        return AssemblyLine.builder()
                .id(this.id)
                .queue(new LinkedBlockingDeque<>())
                .build();
    }
}
