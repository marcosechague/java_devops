package aws.mitocode.spring.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import aws.mitocode.spring.model.Problema;

@Repository
public interface IProblemaDao extends JpaRepository<Problema, Integer> {

}
