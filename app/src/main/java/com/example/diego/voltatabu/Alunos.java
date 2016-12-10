package com.example.diego.voltatabu;

import android.net.Uri;

/**
 * Created by diego on 09/12/2016.
 */

public class Alunos {
    private String nome;
    private String horario;
    private String uri;


    public Alunos(){

    }
    public Alunos(String nome,String matricula,String uri){
        this.setHorario(matricula);
        this.setNome(nome);
        this.setUri(uri);

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
