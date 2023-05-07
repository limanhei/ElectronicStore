package li.kevin.electronicStore.fakeRepositories;

import li.kevin.electronicStore.models.BasketItem;
import li.kevin.electronicStore.repositories.BasketItemRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FakeBasketItemRepository implements BasketItemRepository {

    private final Set<BasketItem> basketItems = new HashSet<>();

    private AtomicInteger idSequence = new AtomicInteger();

    @Override
    public void flush() {

    }

    @Override
    public <S extends BasketItem> S saveAndFlush(S entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends BasketItem> List<S> saveAllAndFlush(Iterable<S> entities) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAllInBatch(Iterable<BasketItem> entities) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Integer> ids) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAllInBatch() {
        throw new UnsupportedOperationException();
    }

    @Override
    public BasketItem getOne(Integer i) {
        throw new UnsupportedOperationException();
    }

    @Override
    public BasketItem getById(Integer i) {
        throw new UnsupportedOperationException();
    }

    @Override
    public BasketItem getReferenceById(Integer i) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends BasketItem> Optional<S> findOne(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends BasketItem> List<S> findAll(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends BasketItem> List<S> findAll(Example<S> example, Sort sort) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends BasketItem> Page<S> findAll(Example<S> example, Pageable pageable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends BasketItem> long count(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends BasketItem> boolean exists(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends BasketItem, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends BasketItem> S save(S entity) {
        boolean exist = basketItems.remove(entity);
        if (!exist) {
            entity.setId(idSequence.incrementAndGet());
        }
        basketItems.add(entity);
        return entity;
    }

    @Override
    public <S extends BasketItem> List<S> saveAll(Iterable<S> entities) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<BasketItem> findById(Integer i) {
        return basketItems.stream().filter(it -> it.getId() == i).findFirst();
    }

    @Override
    public boolean existsById(Integer id) {
        return basketItems.stream().anyMatch(it -> it.getId() == id);
    }

    @Override
    public List<BasketItem> findAll() {
        return basketItems.stream().toList();
    }

    @Override
    public List<BasketItem> findAllById(Iterable<Integer> ids) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long count() {
        return basketItems.size();
    }

    @Override
    public void deleteById(Integer id) {
        basketItems.stream().filter(it -> it.getId() == id).findFirst().ifPresent(basketItems::remove);
    }

    @Override
    public void delete(BasketItem entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> ids) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll(Iterable<? extends BasketItem> entities) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll() {
        basketItems.clear();
    }

    @Override
    public List<BasketItem> findAll(Sort sort) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Page<BasketItem> findAll(Pageable pageable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<BasketItem> findByClientName(String clientName) {
        return basketItems.stream().filter(it -> it.getClientName().equals(clientName)).collect(Collectors.toList());
    }
}
