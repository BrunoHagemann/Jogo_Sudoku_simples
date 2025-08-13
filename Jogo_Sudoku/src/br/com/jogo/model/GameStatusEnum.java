package br.com.jogo.model;

public enum GameStatusEnum {

    non_Started("não iniciado"),
    INCOMPLITE("incompelto"),
    COMPLITE("completo");

    private String label;

    GameStatusEnum(final String label){
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
