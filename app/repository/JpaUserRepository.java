package repository;

import com.google.inject.Inject;
import models.User;
import play.db.jpa.JPAApi;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class JpaUserRepository implements UserRepository {

    private final JPAApi jpaApi;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public JpaUserRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext) {
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }

    @Override
    public CompletionStage<Integer> sync(List<User> users) {
        return supplyAsync(() -> wrap(em -> sync(em, users)), executionContext);
    }

    @Override
    public CompletionStage<Optional<User>> create(User user) {
        return supplyAsync(() -> wrap(em -> insert(em, user)), executionContext);
    }

    @Override
    public CompletionStage<Optional<User>> get(Integer id) {
        return supplyAsync(() -> wrap(em -> get(em, id)), executionContext);
    }

    @Override
    public CompletionStage<Optional<User>> update(User user) {
        return supplyAsync(() -> wrap(em -> update(em, user)), executionContext);
    }

    @Override
    public CompletionStage<Optional<User>> delete(Integer id) {
        return supplyAsync(() -> wrap(em -> delete(em, id)), executionContext);
    }

    @Override
    public CompletionStage<Stream<User>> list() {
        return supplyAsync(() -> wrap(em -> list(em)), executionContext);
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    private Integer sync(EntityManager em, List<User> users) {
        int count=0;
        for (User user : users) {
            em.merge(user);
            count++;
        }
        return count;
    }

    private Optional<User> get(EntityManager em, Integer id) {
        User user = em.find(User.class, id);
        if (user != null) {
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    private Optional<User> insert(EntityManager em, User user) {
        Optional<User> userOptional = get(em, user.getId());
        if (userOptional.isPresent()) {
            return Optional.empty();
        } else {
            em.persist(user);
            return Optional.of(user);
        }
    }

    private Optional<User> update(EntityManager em, User user) {
        if (get(em, user.getId()).isPresent()) {
            //too lazy to do it in a proper way
            em.merge(user);
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    private Optional<User> delete(EntityManager em, Integer id) {
        Optional<User> user = get(em, id);
        if (user.isPresent()) {
            em.remove(user.get());
            return user;
        } else {
            return Optional.empty();
        }
    }

    private Stream<User> list(EntityManager em) {
        List<User> persons = em.createQuery("select u from User u", User.class).getResultList();
        return persons.stream();
    }
}
