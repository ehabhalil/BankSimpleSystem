/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banksystem.modal;
import banksystem.view.*;
import java.sql.SQLException;
/**
 *
 * @author EHAB-
 */
public class Manager extends PersonalInfo {
    private ManagerPage managerPage;
    private int clientNumber;
    
    public Manager(long id) throws SQLException{
        super(id);
    }
    public Manager(long id,ManagerPage managerPage) throws SQLException{
        super(id);
        this.managerPage = managerPage;
        this.managerPage.setVisible(true);
        clientNumber = getClietNnumber();
        
    }

    public Manager() {
        
    }

    public int getClietNnumber() throws SQLException {
        DBConnect.Connect();
        DBConnect.res = DBConnect.resultStatment("select count(*) from bank.client;");
        int n=0;
        if(DBConnect.res.next()){
             n = DBConnect.res.getInt(1);
        }
        DBConnect.close();
        return n;
    }
    public void addNewClient(String name,String email,String password){
        long id = PersonalInfo.generateID("client");
        this.createNewAccount(new PersonalInfo(id,name,email,password));
        System.out.println(name+email+password);
    }
    public boolean changeClientMoney(long ID,double money) throws SQLException{
        DBConnect.Connect();
        DBConnect.res = DBConnect.resultStatment("SELECT lira FROM bank.lira_account where id = "+ID+";");
        if(DBConnect.res.next())
            money += DBConnect.res.getDouble(1);
        if(money < 0 )
            return false; 
        DBConnect.updateStatment("update bank.lira_account set lira ="+money + " where(id="+ID +");");
        DBConnect.close();
        return true;
    }
    
}
