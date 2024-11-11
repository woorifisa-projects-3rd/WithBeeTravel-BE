package withbeetravel.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import withbeetravel.domain.History;

public interface HistoryRepository extends JpaRepository<History,Long> {
}
