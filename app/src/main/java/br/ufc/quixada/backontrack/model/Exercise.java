package br.ufc.quixada.backontrack.model;

import java.io.Serializable;

/**
 * Created by samue on 17/07/2017.
 */

public class Exercise implements Serializable{
    private Integer id;
    private int nivel;
    private String nome;
    private String descricao;
    private boolean isUnlocked = true;
    private int videoPath;
/*    private List<Integer> stepsAudio;*/
    //private int duracao;
    private int thumbnail;

    public Exercise(){

    }

    public Exercise(int nivel, String nome,/* int duracao,*/ String descricao, int thumbnail, int videoPath , boolean isUnlocked/*, List<Integer> stepsAudio*/){
        this.nivel = nivel;
        this.nome = nome;
        this.descricao = descricao;
        /*this.stepsAudio = stepsAudio;*/
        this.thumbnail = thumbnail;
        this.videoPath = videoPath;
        this.isUnlocked = isUnlocked;

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

/*    public int getDuracao() {
        return duracao;
    }

    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }*/

    public int getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(int videoPath) {
        this.videoPath = videoPath;
    }

    public boolean isUnlocked() {
        return isUnlocked;
    }

    public void setUnlocked(boolean unlocked) {
        isUnlocked = unlocked;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
