package br.ufc.quixada.backontrack.model;

/**
 * Created by samue on 17/07/2017.
 */

public class Atividade {
    private Integer id;
    private int nivel;
    private String nome;
    private String descricao;
    private int duracao;
    private int thumbnail;

    public Atividade(){

    }

    public Atividade(int nivel, String nome, int duracao, String descricao, int thumbnail){
        this.nivel = nivel;
        this.nome = nome;
        this.descricao = descricao;
        this.duracao = duracao;
        this.thumbnail = thumbnail;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getNivel() {
        return nivel;
    }
    public void setNivel(int nivel) {
        this.nivel = nivel;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getDuracao() {
        return duracao;
    }

    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
