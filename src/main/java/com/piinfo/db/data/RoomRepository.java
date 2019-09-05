package com.piinfo.db.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RoomRepository extends JpaRepository<Room,Long> {
    Room findById(long id);
    Set<Room> findAllByName(String name);
    Set<Room> findByNo(long no);

}
