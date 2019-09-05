package com.piinfo.db.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Furniture_SettingsRepository extends JpaRepository<Furniture_Settings, Long> {
	Furniture_Settings findByType (String Type);
}
