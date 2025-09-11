package com.forwork.backend.api.order.repository;

import com.forwork.backend.api.mypage.dto.query.ArchiveSalesCountQueryDTO;
import com.forwork.backend.api.order.entity.OrderPassArchive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderPassArchiveRepository extends JpaRepository<OrderPassArchive, Long> {

    @Query("select new com.forwork.backend.api.mypage.dto.query.ArchiveSalesCountQueryDTO(opa.passArchive.passArchiveId, count(*)) from OrderPassArchive opa" +
            " join opa.order o" +
            " join Payment p on p.order.id=o.id" +
            " where opa.passArchive.passArchiveId in :archiveIds and p.paymentStatus='DONE'" +
            " group by opa.passArchive.passArchiveId")
    List<ArchiveSalesCountQueryDTO> findSalesCountsByArchiveIds(@Param("archiveIds") List<Long> archiveIds);


    @Query("select opa from OrderPassArchive opa" +
            " join fetch opa.order o" +
            " join fetch opa.passArchive a" +
            " where o.id in :orderIds")
    List<OrderPassArchive> findAllByOrderIds(@Param("orderIds") List<Long> orderIds);

    @Query("select distinct opa.passArchive.passArchiveId from OrderPassArchive opa" +
            " where opa.order.id in :orderIds")
    List<Long> findArchiveIdsByOrderIds(@Param("orderIds") List<Long> orderIds);
}
