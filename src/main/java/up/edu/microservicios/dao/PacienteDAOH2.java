package up.edu.microservicios.dao;

import org.apache.log4j.Logger;
import up.edu.microservicios.model.Domicilio;
import up.edu.microservicios.model.Paciente;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PacienteDAOH2 implements iDao<Paciente>{
    private static final Logger LOGGER= Logger.getLogger(PacienteDAOH2.class);

    private static final String SQL_SELECT_ONE=" SELECT * FROM PACIENTES WHERE ID=?";
    private static final String SQL_SELECT_ALL=" SELECT * FROM PACIENTES";
    private static final String SQL_UPDATE_BY_ID = "UPDATE PACIENTES SET NOMBRE=?, APELLIDO=?, NUMEROCONTACTO=?, FECHAINGRESO=?, DOMICILIO_ID=?, EMAIL=? WHERE ID=?";
    private static final String SQL_DELETE_BY_ID = "DELETE FROM PACIENTES WHERE ID=?";
    private static final String SQL_INSERT = "INSERT INTO PACIENTES(NOMBRE, APELLIDO, NUMEROCONTACTO, FECHAINGRESO, DOMICILIO_ID, EMAIL) VALUES(?,?,?,?,?,?)";


    @Override
    public Paciente guardar(Paciente paciente) {
        Connection connection = null;
        try {
            connection = BD.getConnection();

            PreparedStatement ps = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, paciente.getNombre());
            ps.setString(2, paciente.getApellido());
            ps.setInt(3, paciente.getNumeroContacto());
            ps.setDate(4, Date.valueOf(paciente.getFechaIngreso())); // si tu atributo es LocalDate
            ps.setInt(5, paciente.getDomicilio().getId());
            ps.setString(6, paciente.getEmail());

            ps.executeUpdate();

            // Obtener la clave primaria generada
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                paciente.setId(keys.getInt(1));
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return paciente;
    }

    @Override
    public Paciente buscar(Integer id) {
        Connection connection=null;
        Paciente paciente= null;
        Domicilio domicilio= null;
        try{
            connection=BD.getConnection();
            //statement mundo java a sql
            Statement statement= connection.createStatement();
            PreparedStatement ps_select_one= connection.prepareStatement(SQL_SELECT_ONE);
            ps_select_one.setInt(1,id);

            //ResultSet mundo bdd a java
            ResultSet rs= ps_select_one.executeQuery();
            DomicilioDAOH2 daoAux= new DomicilioDAOH2();
            while(rs.next()){
                domicilio=daoAux.buscar(rs.getInt(6));
                paciente= new Paciente(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getInt(4),rs.getDate(5).toLocalDate(),domicilio,rs.getString(7));
            }
        }catch (Exception e){
            e.getMessage();
        }
        LOGGER.debug("Paciente encontrado: " +paciente);
        return paciente;
    }

    @Override
    public void eliminar(Integer id) {
        Connection connection = null;
        try {
            connection = BD.getConnection();
            PreparedStatement ps_delete = connection.prepareStatement(SQL_DELETE_BY_ID);
            ps_delete.setInt(1, id);
            ps_delete.executeUpdate();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        LOGGER.debug("Paciente eliminado: " +id);
    }

    @Override
    public void actualizar(Paciente paciente) {
        Connection connection = null;
        try {
            connection = BD.getConnection();
            PreparedStatement ps_update = connection.prepareStatement(SQL_UPDATE_BY_ID);
            ps_update.setString(1, paciente.getNombre());
            ps_update.setString(2, paciente.getApellido());
            ps_update.setInt(3, paciente.getNumeroContacto());
            ps_update.setDate(4, java.sql.Date.valueOf(paciente.getFechaIngreso()));
            ps_update.setInt(5, paciente.getDomicilio().getId());
            ps_update.setString(6, paciente.getEmail());
            ps_update.setInt(7, paciente.getId());

            ps_update.executeUpdate();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        LOGGER.debug("Paciente actualizado: " +paciente.getId());
    }

    @Override
    public List<Paciente> buscarTodos() {
        Connection connection=null;
        List<Paciente> pacientes = new ArrayList<>();
        Domicilio domicilio= null;
        try{
            connection=BD.getConnection();
            //statement mundo java a sql
            Statement statement= connection.createStatement();
            PreparedStatement ps_select_all = connection.prepareStatement(SQL_SELECT_ALL);

            //ResultSet mundo bdd a java
            ResultSet rs= ps_select_all.executeQuery();
            DomicilioDAOH2 daoAux= new DomicilioDAOH2();
            while(rs.next()){
                domicilio=daoAux.buscar(rs.getInt(6));
                Paciente p = new Paciente(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getInt(4),rs.getDate(5).toLocalDate(),domicilio,rs.getString(7));
                pacientes.add(p);
            }
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        LOGGER.debug("Pacientes encontrados: " +pacientes.size());
        return pacientes;
    }
}
