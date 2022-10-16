package com.example.jdbc.repository;

import com.example.jdbc.entity.LogInsert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;


@Repository
public interface TestRepository extends JpaRepository<LogInsert, Integer> {
    @Query(value = "select count(*) from log_insert where FIDProgram = ?1", nativeQuery = true)
    Integer existsByFIDProgram(String fid);

    @Transactional
    @Modifying
    @Query(value = "insert into log_insert (FIDProgram, NamaProgram, TanggalMulaiProgram, TanggalAwalRealisasi) " +
            " values (?, ?, ?, ?) ", nativeQuery = true)
    int saveLog(String fid, String nama, Date tanggal, Date tanggalAwal);

}
