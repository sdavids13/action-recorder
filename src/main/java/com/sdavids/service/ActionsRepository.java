package com.sdavids.service;

import com.sdavids.domain.Action;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ActionsRepository extends JpaRepository<Action, Long> {

    List<Action> findByUserAndObjectUri(String user, String objectUri);

    Page<Action> findByUserAndObjectUri(String user, String objectUri, Pageable pageable);

    Page<Action> findByUser(String user, Pageable pageable);

    @Modifying
    @Transactional
    @Query("delete from Action a where a.id = :id and a.user = :username")
    void delete(@Param("id") Long id, @Param("username") String username);

    default Action findOne(Long id, String user) throws AccessDeniedException, EntityNotFoundException {
        Action action = findOne(id);
        if (action == null) {
            throw new EntityNotFoundException("Action doesn't exist.");
        } else if (action.getUser().equals(user) == false) {
            throw new AccessDeniedException("You can only access actions you own.");
        }

        return action;
    }
}
