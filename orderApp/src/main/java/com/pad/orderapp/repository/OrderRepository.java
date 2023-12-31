package com.pad.orderapp.repository;

import com.pad.orderapp.model.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

//TODO - keycloak does not allow external URL as issuer when comparing it with internal url
    @Query("SELECT o FROM OrderEntity o WHERE (:createdBefore is null OR o.created <= :createdBefore) AND (:createdAfter is null OR o.created >= :createdAfter) AND (:status is null OR o.status = :status) AND (:isPayed is null OR o.isPayed = :isPayed) AND (:user != 'testName' OR o.user = :user)")
    List<OrderEntity> findByQueryParams(@Param("createdBefore") String createdBefore, @Param("createdAfter") String createdAfter, @Param("status") String status, @Param("isPayed") boolean isPayed, @Param("user") String user);

}
