package com.example.diego.voltatabu;

/**
 * Created by diego on 28/12/2016.
 */

public class Cidade {
    private String id_cidade;
    private String nome_cidade;

    public Cidade(){

    }
    public Cidade(String id_cidade,String nome_cidade){
        this.setId_cidade(id_cidade);
        this.setNome_cidade(nome_cidade);
    }

    public String getId_cidade() {
        return id_cidade;
    }

    public void setId_cidade(String id_cidade) {
        this.id_cidade = id_cidade;
    }

    public String getNome_cidade() {
        return nome_cidade;
    }

    public void setNome_cidade(String nome_cidade) {
        this.nome_cidade = nome_cidade;
    }
}
