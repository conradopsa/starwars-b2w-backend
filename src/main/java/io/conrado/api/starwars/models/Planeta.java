package io.conrado.api.starwars.models;

import lombok.Data;

@Data
public class Planeta {    
    private Integer idPlaneta;
    private String nome;
    private String clima;
    private String terreno;
}