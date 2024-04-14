package mainPackage.DAOS;

import mainPackage.models.Book;
import mainPackage.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BookDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> showAllBooks(){
        return jdbcTemplate.query("SELECT * FROM Book", new BeanPropertyRowMapper<>(Book.class));
    }

    public void createBook(Book book){
        jdbcTemplate.update("INSERT INTO Book (bookname, author, year) VALUES (?,?,?)", book.getBookName(),book.getAuthor(),book.getYear());
    }

    public void updateBook(Book book, int id){
        jdbcTemplate.update("UPDATE Book SET bookname=?, author=?, year=? WHERE id=?",
                book.getBookName(), book.getAuthor(), book.getYear(), id);
    }

    public Book showBookById(int id){
        return jdbcTemplate.query("SELECT * FROM Book WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Book.class)).stream().findAny().orElse(null);
    }

    public void deleteBook(int id){
        jdbcTemplate.update("DELETE FROM Book WHERE id=?", id);
    }

    public void occupyBook(int personId, int bookId) {
        jdbcTemplate.update("UPDATE Book SET person_id = ? WHERE id = ?", personId, bookId);

    }
    public List<Person> getAllPeople(){
        List<Person> personList = jdbcTemplate.query("SELECT * FROM Person", (RowMapper<Person>) new BeanPropertyRowMapper<>(Person.class));
        return personList;
    }

    public Person getOwnerOfTheBook(int bookId) {
        String sql = "SELECT * FROM Person WHERE id = (SELECT person_id FROM Book WHERE id = ?)";
        List<Person> owners = jdbcTemplate.query(sql, new Object[]{bookId}, new BeanPropertyRowMapper<>(Person.class));
        return owners.isEmpty() ? null : owners.get(0);
    }

    public void makeBookAvailable(int personId) {
        jdbcTemplate.update("UPDATE Book SET person_id = NULL WHERE person_id = ?", personId);
    }

    public void deleteSelectedBook(int bookId) {
        jdbcTemplate.update("UPDATE Book SET person_id = NULL WHERE Book.id=?",bookId);
    }

    public Optional validateByName(String bookName){
        return jdbcTemplate.query("SELECT * FROM Book WHERE bookname=?", new Object[]{bookName}, new BeanPropertyRowMapper<>(Book.class)).stream().findAny();
    }




}
