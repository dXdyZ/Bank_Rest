package com.example.bank_rest_test_task.repository;

import com.example.bank_rest_test_task.entity.BlockRequestStatus;
import com.example.bank_rest_test_task.entity.CardBlockRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;

@Repository
public interface CardBlockRequestRepository extends JpaRepository<CardBlockRequest, Long> {

    @EntityGraph(attributePaths = {"card", "requester", "processedBy"})
    @Query("select c from CardBlockRequest c " +
            "where (:blockRequestStatus is null or c.blockRequestStatus = :blockRequestStatus) " +
            "and (:requesterId is null or c.requester.id = :requesterId) " +
            "and (:searchHash is null or c.card.searchHash = :searchHash) " +
            "and (:createStartDate is null or c.createAt >= :createStartDate) " +
            "and (:createEndDate is null or c.createAt <= :createEndDate)")
    Page<CardBlockRequest> findFilterCardBlockRequest(@Param("blockRequestStatus") BlockRequestStatus blockRequestStatus,
                                                      @Param("requesterId") Long requesterId,
                                                      @Param("searchHash") String searchHash,
                                                      @Param("createStartDate") OffsetDateTime createStartDate,
                                                      @Param("createEndDate") OffsetDateTime createEndDate,
                                                      Pageable pageable);
    Page<CardBlockRequest> findAllByProcessedBy_Id(Long processedById, Pageable pageable);
}

