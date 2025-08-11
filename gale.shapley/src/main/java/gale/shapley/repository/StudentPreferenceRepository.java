package gale.shapley.repository;

import gale.shapley.model.StudentPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentPreferenceRepository extends JpaRepository<StudentPreference, Long> {
}
