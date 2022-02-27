package day03.elearning;

import org.mariadb.jdbc.MariaDbDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PersonDao {

    private MariaDbDataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    public PersonDao(MariaDbDataSource dataSource) {
        this.dataSource = dataSource;
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public static void main(String[] args) {

        PersonDao personDao;

        try {
            MariaDbDataSource dataSource = new MariaDbDataSource();
            // dataSource = new MariaDbDataSource();
            dataSource.setUrl(("jdbc:mariadb://localhost:3306/people?useUnicode=true"));
            dataSource.setUser("peopleuser");
            dataSource.setPassword("peoplepassword");
            personDao = new PersonDao(dataSource);
        } catch (SQLException sqle) {
            throw new IllegalStateException("Can not reach database", sqle);
        }

//        try (Connection conn = dataSource.getConnection();
//             Statement stmt = conn.createStatement()){
//            stmt.executeUpdate("INSERT INTO person (name_of_person, date_of_birth, height, weight) VALUES ('Kiss József', '1985-02-03', 187, 67.4);");
//
//        } catch (SQLException sqle) {
//            throw new IllegalStateException("Can not insert.", sqle);
//        }


        //personDao.savePerson(new Person("Kiss József", LocalDate.of(1985, 2, 3), 187, 67.4));

        /*
        List<Person> people = new ArrayList<>();
        people.add(new Person("Kiss József", LocalDate.of(1985, 2, 3), 187, 67.4));
        people.add(new Person("Nagy Béla", LocalDate.of(1985, 2, 3), 187, 67.4));
        personDao.savePersons(people);
        */

        // personDao.savePersonWithJdbc(new Person("Kiss József", LocalDate.of(1985, 2, 3), 187, 67.4));

        /*
        List<Person> people = Arrays.asList(
        new Person("Kiss József", LocalDate.of(1985, 2, 3), 187, 67.4),
        new Person("Nagy Béla", LocalDate.of(1985, 2, 3), 187, 67.4));
        personDao.savePersonsWithJdbc(people);
        */

        //personDao.updateName(2, "xy");

        // personDao.updateNameWithJdbc(2, "xy");

        // personDao.deletePerson(2);

        // personDao.deletePersonWithJdbc(3);

        Person person = personDao.findById(5);
        System.out.println(person.getNameOfPerson());

        Person person2 = personDao.findByIdWithJdbc(7);
        System.out.println(person2.getNameOfPerson());

        System.out.println(personDao.listPeopleNames());
        Optional<List<String>> ns = personDao.listPeopleNames();
        List<List<String>> ls = ns.stream().toList();
        List<String> ls2 = ls.get(0);
        System.out.println(ls2.get(0).toString());

        System.out.println(personDao.listPeopleNamesWithJdbc());

        personDao.savePersonAndGetGenerateKey(new Person("Kiss József", LocalDate.of(1985, 2, 3), 187, 67.4));

        personDao.savePersonAndGetGenerateKeyWithJdbc(new Person("Kiss József", LocalDate.of(1985, 2, 3), 187, 67.4));

        personDao.updateNameInTransaction(8, "Péter");

        personDao.updateNameInTransactionWithAnnotation(9, "Valter");
    }


    public void savePerson(Person person) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO person (name_of_person, date_of_birth, height, weight) VALUES (?, ?, ?, ?);")) {
            stmt.setString(1, person.getNameOfPerson());
            stmt.setDate(2, Date.valueOf(person.getDateOfBirth()));
            stmt.setInt(3, person.getHeight());
            stmt.setDouble(4, person.getWeight());
            stmt.executeUpdate();
        } catch (SQLException sqle) {
            throw new IllegalStateException("Can not insert.", sqle);
        }
    }

    public void savePersons(List<Person> people) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO person (name_of_person, date_of_birth, height, weight) VALUES (?, ?, ?, ?);")) {
            for (Person person : people) {
                stmt.setString(1, person.getNameOfPerson());
                stmt.setDate(2, Date.valueOf(person.getDateOfBirth()));
                stmt.setInt(3, person.getHeight());
                stmt.setDouble(4, person.getWeight());
                stmt.executeUpdate();
            }
        } catch (SQLException sqle) {
            throw new IllegalStateException("Can not insert.", sqle);
        }
    }

    public void savePersonWithJdbc(Person person) {
        jdbcTemplate.update("INSERT INTO person (name_of_person, date_of_birth, height, weight) VALUES (?, ?, ?, ?);",
                person.getNameOfPerson(), person.getDateOfBirth(), person.getHeight(), person.getWeight());
    }

    public void savePersonsWithJdbc(List<Person> people) {
        for (Person person : people) {
            jdbcTemplate.update("INSERT INTO person (name_of_person, date_of_birth, height, weight) VALUES (?, ?, ?, ?);",
                    person.getNameOfPerson(), person.getDateOfBirth(), person.getHeight(), person.getWeight());
        }
    }


    public void savePerson_old(Person person) {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("INSERT INTO person (name_of_person, date_of_birth, height, weight) VALUES ('Kiss József', '1985-02-03', 187, 67.4);");

        } catch (SQLException sqle) {
            throw new IllegalStateException("Can not insert.", sqle);
        }
    }

    public void updateName(long id, String newName) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE person SET name_of_person = ? WHERE id = ?;")) {
            stmt.setString(1, newName);
            stmt.setLong(2, id);
            stmt.executeUpdate();
        } catch (SQLException sqle) {
            throw new IllegalStateException("Can not update", sqle);
        }
    }

    public void updateNameWithJdbc(long id, String newName) {
        jdbcTemplate.update("UPDATE person SET name_of_person = ? WHERE id = ?;", newName, id);
    }

    public void deletePerson(long id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE from person WHERE id = ?; ")) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException sqle) {
            throw new IllegalStateException("Can not delete", sqle);
        }
    }

    public void deletePersonWithJdbc(long id) {
        jdbcTemplate.update("DELETE from person WHERE id = ?; ", id);
    }

    public Person findById(long id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * from person WHERE id = ?;")) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name_of_person");
                    LocalDate dateOfBirth = rs.getDate("date_of_birth").toLocalDate();
                    int height = rs.getInt("height");
                    double weight = rs.getDouble("weight");
                    return new Person(name, dateOfBirth, height, weight);


                }
            }
        } catch (SQLException sqle) {
            throw new IllegalStateException("Can not read", sqle);
        }
        throw new IllegalArgumentException("No such person");
    }

    public Person findByIdWithJdbc(long id) {
        return jdbcTemplate.queryForObject("SELECT * from person where id = ?;",
                (rs, rowNum) -> new Person(rs.getString("name_of_person"),
                        rs.getDate("date_of_birth").toLocalDate(),
                        rs.getInt("weight"),
                        rs.getDouble("weight")),
                id);
    }

    public Optional<List<String>> listPeopleNames() {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name_of_person from person;")) {
            List<String> names = new ArrayList<>();
            while (rs.next()) {
                String name = rs.getString("name_of_person");
                names.add(name);
            }
            return Optional.of(names);
        } catch (SQLException sqle) {
            throw new IllegalStateException("Can not find person", sqle);
        }
    }

    public Optional<List<String>> listPeopleNamesWithJdbc() {
        return Optional.of(jdbcTemplate.query("SELECT name_of_person from person;",
                (rs, rowNum) -> rs.getString("name_of_person")));
    }

    public void savePersonAndGetGenerateKey(Person person) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO person (name_of_person, date_of_birth, height, weight) VALUES (?, ?, ?, ?);"
                     , Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, person.getNameOfPerson());
            stmt.setDate(2, Date.valueOf(person.getDateOfBirth()));
            stmt.setInt(3, person.getHeight());
            stmt.setDouble(4, person.getWeight());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    System.out.println(rs.getLong(1));
                }
            }
        } catch (SQLException sqle) {
            throw new IllegalStateException("Can not insert.", sqle);
        }
    }

    public void savePersonAndGetGenerateKeyWithJdbc(Person person) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps =
                    connection.prepareStatement("INSERT INTO person (name_of_person, date_of_birth, height, weight) VALUES (?, ?, ?, ?);",
                            Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, person.getNameOfPerson());
            ps.setDate(2, Date.valueOf(person.getDateOfBirth()));
            ps.setInt(3, person.getHeight());
            ps.setDouble(4, person.getWeight());
            return ps;
        }, keyHolder);
        System.out.println(keyHolder.getKey().longValue());
    }

    public void updateNameInTransaction(long id, String newName) {
        try (Connection conn = dataSource.getConnection()){
            conn.setAutoCommit(false);
            try(PreparedStatement stmt = conn.prepareStatement("UPDATE person SET name_of_person = ? WHERE id = ?;")) {
                stmt.setString(1, newName);
                stmt.setLong(2, id);
                stmt.executeUpdate();
                conn.commit();
            }catch (Exception ex) {
                conn.rollback();
                throw new IllegalStateException("Transaction not successed");
            }
        } catch (SQLException sqle) {
            throw new IllegalStateException("Can not update", sqle);
        }
    }

    @Transactional
    public void updateNameInTransactionWithAnnotation(long id, String newName) {
        jdbcTemplate.update("UPDATE person SET name_of_person = ? WHERE id = ?;", newName, id);
    }


}
