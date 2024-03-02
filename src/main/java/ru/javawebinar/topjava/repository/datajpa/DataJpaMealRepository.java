package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepository implements MealRepository {

    private static final Sort SORT_DESC = Sort.by(Sort.Direction.DESC, "dateTime");

    private final CrudMealRepository crudRepository;

    private final CrudUserRepository userRepository;

    @Autowired
    public DataJpaMealRepository(CrudMealRepository crudRepository, CrudUserRepository userRepository) {
        this.crudRepository = crudRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            User user = userRepository.getReferenceById(userId);
            meal.setUser(user);
            return crudRepository.save(meal);
        } else {
            Meal foundMeal = get(meal.id(), userId);
            if (foundMeal != null) {
                meal.setUser(foundMeal.getUser());
                return crudRepository.save(meal);
            }
        }
        return null;
    }

    @Override
    public boolean delete(int id, int userId) {
        return crudRepository.delete(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return crudRepository.findById(id, userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudRepository.getAll(userId, SORT_DESC);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return crudRepository.getAllBetween(startDateTime, endDateTime, userId, SORT_DESC);
    }
}
