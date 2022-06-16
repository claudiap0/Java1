package com.company;

public class Apartament {
    private int nrApartament;
    private int suprafata;
    private int nrPersoane;

    public Apartament() {
    }

    public Apartament(int nrApartament, int suprafata, int nrPersoane) {
        this.nrApartament = nrApartament;
        this.suprafata = suprafata;
        this.nrPersoane = nrPersoane;
    }

    public int getNrApartament() {
        return nrApartament;
    }

    public void setNrApartament(int nrApartament) {
        this.nrApartament = nrApartament;
    }

    public int getSuprafata() {
        return suprafata;
    }

    public void setSuprafata(int suprafata) {
        this.suprafata = suprafata;
    }

    public int getNrPersoane() {
        return nrPersoane;
    }

    public void setNrPersoane(int nrPersoane) {
        this.nrPersoane = nrPersoane;
    }

    @Override
    public String toString() {
        return "Apartament{" +
                "nrApartament=" + nrApartament +
                ", suprafata=" + suprafata +
                ", nrPersoane=" + nrPersoane +
                '}';
    }
}
