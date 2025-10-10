package dao;

import model.Odontologo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OdontologoDAOH2 implements iDao<Odontologo> {
    private static final String SQL_SELECT_ONE=" SELECT * FROM ODONTOLOGOS WHERE ID=?";
    private static final String SQL_SELECT_ALL=" SELECT * FROM ODONTOLOGOS";
    private static final String SQL_UPDATE_BY_ID = "UPDATE ODONTOLOGOS SET NOMBRE=?, APELLIDO=?, MATRICULA=?, REQUISITOSTURNOS=? WHERE ID=?";
    private static final String SQL_DELETE_BY_ID = "DELETE FROM ODONTOLOGOS WHERE id=?";
    private static final String SQL_INSERT = "INSERT INTO ODONTOLOGOS(NOMBRE, APELLIDO, MATRICULA, REQUISITOSTURNOS) VALUES(?,?,?,?)";



    @Override
    public Odontologo guardar(Odontologo odontologo) {
        Connection connection = null;
        try {
            connection = BD.getConnection(); // tu clase utilitaria para obtener conexión
            PreparedStatement ps = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, odontologo.getNombre());
            ps.setString(2, odontologo.getApellido());
            ps.setString(3, odontologo.getMatricula());
            ps.setString(4, odontologo.getRequisitosTurnos());

            ps.executeUpdate();

            // Recuperar la primary key
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                odontologo.setId(rs.getInt(1));
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return odontologo;
    }

    @Override
    public Odontologo buscar(Integer id) {
        Connection connection = null;
        Odontologo odontologo = null;

        try {
            connection = BD.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL_SELECT_ONE);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                odontologo = new Odontologo();
                odontologo.setId(rs.getInt("ID"));
                odontologo.setNombre(rs.getString("NOMBRE"));
                odontologo.setApellido(rs.getString("APELLIDO"));
                odontologo.setMatricula(rs.getString("MATRICULA"));
                odontologo.setRequisitosTurnos(rs.getString("REQUISITOSTURNOS")); // puede ser null
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return odontologo;
    }

    @Override
    public void eliminar(Integer id) {
        Connection connection = null;

        try{
            connection = BD.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL_DELETE_BY_ID);

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Odontologo con id " + id + " eliminado");
    }

    @Override
    public void actualizar(Odontologo odontologo) {
        Connection connection = null;

        try{
            connection = BD.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL_UPDATE_BY_ID);

            ps.setString(1, odontologo.getNombre());
            ps.setString(2, odontologo.getApellido());
            ps.setString(3, odontologo.getMatricula());
            ps.setString(4, odontologo.getRequisitosTurnos());
            ps.setInt(5, odontologo.getId());

            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Odontologo> buscarTodos() {
        Connection connection = null;
        List<Odontologo> odontologos = new ArrayList<>();

        try {
            connection = BD.getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(SQL_SELECT_ALL);

            while (rs.next()) {
                Odontologo odontologo = new Odontologo();
                odontologo.setId(rs.getInt("ID"));
                odontologo.setNombre(rs.getString("NOMBRE"));
                odontologo.setApellido(rs.getString("APELLIDO"));
                odontologo.setMatricula(rs.getString("MATRICULA"));
                odontologo.setRequisitosTurnos(rs.getString("REQUISITOSTURNOS"));

                odontologos.add(odontologo);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return odontologos;
    }
}
