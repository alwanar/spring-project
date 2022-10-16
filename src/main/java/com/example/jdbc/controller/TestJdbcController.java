package com.example.jdbc.controller;

//import com.example.jdbc.dto.KonfigurasiMitra;
import com.example.jdbc.dto.KonfigurasiMitra;
import com.example.jdbc.dto.ProgramInsert;
import com.example.jdbc.service.ProgramInsertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestJdbcController {
    JdbcTemplate jdbcTemplate;
    
    @Autowired
    private ProgramInsertService programInsertService;

    public TestJdbcController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private String sql = "select flag_dasar, nomor_pks_sp3, tgl_awal_pks_sp3, tgl_akhir_pks_sp3, coalesce(c.cov_macet,0) as cov_macet, " +
            "COALESCE(FIDSkemaPerhitunganBunga,1) AS FIDSkemaPerhitunganBunga, " +
            " b.id_dd_bank, b.id_dc_produk, b.keterangan_produk, b.id_dc_peruntukan_kredit, b.flag_syariah, b.plafon_atas as PlafonMaksimal, " +
            " b.plafon_bawah as PlafonMinimal, c.jangka_waktu_atas as JangkaWaktuMaksimal, c.jangka_waktu_bawah as JangkaWaktuMinimal " +
            "from opdd_konfigurasi_ijp a with(nolock) JOIN dc_produk_mitra b with(nolock) ON a.id_opdd_konfigurasi_ijp = b.id_opdd_konfigurasi_ijp " +
            "JOIN opdd_master_tarif c with(nolock) ON a.id_opdd_konfigurasi_ijp = c.id_opdd_konfigurasi_ijp " +
            "AND COALESCE(c.id_dc_peruntukan_kredit,0) IN (0,b.id_dc_peruntukan_kredit) " +
            "INNER JOIN dc_jns_pengajuan_mitra d ON a.id_opdd_konfigurasi_ijp = d.id_opdd_konfigurasi_ijp AND b.kode_program_mitra = d.FIDProgram " +
            "LEFT JOIN dd_jenis_lain jns on jns.id_dd_jenis_lain = b.id_dd_jenis_lain " +
            "WHERE a.id_dd_bank = 15 and GETDATE() BETWEEN tgl_awal_pks_sp3 AND tgl_akhir_pks_sp3 AND kode_program_mitra=? ";

//    @Autowired
//    public TestJdbcController(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }

    @PostMapping("/programInsert")
    public ResponseEntity programInsert(@RequestBody ProgramInsert programInsert) {

        Object o = programInsertService.programInsert(programInsert);

        return ResponseEntity.ok().body(o);
    }

    @GetMapping ("/testJdbc")
    public ResponseEntity test() {
        Object[] objects = {42};
        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("fid", 42);
        System.out.println("obj " +objects.toString());
        KonfigurasiMitra sqlRowSet = jdbcTemplate.queryForObject(sql,
                new BeanPropertyRowMapper<>(KonfigurasiMitra.class), 42);

        return ResponseEntity.ok().body(sqlRowSet);
    }

    @GetMapping("/dcproduk")
    public ResponseEntity test2() {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select * from dc_produk_mitra");

        while (sqlRowSet.next()){
            System.out.println(sqlRowSet.getString(1));
            System.out.println(sqlRowSet.getString(2));
            System.out.println(sqlRowSet.getString(3));

        }
        return ResponseEntity.ok().body(sqlRowSet);
    }

    @GetMapping("/test")
    public ResponseEntity getKonfigurasi(@RequestParam String fid, @RequestParam(required = false) String namaProduk){
        System.out.println(namaProduk == null);

        Object[] objects = {fid};

        KonfigurasiMitra mitra = null;
        if (namaProduk != null) {
            String newSql = sql + " and b.NamaProduk=?";
            System.out.println(newSql);
            Object[] objectsBaru = new Object[]{fid, namaProduk};
            mitra = jdbcTemplate.queryForObject( newSql,
                    (rs, rowNum) -> {
                        KonfigurasiMitra konfigurasiMitra = new KonfigurasiMitra();
                        konfigurasiMitra.setFlagDasar(rs.getString(1));
                        konfigurasiMitra.setNomorPksSp3(rs.getString(2));
                        konfigurasiMitra.setTglAwalPksSp3(rs.getDate(3));
                        konfigurasiMitra.setTglAkhirPksSp3(rs.getDate(4));
                        konfigurasiMitra.setCovMacet(rs.getInt(5));
                        konfigurasiMitra.setFIDSkemaPerhitunganBunga(rs.getInt(6));
                        konfigurasiMitra.setId_DD_Bank(rs.getInt(7));
                        konfigurasiMitra.setIdDcProduk(rs.getInt(8));
                        konfigurasiMitra.setKeteranganProduk(rs.getString(9));
                        konfigurasiMitra.setPlafonMaksimal(rs.getInt(11));
                        konfigurasiMitra.setPlafonMaksimal(rs.getInt(12));
                        konfigurasiMitra.setJangkaWaktuMaksimal(rs.getInt(13));
                        konfigurasiMitra.setJangkaWaktuMinimal(rs.getInt(14));

//                    System.out.println(rs.getString(rs.getInt(6)));
                        return konfigurasiMitra;
                    } , objectsBaru);
        }

        mitra = jdbcTemplate.queryForObject( sql,
                (rs, rowNum) -> {
                    KonfigurasiMitra konfigurasiMitra = new KonfigurasiMitra();
                    konfigurasiMitra.setFlagDasar(rs.getString(1));
                    konfigurasiMitra.setNomorPksSp3(rs.getString(2));
                    konfigurasiMitra.setTglAwalPksSp3(rs.getDate(3));
                    konfigurasiMitra.setTglAkhirPksSp3(rs.getDate(4));
                    konfigurasiMitra.setCovMacet(rs.getInt(5));
                    konfigurasiMitra.setFIDSkemaPerhitunganBunga(rs.getInt(6));
                    konfigurasiMitra.setId_DD_Bank(rs.getInt(7));
                    konfigurasiMitra.setIdDcProduk(rs.getInt(8));
                    konfigurasiMitra.setKeteranganProduk(rs.getString(9));
                    konfigurasiMitra.setPlafonMaksimal(rs.getInt(11));
                    konfigurasiMitra.setPlafonMinimal(rs.getInt(12));
                    konfigurasiMitra.setJangkaWaktuMaksimal(rs.getInt(13));
                    konfigurasiMitra.setJangkaWaktuMinimal(rs.getInt(14));

//                    System.out.println(rs.getString(rs.getInt(6)));
                    return konfigurasiMitra;
                } , objects); //

        return ResponseEntity.ok().body(mitra);
    }
}
