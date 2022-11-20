package com.example.jdbc.controller;

//import com.example.jdbc.dto.KonfigurasiMitra;
import com.example.jdbc.dto.KonfigurasiMitra;
import com.example.jdbc.dto.ProgramDetailDTO;
import com.example.jdbc.dto.ProgramInsert;
import com.example.jdbc.service.ProgramDetailService;
import com.example.jdbc.service.ProgramInsertService;
import com.example.jdbc.service.TestTransactionService;
import com.example.jdbc.test.ServicePenampung;
import com.example.jdbc.transactional.TestTransactionalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class TestJdbcController {

    private static Logger log = LoggerFactory.getLogger(TestJdbcController.class);
    @Autowired
    @Qualifier("jdbcTemplate")
    JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("testDbJdbcTemplate")
    JdbcTemplate jdbcTemplateTestDb;
    
    @Autowired
    private ProgramInsertService programInsertService;

    @Autowired
    private ProgramDetailService programDetailService;

    @Autowired
    private TestTransactionService testTransactionService;

    @Autowired
    private ServicePenampung service;

    @Autowired
    private TestTransactionalService testTransactionalService;


//    public TestJdbcController(@Qualifier("jdbcTemplate") JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }

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
        Map<String, Object> logMap = new LinkedHashMap<>();
        logMap.put("Request", programInsert);
        logMap.put("Response", o);
        log.info("{}", logMap);
        return ResponseEntity.ok().body(o);
    }

    @GetMapping("/testData")
    public ResponseEntity testData() {

        Optional<TestTransactionalService.Tutorial> tutorial = testTransactionalService.testData();

        log.info("tutorial : {}", tutorial.isPresent()); //false

        return ResponseEntity.ok().body("Oke!!");
    }

    @GetMapping("/testTransactional2")
    public ResponseEntity programInsert() {

     testTransactionalService.testTransaction();

        return ResponseEntity.ok().body("Oke!!");
    }

    @PostMapping("/testTransactions")
    public ResponseEntity testTransaction(@RequestBody ProgramInsert programInsert) {

        Object o = testTransactionService.saveData(programInsert);

        return ResponseEntity.ok().body(o);
    }

    @PostMapping("/testTransactions2")
    public ResponseEntity testTransaction2(@RequestBody ProgramInsert programInsert) {

        Object o = service.doIt(programInsert);

        return ResponseEntity.ok().body(o);
    }

    @PostMapping("/programDetail")
    public ResponseEntity programDetail(@RequestBody ProgramDetailDTO programDetailDTO, HttpServletRequest servletRequest) {

        Object o = programDetailService.programDetail(programDetailDTO, servletRequest);

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

    @PostMapping ("/testAsync")
    public ResponseEntity testAsync(@RequestBody ProgramDetailDTO programDetailDTO) throws ExecutionException, InterruptedException {
        CompletableFuture<Boolean> queryMitra = programDetailService.queryMitra(programDetailDTO.getFIDProgram());
        CompletableFuture<Boolean> exist = programDetailService.isExist(programDetailDTO.getFIDProgram());
        CompletableFuture<KonfigurasiMitra> data = programDetailService.getData(programDetailDTO.getFIDProgram());


        CompletableFuture.allOf(queryMitra, exist, data).join();

        System.out.println("queryMitra -->  " + Thread.currentThread().getName() + " " + queryMitra.get());
        System.out.println("exist --> " + Thread.currentThread().getName() + " " + exist.get());
        System.out.println("exist --> " + Thread.currentThread().getName() + " " + data.get());

        return ResponseEntity.ok().body("oke!");
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

    @GetMapping("/cobaaja")
    public ResponseEntity<String> test4(){
        return ResponseEntity.ok().body("ja");
    }
}
