package io.conrado.api.starwars.pojo;
import io.conrado.api.starwars.models.Planeta;
import lombok.Getter;
import lombok.Setter;


public class ResponsePlaneta extends Planeta {
    @Getter @Setter
    private Integer quantidadeFilmes;
}
