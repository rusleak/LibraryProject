package mainPackage.controllers;

import mainPackage.DAOS.BookDAO;
import mainPackage.DAOS.PersonDAO;
import mainPackage.models.Book;
import mainPackage.models.Person;
import mainPackage.util.BookValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookDAO bookDAO;
    private final BookValidator bookValidator;
    private final PersonDAO personDAO;

    @Autowired
    public BookController(BookDAO bookDAO, BookValidator bookValidator, PersonDAO personDAO) {
        this.bookDAO = bookDAO;
        this.bookValidator = bookValidator;
        this.personDAO = personDAO;
    }

    @GetMapping
    public String showAllBooks(Model model) {
        model.addAttribute("ListOfAllBooks", bookDAO.showAllBooks());
        return "/books/allbooks";
    }

    @GetMapping("/new")
    public String createBook(Model model){
        model.addAttribute("newBook", new Book());
        return "/books/newBook";
    }

    @PostMapping("/new")
    public String createBookPost(@ModelAttribute("newBook") @Valid Book book, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("bindingResult", bindingResult);
            return "books/newBook";
        }
        bookDAO.createBook(book);
        return "redirect:/books";
    }




    @GetMapping("/{id}/update")
    public String showEditFormBook(@PathVariable("id") int id, Model model) {
        Book book = bookDAO.showBookById(id);
        model.addAttribute("selectedBook", book);
        return "/books/edit";
    }


    @PostMapping("/{bookId}/update")
    public String updateBookPost(@ModelAttribute("selectedBook") @Valid Book book,
                                 BindingResult bindingResult,
                                 @PathVariable("bookId")int bookId,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            // Если есть ошибки валидации, возвращаем страницу редактирования с формой и сообщениями об ошибках
            model.addAttribute("selectedBook", book); // Передаем обратно объект книги с данными
            return "/books/edit"; // Возвращаем страницу с формой редактирования
        }
        bookDAO.updateBook(book, bookId);
        return "redirect:/books";
    }





    @GetMapping("/{id}")
    public String showSelectedBook(@PathVariable("id") int id, Model model){
        Book book = bookDAO.showBookById(id);
        model.addAttribute("selectedBook", book);
        model.addAttribute("personOwner", bookDAO.getOwnerOfTheBook(id)); // Передаем id выбранной книги
        return "/books/selectedBook";
    }


    @PostMapping("/{id}/delete")
    public String deleteBook(@PathVariable("id") int id){
        bookDAO.deleteBook(id);
        return "redirect:/books";
    }

    @GetMapping("/{bookId}/assign-book")
    public String showAssignBookForm(Model model, @PathVariable("bookId") int bookId) {
        // Получите список всех людей, которым можно назначить книгу
        List<Person> allPersons = bookDAO.getAllPeople();
        model.addAttribute("allPersons", allPersons);
        model.addAttribute("bookId", bookId); // Добавление bookId в модель
        return "books/assignBookForm";
    }



    @PostMapping("/{bookId}/assign-book")
    public String assignBook(@PathVariable("bookId") int bookId, @RequestParam("personId") int personId) {
        Person owner = bookDAO.getOwnerOfTheBook(bookId);
        if (owner != null) {
            // Книга уже занята, возвращаем пользователя с предупреждением
            return "redirect:/books/" + bookId + "?error=Book already assigned";
        }

        // Выполняем назначение книги конкретному человеку
        bookDAO.occupyBook(personId, bookId);
        return "redirect:/books";
    }

    @PostMapping("/{id}/freeSelectedBook")
    public String freeSelectedBook(@PathVariable("id") int bookId) {
        bookDAO.deleteSelectedBook(bookId);
        return "redirect:/books/" + bookId;
    }








}
