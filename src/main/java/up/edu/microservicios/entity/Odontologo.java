package up.edu.microservicios.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "odontologos")
public class Odontologo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
    private String apellido;
    @Column(unique = true, nullable = false)
    private String matricula;
    private String requisitosTurnos;

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
    /*
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

     */
}

