package com.micro.usuarios.models.dao;

import com.micro.usuarios.models.entity.Foo;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path="foo")
public interface IFooDao extends PagingAndSortingRepository<Foo,Long> {

}
