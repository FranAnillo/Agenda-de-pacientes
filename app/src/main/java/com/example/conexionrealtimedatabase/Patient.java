package com.example.conexionrealtimedatabase;

public final class Patient {
    private String nombre;
    private String apellidos;
    private String grupoSanguineo;
    private String nuhsa;
    public Patient() {}
    public Patient(String nombre,String apellidos,String grupoSanguineo,String nuhsa){this.nombre=nombre;this.apellidos=apellidos;this.grupoSanguineo=grupoSanguineo;this.nuhsa=nuhsa;}
    public String getNombre(){return nombre;}
    public String getApellidos(){return apellidos;}
    public String getGrupoSanguineo(){return grupoSanguineo;}
    public String getNuhsa(){return nuhsa;}
}
