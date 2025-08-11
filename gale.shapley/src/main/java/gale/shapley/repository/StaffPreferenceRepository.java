package gale.shapley.repository;

import gale.shapley.model.StaffPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffPreferenceRepository extends JpaRepository<StaffPreference, Long> {
}
