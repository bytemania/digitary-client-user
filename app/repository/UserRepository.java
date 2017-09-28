package repository;

import com.google.inject.ImplementedBy;
import models.User;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

@ImplementedBy(JpaUserRepository.class)
public interface UserRepository {

    CompletionStage<Integer> sync(List<User> users);

    CompletionStage<Optional<User>> create(User user);

    CompletionStage<Optional<User>> get(Integer id);

    CompletionStage<Optional<User>> update(User user);

    CompletionStage<Optional<User>> delete(Integer id);

    CompletionStage<Stream<User>> list();
}
