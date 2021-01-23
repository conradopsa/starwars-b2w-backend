package io.conrado.api.starwars.pojo;
import lombok.Data;

@Data
public class RequestPlaneta {
    private String nome;
    private String clima;
    private String terreno;
}
