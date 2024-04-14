package mainPackage.util;

import mainPackage.DAOS.BookDAO;
import mainPackage.DAOS.PersonDAO;
import mainPackage.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class BookValidator implements Validator {
    private final BookDAO bookDAO;

    @Autowired
    public BookValidator(BookDAO bookdao) {
        this.bookDAO = bookdao;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Book.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Book book = (Book) o;
        if (bookDAO.validateByName(book.getBookName()).isPresent()) {
            errors.rejectValue("bookName", "", "This book is in library, you can't add same book");
        }
    }
}
