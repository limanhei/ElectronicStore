package li.kevin.electronicStore.fakeRepositories;

import li.kevin.electronicStore.entities.Product;
import li.kevin.electronicStore.repositories.ProductRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.*;
import java.util.function.Function;

public class FakeProductRepository implements ProductRepository {

    private final Set<Product> products = new HashSet<>();

    @Override
    public void flush() {

    }

    @Override
    public <S extends Product> S saveAndFlush(S entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Product> List<S> saveAllAndFlush(Iterable<S> entities) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAllInBatch(Iterable<Product> entities) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<String> strings) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAllInBatch() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Product getOne(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Product getById(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Product getReferenceById(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Product> Optional<S> findOne(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Product> List<S> findAll(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Product> List<S> findAll(Example<S> example, Sort sort) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Product> Page<S> findAll(Example<S> example, Pageable pageable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Product> long count(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Product> boolean exists(Example<S> example) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Product, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Product> S save(S entity) {
        products.remove(entity);
        products.add(entity);
        return entity;
    }

    @Override
    public <S extends Product> List<S> saveAll(Iterable<S> entities) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Product> findById(String s) {
        return products.stream().filter(it -> it.getName().equals(s)).findFirst();
    }

    @Override
    public boolean existsById(String name) {
        return products.stream().anyMatch(it -> it.getName().equals(name));
    }

    @Override
    public List<Product> findAll() {
        return products.stream().toList();
    }

    @Override
    public List<Product> findAllById(Iterable<String> strings) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long count() {
        return products.size();
    }

    @Override
    public void deleteById(String name) {
        products.stream().filter(it -> it.getName().equals(name)).findFirst().ifPresent(products::remove);
    }

    @Override
    public void delete(Product entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll(Iterable<? extends Product> entities) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll() {
        products.clear();
    }

    @Override
    public List<Product> findAll(Sort sort) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        throw new UnsupportedOperationException();
    }
}
