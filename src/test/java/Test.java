import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.svydovets.bibirnate.mapper.EntityMapperService;

public class Test {

    public static void main(String[] args) {

        var mapperService = new EntityMapperService();

        try {
            Properties connectionProps = new Properties();
            connectionProps.put("user", "postgres");
            connectionProps.put("password", "postgres");
            connectionProps.put("role", "vpestov");
            var connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres",
              connectionProps);
            var stmt = connection.createStatement();
//            stmt.executeQuery("select * from products");
            stmt.executeQuery("select * from test_table");
            var resultSet = stmt.getResultSet();
            while (resultSet.next()) {
                System.out.println();
                var product = mapperService.mapToObject(TestEntity.class, resultSet);
                System.out.println();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
