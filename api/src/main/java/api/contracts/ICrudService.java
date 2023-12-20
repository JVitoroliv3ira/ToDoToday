package api.contracts;

import api.exceptions.BadRequestException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICrudService<E extends IEntity<T>, T, R extends JpaRepository<E, T>> {
    R getRepository();

    String getNotFoundMessage();

    default E create(E entity) {
        entity.setId(null);
        return this.getRepository().save(entity);
    }

    default E read(T id) throws BadRequestException {
        return this.getRepository()
                .findById(id)
                .orElseThrow(() -> new BadRequestException(this.getNotFoundMessage()));
    }

    default E update(E entity) {
        if (Boolean.FALSE.equals(this.getRepository().existsById(entity.getId()))) {
            throw new BadRequestException(this.getNotFoundMessage());
        }
        return this.getRepository().save(entity);
    }

    default void delete(T id) {
        if (Boolean.FALSE.equals(this.getRepository().existsById(id))) {
            throw new BadRequestException(this.getNotFoundMessage());
        }
        this.getRepository().deleteById(id);
    }
}
