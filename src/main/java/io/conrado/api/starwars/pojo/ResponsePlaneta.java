package io.conrado.api.starwars.pojo;
import io.conrado.api.starwars.models.Planeta;
import lombok.Data;

@Data
public class ResponsePlaneta extends Planeta {
    private Integer quantidadeFilmes;
}
