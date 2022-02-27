package day03.elearning;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mariadb.jdbc.MariaDbDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PersonDaoTest {

    PersonDao personDao;

    @BeforeEach
    void init() throws SQLException {
        MariaDbDataSource dataSource = new MariaDbDataSource();
        dataSource.setUrl(("jdbc:mariadb://localhost:3306/people?useUnicode=true"));
        dataSource.setUser("peopleuser");
        dataSource.setPassword("peoplepassword");
        personDao = new PersonDao(dataSource);

        // deleteAndCreateTable(dataSource);
        Flyway fw = Flyway.configure().dataSource(dataSource).load();
        fw.clean();
        fw.migrate();
    }


    @Test
    void testSavePerson(){
        Person person = new Person("Kiss József", LocalDate.of(1985, 2, 3), 187, 67.4);
        personDao.savePerson(person);
        Person expected = personDao.findById(1);

        assertEquals("Kiss József", expected.getNameOfPerson());
    }



    private void deleteAndCreateTable(DataSource dataSource) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()){
            stmt.executeUpdate("DROP table person;");
            stmt.executeUpdate("CREATE TABLE if NOT EXISTS person(\n" +
                    "id BIGINT not null AUTO_INCREMENT,\n" +
                    "name_of_person VARCHAR(250),\n" +
                    "date_of_birth DATE,\n" +
                    "height INT,\n" +
                    "weight FLOAT,\n" +
                    "PRIMARY KEY(id)person\n" +
                    ");");
        }

    }

}