package li.kevin.electronicStore.fakeRepositories;

import li.kevin.electronicStore.models.Discount;
import li.kevin.electronicStore.repositories.DiscountRepository;
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

public class FakeDiscountRepository implements DiscountRepository {

    private final Set<Discount> discounts = new HashSet<>();

    private AtomicInteger idSequence = new AtomicInteger();

    @Override
    public void flush() {

    }

    @Override
    public <S extends Discount> S saveAndFlush(S entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Discount> List<S> saveAllAndFlush(Iterable<S> entities) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAllInBatch(Iterable<Discount> entities) {
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
    public Discount getOne(Integer i) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Discount getById(Integer i) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Discount getReferenceById(Integer i) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Discount> Optional<S> findOne(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Discount> List<S> findAll(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Discount> List<S> findAll(Example<S> example, Sort sort) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Discount> Page<S> findAll(Example<S> example, Pageable pageable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Discount> long count(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Discount> boolean exists(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Discount, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Discount> S save(S entity) {
        boolean exist = discounts.remove(entity);
        if (!exist) {
            entity.setId(idSequence.incrementAndGet());
        }
        discounts.add(entity);
        return entity;
    }

    @Override
    public <S extends Discount> List<S> saveAll(Iterable<S> entities) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Discount> findById(Integer i) {
        return discounts.stream().filter(it -> it.getId() == i).findFirst();
    }

    @Override
    public boolean existsById(Integer id) {
        return discounts.stream().anyMatch(it -> it.getId() == id);
    }

    @Override
    public List<Discount> findAll() {
        return discounts.stream().toList();
    }

    @Override
    public List<Discount> findAllById(Iterable<Integer> ids) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long count() {
        return discounts.size();
    }

    @Override
    public void deleteById(Integer id) {
        discounts.stream().filter(it -> it.getId() == id).findFirst().ifPresent(discounts::remove);
    }

    @Override
    public void delete(Discount entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> ids) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll(Iterable<? extends Discount> entities) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll() {
        discounts.clear();
    }

    @Override
    public List<Discount> findAll(Sort sort) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Page<Discount> findAll(Pageable pageable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Discount> findByProductName(String productName) {
        return discounts.stream().filter(it -> it.getProductName().equals(productName)).collect(Collectors.toList());
    }
}
