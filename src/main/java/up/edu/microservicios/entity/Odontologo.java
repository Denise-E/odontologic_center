package up.edu.microservicios.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

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

    @OneToMany(mappedBy = "odontologo", fetch = FetchType.LAZY)
    private Set<Turno> turnos = new HashSet<>(); // Set porque son datos únicos no duplicados


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
}

