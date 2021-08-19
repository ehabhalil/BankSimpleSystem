package banksystem.modal;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PersonalInfo {

    final static int IDdeterminant = 1;

    public static boolean checkID(int id) throws SQLException {
        DBConnect.Connect();
        DBConnect.res = DBConnect.resultStatment("select idclient from bank.client where idclient ="+id+";");
        if(DBConnect.res.next())
            return true;
        DBConnect.close();
        return false;
    }
    //personal information
    protected int permission;
    protected long id;
    protected String name;
    protected String email;
    protected String password;

    //functions
    public PersonalInfo(long id) throws SQLException {
        getMyInfo(id);
    }
    public PersonalInfo(){
        
    }

    public PersonalInfo(long id,String name, String email, String password) {
        this.id = id;
        this.name=name;
        this.email = email;
        this.password = password;
        this.permission = givePermission(id);
    }

    private void getMyInfo(long id) throws SQLException {
        DBConnect.Connect();
        String s = tableType(id);
        DBConnect.res = DBConnect.resultStatment("select * from bank." + s + " where id" + s + "=" + String.valueOf(id) + ";");
        if (DBConnect.res.next()) {
            this.id = DBConnect.res.getInt("id" + s);
            this.name = DBConnect.res.getString("name");
            this.email = DBConnect.res.getString("email");
            this.password = DBConnect.res.getString("password");
        }
        DBConnect.close();
        this.permission = givePermission(id);
    }
    public PersonalInfo getInfo(long wantedID) throws SQLException {
        PersonalInfo p = new PersonalInfo();
        if( permission > givePermission(wantedID)){
            DBConnect.Connect();
            String s = tableType(id);
            DBConnect.res = DBConnect.resultStatment("select * from bank." + s + " where id" + s + "=" + String.valueOf(id) + ";");
            if (DBConnect.res.next()) {
                p.setId(DBConnect.res.getLong("id" + s))  ;
                p.setname(DBConnect.res.getString("name"));
                p.setEmail(DBConnect.res.getString("email"));
                p.setPassword(DBConnect.res.getString("password"), DBConnect.res.getString("password")) ;
            }
            DBConnect.close();
        }
        this.permission = givePermission(id);
        return p;
    }
    public static long generateID(String s){
        long ID = 0;
        try {
            DBConnect.Connect();
            DBConnect.res = DBConnect.resultStatment("select max(id" + s + ") from bank." + s + ";");
            if (DBConnect.res.next()) {
                ID = DBConnect.res.getLong(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PersonalInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        DBConnect.close();
        return ++ID;
    }

    public static String tableType(long id){
        char[] s = String.valueOf(id).toCharArray();
        String res;
        res = switch (s[0]) {
            case '1' -> "manager";
            case '2' -> "branch";
            case '3' -> "client";
            default -> "";
        };
        return res;
    }

    /**
     *
     * @param id
     * @return
     */
    public static String tableType(String id) {
        String s = "";
        for (int i = 0; i < IDdeterminant; i++) {
            s += id.charAt(i);
        }
        s = switch (Integer.parseInt(s)) {
            case 1 -> "manager";
            case 2 -> "branch";
            case 3 -> "client";
            default -> "";
        };
        return s;
    }

    public long getId() {
        return id;
    }

    public String getname() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean setId(long id) {
        if(tableType(id).equals(""))
            return false;
        else this.id = id;
        return true;
    }


    public boolean setname(String Fname) {
        if(Fname.equals(""))
            return false;
        this.name = Fname;
        return true;
    }


    public boolean setEmail(String email) {
        if(email.equals(""))
            return false;
        for(int i=0;i<email.length();i++)
            if(email.charAt(i) != '@')
                ;
            else {
                this.email = email;
                return true;
            }
        return false;
    }

    public boolean setPassword(String password,String verifyPassword) {
        if(password.equals("")||verifyPassword.equals(""))
            return false;
        if(password.equals(verifyPassword)){
            this.password = password;
            return true;
        }
        return false;
    }

    public boolean createNewAccount(PersonalInfo p){
        if(this.permission > p.permission){
            String s = PersonalInfo.tableType(p.id);
            DBConnect.Connect();
            DBConnect.updateStatment("INSERT INTO `bank`.`client` (`idclient`, `name`, `email`, `password`) VALUES ('"+p.id+"', '"+p.name+"', '"+p.email+"', '"+p.password+"');");
            DBConnect.updateStatment("INSERT INTO `bank`.`lira_account` (`id`, `lira`) VALUES ('"+p.id+"', '"+0+"');");
            DBConnect.close();
            return true;
        }
        else return false;
    }
    public boolean deleteAccount(PersonalInfo p){
        if(this.permission > p.permission){
            String s = PersonalInfo.tableType(p.id);
            DBConnect.Connect();
            DBConnect.updateStatment("delete from `bank`.`"+s+"` where id"+s+" ="+p.id+";");
            DBConnect.close();
            return true;
        }
        else return false;
    }
    public boolean updateAccountInfo(PersonalInfo p) throws SQLException{
        if(this.permission > p.permission){
            String s = PersonalInfo.tableType(p.id);
            DBConnect.Connect();
            DBConnect.updateStatment("UPDATE `bank`.`"+s+"` SET  `name` = '"+p.name+"',`email` = '"+p.email+"', `password` = '"+p.password+"' WHERE (`id"+s+"` = "+p.id+");");
            DBConnect.close();
            return true;
        }
        else return false;
    }

    private int givePermission(long id) {
        int acc;
        acc = switch (PersonalInfo.tableType(id)) {
            case "manager" -> 3;
            case "branch" -> 2;
            case "client" -> 1;
            default -> 0;
        };
        return acc;
    }
}
