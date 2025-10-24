/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.awt.HeadlessException;
import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import jdbc.ModuloConexao;
import model.Cliente;

/**
 *
 * @author clebe
 */
public class ClienteDAO {
    
    private Connection con;
    
    public ClienteDAO(){
        this.con = ModuloConexao.conectar();
    }
    
    public void adicionarCliente(Cliente obj){
        try{
            String sql = "insert into tbclientes(idcli, nomecli, endcli, fonecli, emailcli) values(?,?,?,?,?)";
            con = ModuloConexao.conectar();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, obj.getId());
            stmt.setString(2, obj.getNome());
            stmt.setString(3, obj.getEndereco());
            stmt.setString(4, obj.getFone());
            stmt.setString(5, obj.getEmail());
            
            stmt.execute();
            stmt.close();
            JOptionPane.showMessageDialog(null, "Cliente cadastrado com sucesso!");
        } catch (SQLIntegrityConstraintViolationException e1){
            JOptionPane.showMessageDialog(null, "Nome em uso.\nEscolha outro nome.");
        } catch (HeadlessException | SQLException e){
            JOptionPane.showMessageDialog(null, e);
        } finally {
            try{
                con.close();
            } catch(SQLException ex){
                JOptionPane.showMessageDialog(null, ex);
                
            }
        }
    }
    public void alterarCliente(Cliente obj){
        try{
            String sql = "UPDATE tbclientes SET nomecli = ?, endcli = ?, fonecli = ?, emailcli WHERE idcli = ?;";
            con = ModuloConexao.conectar();
            PreparedStatement stmt = con.prepareStatement(sql);
            
            stmt.setString(1, obj.getNome());
            stmt.setString(2, obj.getEndereco());
            stmt.setString(3, obj.getFone());
            stmt.setString(4, obj.getEmail());
            stmt.setInt(5, obj.getId());
            
            stmt.execute();
            
            stmt.close();
            JOptionPane.showMessageDialog(null, "Cliente alterado com sucesso!");  
        } catch (HeadlessException | SQLException e){
            JOptionPane.showMessageDialog(null, e);
        } finally {
            try{
                con.close();
            } catch(SQLException ex){
                JOptionPane.showMessageDialog(null, ex);
            }
        }
    }
    public Cliente deletarCliente(int idCli){
        try{
            //sql
            String sql = "DELETE FROM tbclientes WHERE idcli = ?";
            
            con = ModuloConexao.conectar();
            PreparedStatement stmt = con.prepareStatement(sql);
            
            stmt.setInt(1, idCli);
            
            ResultSet rs = stmt.executeQuery();
            
            stmt.execute();
            stmt.close();
            JOptionPane.showMessageDialog(null, "Cliente deletado com sucesso!");
            Cliente cliente = new Cliente();
            cliente.setId(rs.getInt("idcli"));
            cliente.setNome(rs.getString("nomecli"));
            cliente.setEndereco(rs.getString("enderecocli"));
            cliente.setEmail(rs.getString("emailcli"));
            cliente.setFone(rs.getString("fonecli"));
                
            return cliente;
        } catch(HeadlessException | SQLException e){
            JOptionPane.showMessageDialog(null, e);
        } finally {
            try{
                con.close();
            } catch(SQLException ex){
                JOptionPane.showMessageDialog(null, ex);
            }
        }
        return null;
    }
    
    public List<Cliente> listarCliente() {
        try {

            //1 passo criar a lista
            List<Cliente> lista = new ArrayList<>();

            //2 passo - criar o sql , organizar e executar.
            String sql = "select * from tbclientes";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Cliente obj = new Cliente();

                obj.setId(rs.getInt("idcli"));
                obj.setNome(rs.getString("nomecli"));
                obj.setEndereco(rs.getString("endcli"));
                obj.setFone(rs.getString("fonecli"));
                obj.setEmail(rs.getString("emailcli"));
                lista.add(obj);
            }

            return lista;

        } catch (SQLException erro) {

            JOptionPane.showMessageDialog(null, "Erro :" + erro);
            return null;
        }

    }
}
