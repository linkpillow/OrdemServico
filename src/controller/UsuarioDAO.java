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
import java.util.HashSet;
import javax.swing.JOptionPane;
import jdbc.ModuloConexao;
import model.Usuario;
import view.TelaLogin;
import view.TelaPrincipal;

/**
 *
 * @author clebe
 */
public class UsuarioDAO {
    
    private Connection con;
    
    public UsuarioDAO(){
        this.con = ModuloConexao.conectar();
    }
    
    //Metodo efetuaLogin
    public void efetuaLogin(String email, String senha ) {
       
        try {

            //1 passo - SQL
            String sql = "select * from tbusuarios where login = ? and senha = md5(?)";
            PreparedStatement stmt;
            stmt = con.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, senha);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                //Usuario logou
                String perfil = rs.getString(6);
                TelaPrincipal tela = new TelaPrincipal();
                tela.setVisible(true);
                if(perfil.equals("admin")){
                    TelaPrincipal.JMnItmUsuario.setEnabled(true);
                    TelaPrincipal.jMnRelatorio.setEnabled(true);
                    TelaPrincipal.jLblUsuario.setText(rs.getString(2));
                }
                
            } else {
                //Dados incorretos
                JOptionPane.showMessageDialog(null, "Dados incorretos!");
                new TelaLogin().setVisible(true);
            }

        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro : " + erro);
        }

    }
    
    public void adicionarUsuario(Usuario obj){
        try{
            String sql = "insert into tbusuarios(iduser,usuario,fone,login,senha,perfil) values(?,?,?,?,md5(?),?)";
            con = ModuloConexao.conectar();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, obj.getIdUser());
            stmt.setString(2, obj.getUsuario());
            stmt.setString(3, obj.getFone());
            stmt.setString(4, obj.getLogin());
            stmt.setString(5, obj.getSenha());
            stmt.setString(6, obj.getPerfil());
            
            stmt.execute();
            stmt.close();
            JOptionPane.showMessageDialog(null, "Usuário cadastrado com sucesso!");
        } catch (SQLIntegrityConstraintViolationException e1){
            JOptionPane.showMessageDialog(null, "Login em uso.\nEscolha outro login.");
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
    public Usuario buscarUsuario(int idUser){
        try{
            String sql = "select * from tbusuarios WHERE iduser = ?";
            
            con = ModuloConexao.conectar();
            PreparedStatement stmt =  con.prepareStatement(sql);
            stmt.setInt(1, idUser);
            
            ResultSet rs = stmt.executeQuery();
            
            
            if(rs.next()){
                Usuario usuario = new Usuario();
                usuario.setIdUser(rs.getInt("idUser"));
                usuario.setUsuario(rs.getString("usuario"));
                usuario.setFone(rs.getString("fone"));
                usuario.setLogin(rs.getString("login"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setPerfil(rs.getString("perfil"));
                
                return usuario;
            }else{
                JOptionPane.showMessageDialog(null, "Usuário não encontrado!!");
            }
            stmt.close();
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        return null;
    }
    public void alterarUsuario(int idUser, Usuario obj){
        try{
            String sql = "UPDATE tbusuarios SET usuario = ?, fone = ?, login = ?, senha = md5(?), perfil = ? WHERE iduser = ?;";
            con = ModuloConexao.conectar();
            PreparedStatement stmt = con.prepareStatement(sql);
            
            stmt.setString(1, obj.getUsuario());
            stmt.setString(2, obj.getFone());
            stmt.setString(3, obj.getLogin());
            stmt.setString(4, obj.getSenha());
            stmt.setString(5, obj.getPerfil());
            stmt.setInt(6, idUser);
            
            stmt.execute();
            
            stmt.close();
            JOptionPane.showMessageDialog(null, "Usuário alterado com sucesso!");  
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
    public Usuario deletarUsuario(int idUser){
        try{
            //sql
            String sql = "DELETE FROM tbusuarios WHERE iduser = ?";
            
            con = ModuloConexao.conectar();
            PreparedStatement stmt = con.prepareStatement(sql);
            
            stmt.setInt(1, idUser);
            
            ResultSet rs = stmt.executeQuery();
            
            stmt.execute();
            stmt.close();
            JOptionPane.showMessageDialog(null, "Usuário deletado com sucesso!");
            Usuario usuario = new Usuario();
            usuario.setIdUser(rs.getInt("idUser"));
            usuario.setUsuario(rs.getString("usuario"));
            usuario.setFone(rs.getString("fone"));
            usuario.setLogin(rs.getString("login"));
            usuario.setSenha(rs.getString("senha"));
            usuario.setPerfil(rs.getString("perfil"));
                
            return usuario;
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
}
