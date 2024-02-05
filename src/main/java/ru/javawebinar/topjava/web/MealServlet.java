package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.exception.ExceptionUtils;
import ru.javawebinar.topjava.exception.MealNotFoundException;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.impl.MealRepositoryImpl;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private static final String MEALS_LIST_PAGE = "/meals.jsp";

    private static final String CREATE_EDIT_PAGE = "/mealCreate.jsp";

    private static final String EDIT_ACTION = "edit";

    private static final String DELETE_ACTION = "delete";

    private static final String ACTION = "action";

    public static final String ID_PARAM = "id";

    private final Map<String, String> actions = new HashMap<>();

    private final MealRepository mealRepository = new MealRepositoryImpl();

    public MealServlet() {
        actions.put(EDIT_ACTION, CREATE_EDIT_PAGE);
        actions.put(DELETE_ACTION, MEALS_LIST_PAGE);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.debug("Process GET method to /meals");
        String forward = "";
        String action = request.getParameter(ACTION);
        if (action == null || !actions.containsKey(action)) {
            log.debug("Getting meals list");
            List<MealTo> mealTos = getMealTos();
            setUpRequestWithMealsList(request, mealTos);
            forward = MEALS_LIST_PAGE;
        } else if (action.equalsIgnoreCase(EDIT_ACTION)) {
            log.debug("Editing meals");
            int id = Integer.parseInt(request.getParameter(ID_PARAM));
            Optional<Meal> mealById = mealRepository.findById(id);
            Meal meal = mealById.orElseThrow(() -> {
                String exceptionMessage = String.format(ExceptionUtils.MEAL_NOT_FOUND, id);
                log.warn(exceptionMessage);
                return new MealNotFoundException(exceptionMessage);
            });
            forward = CREATE_EDIT_PAGE;
            request.setAttribute("meal", meal);
        }
        request.getRequestDispatcher(forward).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Processing POST method to /meals");
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter(ACTION);
        if (action == null || !actions.containsKey(action)) {
            log.debug("Updating/saving meal");
            String description = request.getParameter("description");
            int calories = Integer.parseInt(request.getParameter("calories"));
            LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
            Meal meal = new Meal(dateTime, description, calories);
            String id = request.getParameter(ID_PARAM);
            if (!id.isEmpty()) {
                meal.setId(Integer.valueOf(id));
            }
            mealRepository.save(meal);
        } else if (action.equalsIgnoreCase(DELETE_ACTION)) {
            log.debug("Deleting meal");
            int id = Integer.parseInt(request.getParameter(ID_PARAM));
            mealRepository.deleteById(id);
        }
        response.sendRedirect(request.getContextPath() + "/meals");
    }

    private List<MealTo> getMealTos() {
        List<Meal> meals = mealRepository.findAll();
        return MealsUtil.filteredByStreams(meals, LocalTime.MIN, LocalTime.MAX, MealsUtil.CALORIES_PER_DAY);
    }

    private static void setUpRequestWithMealsList(HttpServletRequest request, List<MealTo> mealTos) {
        request.setAttribute("meals", mealTos);
        request.setAttribute("dateTimeFormatter", TimeUtil.DATETIME_FMT);
    }

}
