package dao;

import model.Domicilio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class DomicilioDAOH2 implements iDao<Domicilio> {
    private static final String SQL_DOMICILIOS="SELECT * FROM DOMICILIOS WHERE ID=?";
    private static final String SQL_INSERT_ONE = "INSERT INTO DOMICILIOS (calle, numero, localidad, provincia) VALUES (?,?,?,?)";

    @Override
    public Domicilio guardar(Domicilio domicilio) {
        Connection connection = null;
        try {
            connection = BD.getConnection();
            PreparedStatement ps_insert = connection.prepareStatement(
                    SQL_INSERT_ONE,
                    Statement.RETURN_GENERATED_KEYS
            );
            ps_insert.setString(1, domicilio.getCalle());
            ps_insert.setInt(2, domicilio.getNumero());
            ps_insert.setString(3, domicilio.getLocalidad());
            ps_insert.setString(4, domicilio.getProvincia());

            ps_insert.executeUpdate();

            ResultSet keys = ps_insert.getGeneratedKeys();
            if (keys.next()) {
                domicilio.setId(keys.getInt(1));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Domicilio guardado con id: " + domicilio.getId());
        return domicilio;
    }

    @Override
    public Domicilio buscar(Integer id) {
        Connection connection= null;
        Domicilio domicilio= null;
        try{
            connection= BD.getConnection();
            PreparedStatement ps_select_one= connection.prepareStatement(SQL_DOMICILIOS);
            ps_select_one.setInt(1,id);
            ResultSet rs= ps_select_one.executeQuery();
            while(rs.next()){

                domicilio= new Domicilio(rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getString(4),rs.getString(5));
            }

        }catch (Exception e){
            e.getMessage();
        }
        System.out.println("domicilio encontrado");
        return domicilio;
    }

    @Override
    public void eliminar(Integer id) {}

    @Override
    public void actualizar(Domicilio domicilio) {}

    @Override
    public List<Domicilio> buscarTodos() {
        return List.of();
    }
}
