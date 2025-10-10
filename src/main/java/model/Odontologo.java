package model;

public class Odontologo {
    private Integer id;
    private String nombre, apellido, matricula, requisitosTurnos;

    public Odontologo(Integer id, String nombre, String apellido, String matricula, String requisitosTurnos) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.matricula = matricula;
        this.requisitosTurnos = requisitosTurnos;
    }

    public Odontologo(String nombre, String apellido, String matricula, String requisitosTurnos) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.matricula = matricula;
        this.requisitosTurnos = requisitosTurnos;
    }

    public Odontologo(String nombre, String apellido, String matricula) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.matricula = matricula;
    }

    public Odontologo() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getRequisitosTurnos() {
        return requisitosTurnos;
    }

    public void setRequisitosTurnos(String requisitosTurnos) {
        this.requisitosTurnos = requisitosTurnos;
    }
}

