package cw.gasbygas.repository;

import cw.gasbygas.domain.RequestStatus;
import cw.gasbygas.model.GasRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GasRequestRepository extends JpaRepository<GasRequest, Integer> {
    List<GasRequest> findByUserId(int userId);
    List<GasRequest> findByOutletId(int outletId);
    List<GasRequest> findByStatus(RequestStatus status);
    List<GasRequest> findByOrderNo(long orderNo);

    @Query("SELECT gr FROM GasRequest gr WHERE gr.user.id = :userId AND gr.status = :status")
    List<GasRequest> findByUserIdAndStatus(int userId, RequestStatus status);

    @Query("SELECT gr FROM GasRequest gr WHERE gr.outlet.id = :outletId AND gr.status = :status")
    List<GasRequest> findByOutletIdAndStatus(int outletId, RequestStatus status);

    @Query("SELECT gr FROM GasRequest gr WHERE gr.user.type = 'CUSTOMER' AND gr.status IN :statuses")
    List<GasRequest> findDomesticRequestsByStatuses(List<RequestStatus> statuses);

    @Query("SELECT gr FROM GasRequest gr WHERE gr.outlet.id = :outletId AND gr.status = :status")
    List<GasRequest> findOutletRequestsByStatus(int outletId, RequestStatus status);

    @Query("SELECT gr FROM GasRequest gr WHERE gr.outlet.id = :outletId AND gr.status IN :statuses")
    List<GasRequest> findByOutletIdAndStatusIn(int outletId, List<RequestStatus> statuses);
}
