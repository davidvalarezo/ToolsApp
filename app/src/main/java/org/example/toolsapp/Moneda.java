package org.example.toolsapp;

import java.util.ArrayList;

public class Moneda {
    private int id;
    private String name;
    private String code;
    private String country;
    private String signo;
    private double valor;


    public static ArrayList<Moneda> divisas = new ArrayList<>();

    public Moneda(int id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public Moneda(int id, String name, String code, String country, String signo) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.country = country;
        this.signo = signo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSigno() {
        return signo;
    }

    public void setSigno(String signo) {
        this.signo = signo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
