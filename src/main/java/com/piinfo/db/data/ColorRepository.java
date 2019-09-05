package com.piinfo.db.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * Make db availabale to code
 *
**/
@Repository
public interface ColorRepository extends JpaRepository<Color,Long> {
	/**
	 *
	 * @param Color Value of the color you want
	 * @return Color Object
	 */
	Color findByColor (String Color);
}
