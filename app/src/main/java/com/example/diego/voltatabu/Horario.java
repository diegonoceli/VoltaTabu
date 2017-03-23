package com.example.diego.voltatabu;

/**
 * Created by diego on 28/12/2016.
 */

public class Horario {
    private String id;
    private String horario;

    public Horario(){

    }
    public Horario(String id,String horario){
        this.id=id;
        this.horario=horario;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }
}
