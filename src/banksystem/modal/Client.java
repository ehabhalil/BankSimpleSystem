/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banksystem.modal;

import banksystem.view.ClientPage;
import java.sql.SQLException;

/**
 *
 * @author EHAB-
 */
public class Client extends PersonalInfo{
        private ClientPage clientPage;
        private double moneyAmount;
        
    public Client(long id) throws SQLException {
        super(id);
    }

    public Client(int id, ClientPage clientPage) throws SQLException {
        super(id);
        this.clientPage = clientPage;
        this.clientPage.setVisible(true);
        moneyAmount = getMoneyAccount(id);
    }
    public boolean buy(double price) throws SQLException{
        double amount=0;
        DBConnect.Connect();
        DBConnect.res = DBConnect.resultStatment("SELECT lira FROM bank.lira_account where id = "+id+";");
        if(DBConnect.res.next())
            amount = DBConnect.res.getDouble(1);
        if(amount - price < 0 )
            return false;
        else DBConnect.updateStatment("update bank.lira_account set lira ="+(amount - price) + " where(id="+id +");");
        DBConnect.close();
        return true;
    }
    
    public boolean transferMoney(long receiverID,double amount) throws SQLException{
        if(receiverID == this.id)
            return false;
        double currentAmount=0,currentAmountReceiver=0;
        DBConnect.Connect();
        DBConnect.res = DBConnect.resultStatment("SELECT lira FROM bank.lira_account where id = "+id+";");
        if(DBConnect.res.next())
            currentAmount = DBConnect.res.getDouble(1);
        if(currentAmount- amount< 0 )
            return false;
        else {
            DBConnect.res = DBConnect.resultStatment("SELECT lira FROM bank.lira_account where id = "+receiverID+";");
            if(DBConnect.res.next())
                currentAmountReceiver = DBConnect.res.getDouble(1);
            DBConnect.updateStatment("update bank.lira_account set lira ="+(currentAmount - amount) + " where(id="+this.id +");");
            DBConnect.updateStatment("update bank.lira_account set lira ="+(amount +currentAmountReceiver)+ " where(id="+receiverID +");");
        }
        DBConnect.close();
        return true;
    }

    public double getMoneyAccount(int id) throws SQLException {
        double amount=0;
        DBConnect.Connect();
        DBConnect.res = DBConnect.resultStatment("SELECT lira FROM bank.lira_account where id = "+id+";");
        if(DBConnect.res.next())
            amount = DBConnect.res.getDouble(1);
        return amount;
    }
}
