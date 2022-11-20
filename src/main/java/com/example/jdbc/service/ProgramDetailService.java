package com.example.jdbc.service;

import com.example.jdbc.dto.KonfigurasiMitra;
import com.example.jdbc.dto.ProgramDetailDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class ProgramDetailService {

    private static Logger log = LoggerFactory.getLogger(ProgramDetailService.class);


    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

//    public ProgramDetailService(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }

    public Object programDetail(ProgramDetailDTO programDetailDTO, HttpServletRequest servletRequest) {

        Map<String, Object> responseData = new LinkedHashMap<>();


        Map<String, Object> serverLog = new LinkedHashMap<>();

        serverLog.put("RequestBody", programDetailDTO);
        serverLog.put("startTime", LocalDateTime.now());


        if (programDetailDTO.getFIDProgram() == null) {
            System.out.println("masuk");
            responseData.put("ResponseCode", "01");
            responseData.put("ResponseDescription", "Failed");
            responseData.put("ResponseException", "field request FIDProgam tidak ada");

            serverLog.put("ResponseBody", responseData);
            serverLog.put("endTime", LocalDateTime.now());
            serverLog.put("ClientIp", servletRequest.getRemoteAddr());

            log.info("server : {}", serverLog);


            return responseData;
        } else {

            Map<String, Object> validateFIDProgram = this.validateFIDProgram(responseData, programDetailDTO.getFIDProgram());

            if (validateFIDProgram.get("ResponseCode").equals("01")) {
                serverLog.put("ResponseBody", responseData);
                serverLog.put("endTime", LocalDateTime.now());
                serverLog.put("ClientIp", servletRequest.getRemoteAddr());
                serverLog.put("ClientIp2", servletRequest.getRemoteUser());
                serverLog.put("ClientIp3", servletRequest.getRemoteHost());

                log.info("server : {}", serverLog);
                return responseData;
            } else {

                String fidProgram = programDetailDTO.getFIDProgram();
                String sql = "select 1 from log_insert where FIDProgram = ?";
//                "SELECT EXISTS(SELECT FROM table WHERE email = ?)"
//                jdbcTemplate.query("SELECT * FROM log_insert WHERE FIDProgram = ?",  new Object[] { fidProgram }, Integer.class);
//                Integer query = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getInt(0));
                Boolean query = jdbcTemplate.query(sql, (ResultSet rs) -> {
                    if (rs.next()) {
                        return true;
                    }
                    return false;
                }, new Object[]{fidProgram});
                System.out.println("hasil : " + query);

                if (!query) {
                    responseData.put("ResponseCode", "01");
                    responseData.put("ResponseDescription", "Failed");
                    responseData.put("ResponseException", "FIDProgram tidak valid, " +
                            "pastikan FIDProgram telah diselaraskan melalui modul ProgramInsert");

                    serverLog.put("ResponseBody", responseData);
                    serverLog.put("endTime", LocalDateTime.now());
                    serverLog.put("ClientIp", servletRequest.getRemoteAddr());

                    log.info("server : {}", serverLog);
                    return responseData;
                }
                String sqlMitra = "\n" +
                        "SELECT\n" +
                        "b.kode_produk_mitra,\n" +
                        "b.kode_program_mitra,\n" +
                        "b.id_dd_bank,\n" +
                        "b.id_dc_produk,\n" +
                        "b.keterangan_produk,\n" +
                        "b.id_dc_peruntukan_kredit,\n" +
                        "b.flag_syariah,\n" +
                        "b.plafon_atas as PlafonMaksimal,\n" +
                        "b.plafon_bawah as PlafonMinimal,\n" +
                        "c.jangka_waktu_atas as JangkaWaktuMaksimal,\n" +
                        "c.jangka_waktu_bawah as JangkaWaktuMinimal,\n" +
                        "c.tarif as RatePremi,\n" +
                        "COALESCE(FIDSkemaPerhitunganBunga,1) AS FIDSkemaPerhitunganBunga,\n" +
                        "COALESCE(SkemaPerhitunganBunga,'Flat') AS SkemaPerhitunganBunga,\n" +
                        "b.id_dd_jenis_lain,\n" +
                        "COALESCE(jns.kode_mitra,0) as FIDKategoriJenisFasilitas,\n" +
                        "coalesce(jns.keterangan,'Tidak memerlukan pendefinisian jenis fasilitas') as KategoriJenisFasilitas,\n" +
                        "coalesce(c.cov_macet,0) as cov_macet,\n" +
                        "coalesce(c.cov_jiwa,0) as cov_jiwa,\n" +
                        "coalesce(c.cov_phk,0) as  cov_phk,\n" +
                        "coalesce(c.cov_kebakaran,0) as  cov_kebakaran,\n" +
                        "coalesce(b.plafon_cac,0) as  plafon_cac,\n" +
                        "c.ijp_minimal,\n" +
                        "a.nilai_pajak_feebase,\n" +
                        "a.nilai_feebase,\n" +
                        "b.total_eksposure,\n" +
                        "b.FIDJenisProduk,\n" +
                        "b.FIDProduk,\n" +
                        "b.NamaJenisProduk,\n" +
                        "b.NamaProduk,\n" +
                        "a.fid_acuan_covering,\n" +
                        "a.acuan_covering\n" +
                        "-- akhir tambahan BRISURF 2021-07\n" +
                        "FROM\n" +
                        "opdd_konfigurasi_ijp a with(nolock) \n" +
                        "JOIN dc_produk_mitra b with(nolock) ON a.id_opdd_konfigurasi_ijp = b.id_opdd_konfigurasi_ijp\n" +
                        "JOIN opdd_master_tarif c with(nolock) ON a.id_opdd_konfigurasi_ijp = c.id_opdd_konfigurasi_ijp\n" +
                        "AND COALESCE(c.id_dc_peruntukan_kredit,0) IN (\n" +
                        "0,\n" +
                        "b.id_dc_peruntukan_kredit\n" +
                        ")\n" +
                        "INNER JOIN dc_jns_pengajuan_mitra d ON a.id_opdd_konfigurasi_ijp = d.id_opdd_konfigurasi_ijp AND b.kode_program_mitra = d.FIDProgram\n" +
                        "LEFT JOIN dd_jenis_lain jns on jns.id_dd_jenis_lain = b.id_dd_jenis_lain\n" +
                        "WHERE\n" +
                        "a.id_dd_bank = 15\n" +
                        "AND GETDATE() BETWEEN tgl_awal_pks_sp3 AND tgl_akhir_pks_sp3\n" +
                        "AND kode_program_mitra = ?";
                Boolean query1 = jdbcTemplate.query(sqlMitra, (ResultSet rs) -> {
                    if (rs.next()) {
                        return true;
                    }
                    return false;
                }, fidProgram);
                System.out.println("query nya : " + query1);
                if (!query1) {
                    responseData.put("ResponseCode", "01");
                    responseData.put("ResponseDescription", "Failed");
                    responseData.put("ResponseException",
                            "FIDProgram belum dikonfigurasi oleh Jamkrindo (dimungkinkan karena ProgramInsert baru), harap disampaikan kepada tim TI Jamkrindo atau via divisi.ti@jamkrindo.co.id");

                    serverLog.put("ResponseBody", responseData);
                    serverLog.put("endTime", LocalDateTime.now());
                    serverLog.put("ClientIp", servletRequest.getRemoteAddr());
                    serverLog.put("ClientIp2", servletRequest.getRemoteUser());
                    serverLog.put("ClientIp3", servletRequest.getRemoteHost());

                    log.info("server : {}", serverLog);
                    return responseData;
                }


            }

        }

        return responseData;
    }


    @Async("asyncExecutor")
    public CompletableFuture<Boolean> isExist(String fidProgram) throws InterruptedException {
        String sql = "select 1 from log_insert where FIDProgram = ?";
        Boolean query = jdbcTemplate.query(sql, (ResultSet rs) -> {
            if (rs.next()) {
                return true;
            }
            return false;
        }, new Object[]{fidProgram});
        System.out.println("Execute method asynchronously - " +
                Thread.currentThread().getName());
        Thread.sleep(1000L);
        return CompletableFuture.completedFuture(query);
    }

    @Async("asyncExecutor")
    public CompletableFuture<Boolean> queryMitra(String fid) throws InterruptedException {
        String sqlMitra = "\n" +
                "SELECT\n" +
                "b.kode_produk_mitra,\n" +
                "b.kode_program_mitra,\n" +
                "b.id_dd_bank,\n" +
                "b.id_dc_produk,\n" +
                "b.keterangan_produk,\n" +
                "b.id_dc_peruntukan_kredit,\n" +
                "b.flag_syariah,\n" +
                "b.plafon_atas as PlafonMaksimal,\n" +
                "b.plafon_bawah as PlafonMinimal,\n" +
                "c.jangka_waktu_atas as JangkaWaktuMaksimal,\n" +
                "c.jangka_waktu_bawah as JangkaWaktuMinimal,\n" +
                "c.tarif as RatePremi,\n" +
                "COALESCE(FIDSkemaPerhitunganBunga,1) AS FIDSkemaPerhitunganBunga,\n" +
                "COALESCE(SkemaPerhitunganBunga,'Flat') AS SkemaPerhitunganBunga,\n" +
                "b.id_dd_jenis_lain,\n" +
                "COALESCE(jns.kode_mitra,0) as FIDKategoriJenisFasilitas,\n" +
                "coalesce(jns.keterangan,'Tidak memerlukan pendefinisian jenis fasilitas') as KategoriJenisFasilitas,\n" +
                "coalesce(c.cov_macet,0) as cov_macet,\n" +
                "coalesce(c.cov_jiwa,0) as cov_jiwa,\n" +
                "coalesce(c.cov_phk,0) as  cov_phk,\n" +
                "coalesce(c.cov_kebakaran,0) as  cov_kebakaran,\n" +
                "coalesce(b.plafon_cac,0) as  plafon_cac,\n" +
                "c.ijp_minimal,\n" +
                "a.nilai_pajak_feebase,\n" +
                "a.nilai_feebase,\n" +
                "b.total_eksposure,\n" +
                "b.FIDJenisProduk,\n" +
                "b.FIDProduk,\n" +
                "b.NamaJenisProduk,\n" +
                "b.NamaProduk,\n" +
                "a.fid_acuan_covering,\n" +
                "a.acuan_covering\n" +
                "-- akhir tambahan BRISURF 2021-07\n" +
                "FROM\n" +
                "opdd_konfigurasi_ijp a with(nolock) \n" +
                "JOIN dc_produk_mitra b with(nolock) ON a.id_opdd_konfigurasi_ijp = b.id_opdd_konfigurasi_ijp\n" +
                "JOIN opdd_master_tarif c with(nolock) ON a.id_opdd_konfigurasi_ijp = c.id_opdd_konfigurasi_ijp\n" +
                "AND COALESCE(c.id_dc_peruntukan_kredit,0) IN (\n" +
                "0,\n" +
                "b.id_dc_peruntukan_kredit\n" +
                ")\n" +
                "INNER JOIN dc_jns_pengajuan_mitra d ON a.id_opdd_konfigurasi_ijp = d.id_opdd_konfigurasi_ijp AND b.kode_program_mitra = d.FIDProgram\n" +
                "LEFT JOIN dd_jenis_lain jns on jns.id_dd_jenis_lain = b.id_dd_jenis_lain\n" +
                "WHERE\n" +
                "a.id_dd_bank = 15\n" +
                "AND GETDATE() BETWEEN tgl_awal_pks_sp3 AND tgl_akhir_pks_sp3\n" +
                "AND kode_program_mitra = ?";
        Boolean query1 = jdbcTemplate.query(sqlMitra, (ResultSet rs) -> {
            if (rs.next()) {
                return true;
            }
            return false;
        }, fid);
        System.out.println("Execute method asynchronously - " +
                Thread.currentThread().getName());
        Thread.sleep(1000L);
        return CompletableFuture.completedFuture(query1);
    }

    public CompletableFuture<KonfigurasiMitra> getData(String fid) throws InterruptedException {
        String sql = "select flag_dasar, nomor_pks_sp3, tgl_awal_pks_sp3, tgl_akhir_pks_sp3, coalesce(c.cov_macet,0) as cov_macet, " +
                "COALESCE(FIDSkemaPerhitunganBunga,1) AS FIDSkemaPerhitunganBunga, " +
                " b.id_dd_bank, b.id_dc_produk, b.keterangan_produk, b.id_dc_peruntukan_kredit, b.flag_syariah, b.plafon_atas as PlafonMaksimal, " +
                " b.plafon_bawah as PlafonMinimal, c.jangka_waktu_atas as JangkaWaktuMaksimal, c.jangka_waktu_bawah as JangkaWaktuMinimal " +
                "from opdd_konfigurasi_ijp a with(nolock) JOIN dc_produk_mitra b with(nolock) ON a.id_opdd_konfigurasi_ijp = b.id_opdd_konfigurasi_ijp " +
                "JOIN opdd_master_tarif c with(nolock) ON a.id_opdd_konfigurasi_ijp = c.id_opdd_konfigurasi_ijp " +
                "AND COALESCE(c.id_dc_peruntukan_kredit,0) IN (0,b.id_dc_peruntukan_kredit) " +
                "INNER JOIN dc_jns_pengajuan_mitra d ON a.id_opdd_konfigurasi_ijp = d.id_opdd_konfigurasi_ijp AND b.kode_program_mitra = d.FIDProgram " +
                "LEFT JOIN dd_jenis_lain jns on jns.id_dd_jenis_lain = b.id_dd_jenis_lain " +
                "WHERE a.id_dd_bank = 15 and GETDATE() BETWEEN tgl_awal_pks_sp3 AND tgl_akhir_pks_sp3 AND kode_program_mitra=? ";

        System.out.println("Execute method asynchronously - " +
                Thread.currentThread().getName());
        Object[] objects = {fid};
        KonfigurasiMitra konfigurasiMitra1 = jdbcTemplate.queryForObject(sql,
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
                }, objects);
        Thread.sleep(1000L);
        return CompletableFuture.completedFuture(konfigurasiMitra1);
    }

    private Map<String, Object> validateFIDProgram(Map<String, Object> responseData, String fidProgram) {
        responseData.put("ResponseCode", "00");
        responseData.put("ResponseDescription", "Success");
        responseData.put("ResponseException", "");

        String pattern = "^[0-9]+";
        System.out.println("pattern " + fidProgram.matches(pattern));
        if (!fidProgram.matches(pattern)) {
            responseData.put("ResponseCode", "01");
            responseData.put("ResponseDescription", "Failed");
            responseData.put("ResponseException", "field request FIDProgram ONly number");
            return responseData;
        }
        return responseData;
    }
}
