package tadjik.ilyosjon;

import java.sql.*;

public class MyDatabase {


    String url="jdbc:postgresql://localhost:5432/postgres";
    String username="postgres";
    String password="root";


    public void creatvazifa(Vazifalar vazifalar){
        try {
            Connection connection = DriverManager.getConnection(url,username, password);
            Statement statement = connection.createStatement();

            String query ="insert into vazifalar(Id, nomi, sanasi, status) values ("+vazifalar.getId()+",'"+vazifalar.getNomi()+"','"+vazifalar.getSanasi()+"','"+vazifalar.getStatus()+"');";
            statement.execute(query);
            System.out.println("Vazifalar qo`shildi");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public String readvazifa(){

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();

            String query ="select * from vazifalar";
            ResultSet resultSet = statement.executeQuery(query);

            StringBuilder builder = new StringBuilder();
            while(resultSet.next()){
                int id = resultSet.getInt(1);
                String nomi = resultSet.getString(2);
                String sanasi = resultSet.getString(3);
                String status = resultSet.getString(4);
                builder.append("id: "+id+"\n");
                builder.append("nomi: "+nomi+"\n");
                builder.append("sanasi: "+sanasi+"\n");
                builder.append("status: "+status+"\n");
                builder.append("\n");
            }

            return builder.toString();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void updatevazifa(int id, String nomi){

        Connection connection= null;
        try {
            connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            String query=" update vazifalar set nomi='"+nomi+"' where id="+id;
            statement.execute(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void deletevazifa(int id){
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            String query ="delete from vazifalar where id="+id;
            statement.execute(query);
            System.out.println("Delete vazifa");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

