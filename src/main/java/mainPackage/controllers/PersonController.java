package mainPackage.controllers;


import mainPackage.DAOS.BookDAO;
import mainPackage.DAOS.PersonDAO;
import mainPackage.models.Person;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/people")
public class PersonController {
    private final BookDAO bookDAO;
    private final PersonDAO personDAO;

    public PersonController(BookDAO bookDAO, PersonDAO personDAO) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
    }

    @GetMapping
    public String show(Model model){
        List<Person> people = personDAO.show();
        model.addAttribute("AllPeople", people);
        return "people/show";
    }


    @GetMapping("/{id}")
    public String showPerson(@PathVariable("id") int id, Model model){
        Person person = personDAO.showPerson(id);
        System.out.println("Person found: " + person); // Отладочное сообщение
        model.addAttribute("onePersonShow", person);
        model.addAttribute("takenBooks", personDAO.showTakenBooks(id));
        return "people/showPerson";
    }



    @GetMapping("/new")
    public String newPerson(Model model){
        model.addAttribute("person", new Person());
        return "people/newPerson";
    }


    @PostMapping("/createPerson")
    public String newPersonPost(@ModelAttribute("person") @Valid Person person,
                                BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return "people/newPerson";

    personDAO.save(person);
    return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable("id") int id, Model model) {
        Person person = personDAO.showPerson(id);
        model.addAttribute("person", person);
        return "people/editPerson";
    }


    @PostMapping("/{id}/edit")
    public String updatePerson(@ModelAttribute("person") @Valid Person person,
            BindingResult bindingResult, @PathVariable("id") int id){

        if(bindingResult.hasErrors())
            return "people/editPerson";
        System.out.println("id " + id);
        System.out.println("person " + person);

        personDAO.updatePerson(id, person);
        return "redirect:/people";
    }

    @PostMapping("/{id}/delete")
    public String deletePerson(@PathVariable("id")int id){
        personDAO.deletePerson(id);

        return "redirect:/people";
    }

    @PostMapping("/{id}/freeAllBooks")
    public String freeAllBooks(@PathVariable("id") int personId) {
        bookDAO.makeBookAvailable(personId);
        return "redirect:/people/" + personId;
    }








}
