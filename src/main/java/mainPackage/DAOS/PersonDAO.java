package mainPackage.DAOS;

import mainPackage.models.Book;
import mainPackage.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

//ПРОСМОТР ВСЕХ ЛЮДЕЙ
public List<Person> show() {
    return jdbcTemplate.query("SELECT * FROM Person", new BeanPropertyRowMapper<>(Person.class));
}


    //ПРОСМОТР ОДНОГО ЧЕЛОВЕКА
    public Person showPerson(int id) {
        return jdbcTemplate.query("SELECT * FROM Person WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Person.class)).stream().findAny().orElse(null);
    }

    //ЗАНЕСЕНИЕ ЧЕЛОВЕКА В БАЗУ ДАННЫХ
    public void save(Person person){
        jdbcTemplate.update("INSERT INTO Person (full_name, date_of_birth ) VALUES (?,?)", person.getFullName(), person.getDateOfBirth());
    }
 //УДАЛЕНИЕ ЧЕЛОВЕКА ИЗ БД
    public void deletePerson(int id){
        jdbcTemplate.update("DELETE FROM Person WHERE id=?", id );
    }
    //ОБНОВЛЕНИЕ ДАННЫХ UPDATE
    public void updatePerson(int id, Person updatedPerson) {
        jdbcTemplate.update("UPDATE Person SET full_name=?, date_of_birth=? WHERE id=?",
                updatedPerson.getFullName(), updatedPerson.getDateOfBirth(), id);
    }

    public List<Book> showTakenBooks(int personId) {
        String sql = "SELECT * FROM Book WHERE person_id = ?";
        return jdbcTemplate.query(sql, new Object[]{personId}, new BeanPropertyRowMapper<>(Book.class));
    }














}
