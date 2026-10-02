package com.notely.Repository;

import com.notely.entity.Pad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PadRepository extends JpaRepository<Pad, String> {
}
