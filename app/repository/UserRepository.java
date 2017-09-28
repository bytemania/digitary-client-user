package repository;

import com.google.inject.ImplementedBy;
import models.User;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

@ImplementedBy(JpaUserRepository.class)
public interface UserRepository {

    CompletionStage<User> create(User user);

    CompletionStage<Optional<User>> get(int id);

    CompletionStage<Optional<User>> update(User user);

    CompletionStage<Optional<User>> delete(int id);

    CompletionStage<Stream<User>> list();
}
