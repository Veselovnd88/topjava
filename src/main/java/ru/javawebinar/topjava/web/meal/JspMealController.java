package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Controller
@RequestMapping("/meals")
public class JspMealController extends AbstractMealController {

    private static final String MEALS_ATTR = "meals";

    private static final String MEALS_PAGE = "meals";

    private static final String MEAL_FORM_PAGE = "mealForm";

    private static final String REDIRECT_MEALS_PAGE = "redirect:/" + MEALS_PAGE;

    @Autowired
    protected JspMealController(MealService mealService) {
        super(mealService);
    }

    @GetMapping
    public String get(Model model) {
        model.addAttribute(MEALS_ATTR, getAll());
        return MEALS_PAGE;
    }

    @GetMapping("/delete")
    public String deleteById(@RequestParam("id") int id) {
        delete(id);
        return REDIRECT_MEALS_PAGE;
    }

    @GetMapping("/filter")
    public String getFiltered(Model model, @RequestParam(value = "startDate", required = false) String startDate,
                              @RequestParam(value = "endDate", required = false) String endDate,
                              @RequestParam(value = "startTime", required = false) String startTime,
                              @RequestParam(value = "endTime", required = false) String endTime) {
        model.addAttribute(MEALS_ATTR, getBetween(DateTimeUtil.parseLocalDate(startDate), DateTimeUtil.parseLocalTime(startTime),
                DateTimeUtil.parseLocalDate(endDate), DateTimeUtil.parseLocalTime(endTime)));
        return MEALS_PAGE;
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("meal", new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000));
        return MEAL_FORM_PAGE;
    }

    @GetMapping("/update")
    public String update(Model model, @RequestParam("id") Integer id) {
        model.addAttribute("meal", get(id));
        return MEAL_FORM_PAGE;
    }

    @PostMapping
    public String save(@RequestParam(value = "dateTime") String localDateTime,
                       @RequestParam(value = "description") String description,
                       @RequestParam(value = "calories") Integer calories,
                       @RequestParam(value = "id", required = false) Integer id) {
        Meal meal = new Meal(LocalDateTime.parse(localDateTime), description, calories);
        if (id != null) {
            update(meal, id);
        } else {
            create(meal);
        }
        return REDIRECT_MEALS_PAGE;
    }
}
