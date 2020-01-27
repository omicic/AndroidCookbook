package com.example.androidcookbook.object;

public class Directions {

    private long id;
    private String nazivSastojka;
    private int kolicina;
    private String vremePripreme;

    public Directions(long id, String nazivSastojka, int kolicina,
                      String vremePripreme) {
        super();
        this.id = id;
        this.nazivSastojka = nazivSastojka;
        this.kolicina = kolicina;
        this.vremePripreme = vremePripreme;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNazivSastojka() {
        return nazivSastojka;
    }

    public void setNazivSastojka(String nazivSastojka) {
        this.nazivSastojka = nazivSastojka;
    }

    public int getKolicina() {
        return kolicina;
    }

    public void setKolicina(int kolicina) {
        this.kolicina = kolicina;
    }

    public String getVremePripreme() {
        return vremePripreme;
    }

    public void setVremePripreme(String vremePripreme) {
        this.vremePripreme = vremePripreme;
    }


}
