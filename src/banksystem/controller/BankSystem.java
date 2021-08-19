/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banksystem.controller;

import banksystem.modal.*;
import banksystem.view.*;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author EHAB-
 */
public class BankSystem {
    private LoginPage logPage;
    private static Manager mngr;
    private static Client clnt;
    
    public BankSystem(){
        
    }
    public BankSystem(LoginPage logPage) {
        this.logPage = logPage;
        this.logPage.setVisible(true);
    }
    public boolean checkPassword(String s1,String s2){
        return s1.equals(s2) && !s1.equals("");
    }
    public boolean checkID(String s){
        try {
            Long.parseLong(s);
        }catch(NumberFormatException e){
            return false;
        }
        return false;
    }
    public boolean startLogin(String user , String pass){
        if( "".equals(pass) || user.equals("") ){
            JOptionPane.showMessageDialog(null,"Please Enter both Password and Account No !!!","Warning",3);
            return false;
        }
        for(int i=0;i<user.length();i++)
            if(user.charAt(i)<'0' || user.charAt(i)>'9'){
                JOptionPane.showMessageDialog(null,"Account no should be number only","Warning",3);
                return false;
            }
        String s="";
        s = PersonalInfo.tableType(user);
        String pass2 = null; 
        String query = "select * from bank."+s+" where id"+s+"= "+user+";";
        try {
            DBConnect.Connect();
            DBConnect.resultStatment(query);
            if(DBConnect.res.next())
                pass2 = DBConnect.res.getString("password");
            if(pass2 == null){
                JOptionPane.showMessageDialog(null,"Account No or password is incorrect !!!","Warning",2);
                return false;
            }
            if(pass2.equals(pass) ){
                switch(s){
                    case "manager" -> mngr = new Manager(Integer.parseInt(user),new ManagerPage());
                    case "client" -> clnt = new Client(Integer.parseInt(user),new ClientPage(Integer.parseInt(user)));
                    default -> JOptionPane.showMessageDialog(null,"Error 505","Warning",2);
                }
            }
            else {
                JOptionPane.showMessageDialog(null,"Account No or password is incorrect !!!","Warning",2);
                return false;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"SQLException Account No or password is incorrect !!!\n"+ex,"Warning",1);
        }
        finally {DBConnect.close();}
        return true;
    }
    public boolean checkMoney(String s) {
        try{
            Double.parseDouble(s);
        }catch(NumberFormatException e){
            return false;
        }
        return true;
    }
    public boolean excuteMoneyTransfer(String id,String amount) throws SQLException{
        return clnt.transferMoney(Long.parseLong(id), Double.parseDouble(amount));
    }
    public void excuteMoneyChange(String s,long id,boolean r) throws SQLException{
        mngr = new Manager(1);
        double res =  r ? -Double.parseDouble(s): Double.parseDouble(s);
        mngr.changeClientMoney(id, res);
    }
    public boolean excuteAccountChange(PersonalInfo per) throws SQLException{
        mngr = new Manager(1);
        return mngr.updateAccountInfo(per);
    }       
    public double getMoneyAmount(long id) throws SQLException {
        clnt = new Client(id);
        return clnt.getMoneyAccount((int) id);
    }

    public boolean checkMoneyAvilability(long id,String transferAmount) throws SQLException {
        double amount = this.getMoneyAmount(id);
        return amount >= Double.parseDouble(transferAmount);
    }
    
    public static void main(String[] args) throws SQLException{
        new BankSystem(new LoginPage());
        
    }

    public long getID() {
        if(clnt != null) 
            return clnt.getId();
        return 0;
    }

    

}
