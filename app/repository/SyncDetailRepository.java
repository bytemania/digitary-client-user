package repository;

import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import models.SyncDetail;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

@ImplementedBy(JpaSyncDetailRepository.class)
public interface SyncDetailRepository {

    void setStamp() ;
    Optional<Timestamp> getLast();
}
