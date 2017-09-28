package repository;

import com.google.inject.Inject;
import models.SyncDetail;
import play.db.jpa.JPAApi;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;


public class JpaSyncDetailRepository implements SyncDetailRepository {

    private final JPAApi jpaApi;

    @Inject
    public JpaSyncDetailRepository(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    @Override
    public void setStamp() {
        Date now = new Date();
        Timestamp ts = new Timestamp(now.getTime());
        SyncDetail syncDetail= new SyncDetail(ts);
        wrap(em -> insert(em, syncDetail));
    }

    @Override
    public Optional<Timestamp> getLast() {
        return wrap(em -> getLast(em));
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    private SyncDetail insert(EntityManager em, SyncDetail syncDetail) {
        em.persist(syncDetail);
        return syncDetail;
    }

    public Optional<Timestamp> getLast(EntityManager em) {
        try {
            Timestamp ts = (Timestamp) em.createQuery("select max(s.syncStamp) from SyncDetail s").getSingleResult();
            if (ts != null) {
                return Optional.of(ts);
            } else {
                return Optional.empty();
            }
        } catch (NoResultException nre) {
            return Optional.empty();
        }
    }
}
